package gpig.dc.dispatching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public class DetectionDroneDispatcher extends Thread implements DetectionDroneHeartbeatHandler {
    private final Kilometres edgeDistance = new Kilometres(8.6);
    private Location currentLocation;
    private boolean deployable = false;
    private boolean deployed = false;

    private LinkedHashMap<UUID, DetectionDroneHeartbeat> allDrones;
    private MessageSender messager;
    private RecoveryStrategy recoveryStrategy;

    public DetectionDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy, Location currentLocation) {
        this.messager = messager;
        allDrones = new LinkedHashMap<>();
        this.recoveryStrategy = recoveryStrategy;
        this.currentLocation = currentLocation;
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    public void deployDrones(){
        if(allDronesRecovered()){
            deployed = false;
        }
        if (!deployable && !deployed) {
            deployable = true;
            start();
        }
    }

    private ArrayList<Path> calculateSearchPattern() {
        // Produces a basic hexagon pattern

        ArrayList<Path> deployments = new ArrayList<>();

        for (int segment = 0; segment < 6; segment++) {
            Location l1 = currentLocation.locationAt(segment * 60, edgeDistance);
            Location l2 = currentLocation.locationAt((segment * 60 + 60) % 360, edgeDistance);

            List<Waypoint> waypoints = Arrays.asList(new Waypoint(l1), new Waypoint(l2), new Waypoint(currentLocation));

            Path p = new Path(waypoints);
            deployments.add(p);

        }

        return deployments;

    }

    public synchronized void recoverDrones() {
        deployable = false;

        for (DetectionDroneHeartbeat drone : allDrones.values()) {
            if (drone.state != DroneState.UNDEPLOYED) {
                SetPath sp = new SetPath(recoveryStrategy.getPath(currentLocation), drone.origin);
                messager.send(sp);
            }
        }

    }

    public synchronized boolean allDronesRecovered() {
        boolean recovered = true;
        for (DetectionDroneHeartbeat drone : allDrones.values()) {
            if (drone.state != DroneState.UNDEPLOYED) {
                recovered = false;
            }
        }
        return recovered;
    }

    @Override
    public void run() {
        super.run();
        ArrayList<Path> searchPaths = calculateSearchPattern();
        int searchsDeployed = 0;
        while (deployable) {
            while (allDronesActive()) {
                try {
                    Thread.sleep(1000); // Wait for a drone to become available
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            UUID assignee = getNextInactiveDrone();

            SetPath sp = new SetPath(searchPaths.get(0), assignee);
            messager.send(sp);
            Log.info("Displatched Drone: %s", assignee);
            searchsDeployed++;

            if (searchsDeployed == searchPaths.size()) {
                deployable = false;
            }

            activateDrone(assignee);
        }
    }

    private synchronized boolean allDronesActive() {
        for (DetectionDroneHeartbeat drone : allDrones.values()) {
            if (drone.state == DroneState.UNDEPLOYED) {
                return false;
            }
        }
        return true;
    }

    private synchronized void activateDrone(UUID drone) {
        allDrones.remove(drone); // remove the current heartbeat
    }

    private synchronized UUID getNextInactiveDrone() {
        for (DetectionDroneHeartbeat drone : allDrones.values()) {
            if (drone.state == DroneState.UNDEPLOYED) {
                return drone.origin;
            }
        }
        return null;
    }

    @Override
    public synchronized void handle(DetectionDroneHeartbeat message) {
        allDrones.put(message.origin, message);
    }

}
