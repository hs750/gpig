package gpig.dc.dispatching;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import gpig.common.data.Assignment;
import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.messages.DroneHeartbeat;
import gpig.common.messages.SetPath;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public abstract class DroneDispatcher extends Thread {
    protected DeploymentArea currentLocation;
    protected boolean deployable = false;
    protected boolean deployed = false;
    private KMPH droneSpeed;

    protected Map<UUID, DroneHeartbeat> allDrones;
    private MessageSender droneMessager;
    protected MessageSender c2Messager;
    private RecoveryStrategy recoveryStrategy;
    private Map<UUID, LocalDateTime> deployedDrones;

    private ConcurrentLinkedQueue<Task> tasks;
    ConcurrentHashMap<UUID, AllocatedTask> allocatedTasks;

    public DroneDispatcher(MessageSender droneMessager, RecoveryStrategy recoveryStrategy, DeploymentArea currentLocation, KMPH droneSpeed, MessageSender c2Messager) {
        this.droneMessager = droneMessager;
        this.c2Messager = c2Messager;
        allDrones = Collections.synchronizedMap(new LinkedHashMap<>());
        this.recoveryStrategy = recoveryStrategy;
        this.currentLocation = currentLocation;
        tasks = new ConcurrentLinkedQueue<>();
        allocatedTasks = new ConcurrentHashMap<>();
        this.droneSpeed = droneSpeed;
        this.deployedDrones = new ConcurrentHashMap<>();
        

        new HeartbeatTimer(Constants.DRONE_HEARTBEAT_TIMEOUT, allocatedTasks, allDrones, this).start();
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
                droneMessager.send(sp);
            }
        }

    }

    public synchronized boolean allDronesRecovered() {
        boolean recovered = true;
        for (DroneHeartbeat drone : allDrones.values()) {
            if (deployedDrones.containsKey(drone.origin)) {
                recovered = false;
            }
        }
        return recovered;
    }

    protected synchronized boolean allDronesActive() {
        for (DroneHeartbeat drone : allDrones.values()) {
            if (!deployedDrones.containsKey(drone.origin)) {
                return false;
            }
        }
        return true;
    }

    protected synchronized void activateDrone(UUID drone, Task task) {
        //allDrones.remove(drone); // remove the current (undeployed) heartbeat
        deployedDrones.put(drone, LocalDateTime.now());
        allocatedTasks.put(drone, new AllocatedTask(drone, task, currentLocation.deploymentArea.centre, droneSpeed));

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
        DroneHeartbeat dh = allDrones.put(heartbeat.origin, heartbeat);
        if(heartbeat.state == DroneState.UNDEPLOYED){
            LocalDateTime deployTime = deployedDrones.get(heartbeat.origin);
            if(deployTime != null){
                
                // Allow some time for the heartbeats to reflect deployment to prevent immediate redeployment.
                if(deployTime.isBefore(LocalDateTime.now().minus(10000, ChronoUnit.MILLIS))){
                    deployedDrones.remove(heartbeat.origin);
                }
            }
        }
        if(dh == null){
            Log.info("Descovered new drone " + heartbeat.getClass().getSimpleName() + " " + heartbeat.origin);
        }

        if (heartbeat.state == DroneState.UNDEPLOYED) {
            unallocateTask(heartbeat.origin);
        }
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

            Task t = tasks.poll();
            if (t == null) {
                taskListEmpty();
            } else {
                SetPath sp = new SetPath(t.path, assignee);
                droneMessager.send(sp);
                Log.info("Displatched Drone: %s", assignee);

                activateDrone(assignee, t);
            }
        }
    }

    protected void addTask(Task task) {
        tasks.add(task);
    }

    protected void addTasks(Collection<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    protected Location getLocation() {
        return currentLocation.deploymentArea.centre;
    }

    protected void unallocateTask(UUID drone) {
        allocatedTasks.remove(drone);
    }

    protected abstract void taskListEmpty();

    protected abstract void handleTimeout(AllocatedTask task);

    private class HeartbeatTimer extends Thread {
        private int timeoutLength;
        private Map<UUID, AllocatedTask> currentTasks;
        private Map<UUID, DroneHeartbeat> droneHeartbeats;
        private DroneDispatcher dispatcher;

        public HeartbeatTimer(int timeoutLength, Map<UUID, AllocatedTask> currentTasks,
                Map<UUID, DroneHeartbeat> droneHeartbeats, DroneDispatcher dispatcher) {
            this.timeoutLength = timeoutLength;
            this.currentTasks = currentTasks;
            this.droneHeartbeats = droneHeartbeats;
            this.dispatcher = dispatcher;
        }

        @Override
        public void run() {
            super.run();

            while (true) {
                currentTasks.forEach((drone, task) -> {
                    LocalDateTime lastHeartbeat = droneHeartbeats.get(drone).timestamp;
                    LocalDateTime now = LocalDateTime.now();
                    long timeDiff = ChronoUnit.MILLIS.between(lastHeartbeat, now);
                    if (timeDiff > timeoutLength) {
                        dispatcher.handleTimeout(task);
                    }

                });

                // Don't check too often
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected class AllocatedTask {
        public UUID drone;
        public Task task;
        public LocalDateTime expectedReturnTime;

        public AllocatedTask(UUID drone, Task task, Location start, KMPH droneSpeed) {
            this.drone = drone;
            this.task = task;

            Kilometres distance = task.path.totalDistance();
            
            if (task.path.length() > 0) {
                distance  = distance.add(start.distanceFrom(task.path.get(0).location));
            }

            double timeHours = distance.value() / (droneSpeed.value() * Constants.SPEED_SCALING_FACTOR);
            timeHours *= 1.05; // Add 5% lee-way 
            long timeMillis = (long) Math.ceil(timeHours * 60 * 60 * 1000); // overestimate
            expectedReturnTime = LocalDateTime.now().plus(Duration.ofMillis(timeMillis));
        }
    }
    
    protected class Task {
        public Optional<Assignment> assignment;
        public Path path;
        
        public Task(Path p, Assignment a) {
            path = p;
            assignment = Optional.ofNullable(a);
        }
    }
}
