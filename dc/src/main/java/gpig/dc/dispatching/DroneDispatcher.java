package gpig.dc.dispatching;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import gpig.common.data.DeploymentArea;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.messages.DroneHeartbeat;
import gpig.common.messages.SetPath;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;

public abstract class DroneDispatcher extends Thread {
    protected DeploymentArea currentLocation;
    protected boolean deployable = false;
    protected boolean deployed = false;

    private LinkedHashMap<UUID, DroneHeartbeat> allDrones;
    private MessageSender messager;
    private RecoveryStrategy recoveryStrategy;

    private ConcurrentLinkedQueue<Path> tasks;

    public DroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy, DeploymentArea currentLocation) {
        this.messager = messager;
        allDrones = new LinkedHashMap<>();
        this.recoveryStrategy = recoveryStrategy;
        this.currentLocation = currentLocation;
        tasks = new ConcurrentLinkedQueue<>();
    }

    public void setCurrentLocation(DeploymentArea location) {
        currentLocation = location;
    }

    public void deployDrones() {
        if (allDronesRecovered()) {
            deployed = false;
        }
        if (!deployable && !deployed) {
            deployable = true;
            start();
        }
    }

    public synchronized void recoverDrones() {
        deployable = false;

        for (DroneHeartbeat drone : allDrones.values()) {
            if (drone.state != DroneState.UNDEPLOYED) {
                SetPath sp = new SetPath(recoveryStrategy.getPath(getLocation()), drone.origin);
                messager.send(sp);
            }
        }

    }

    public synchronized boolean allDronesRecovered() {
        boolean recovered = true;
        for (DroneHeartbeat drone : allDrones.values()) {
            if (drone.state != DroneState.UNDEPLOYED) {
                recovered = false;
            }
        }
        return recovered;
    }

    protected synchronized boolean allDronesActive() {
        for (DroneHeartbeat drone : allDrones.values()) {
            if (drone.state == DroneState.UNDEPLOYED) {
                return false;
            }
        }
        return true;
    }

    protected synchronized void activateDrone(UUID drone) {
        allDrones.remove(drone); // remove the current heartbeat
    }

    protected synchronized UUID getNextInactiveDrone() {
        for (DroneHeartbeat drone : allDrones.values()) {
            if (drone.state == DroneState.UNDEPLOYED) {
                return drone.origin;
            }
        }
        return null;
    }

    public void handle(DroneHeartbeat heartbeat) {
        allDrones.put(heartbeat.origin, heartbeat);
    }

    @Override
    public void run() {
        super.run();

        while (deployable) {
            while (allDronesActive()) {
                try {
                    Thread.sleep(1000); // Wait for a drone to become available
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            UUID assignee = getNextInactiveDrone();

            Path p = tasks.poll();
            if (p == null) {
                taskListEmpty();
            } else {
                SetPath sp = new SetPath(p, assignee);
                messager.send(sp);
                Log.info("Displatched Drone: %s", assignee);

                activateDrone(assignee);
            }
        }
    }

    protected void addTask(Path task) {
        tasks.add(task);
    }

    protected void addTasks(Collection<Path> tasks) {
        this.tasks.addAll(tasks);
    }
    
    protected Location getLocation(){
        return currentLocation.deploymentArea.centre;
    }

    protected abstract void taskListEmpty();
}
