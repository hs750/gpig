package gpig.dc.dispatching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public class DetectionDroneDispatcher extends DroneDispatcher implements DetectionDroneHeartbeatHandler {
    private final Kilometres edgeDistance = new Kilometres(8.6);

    public DetectionDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy,
            DeploymentArea currentLocation) {
        super(messager, recoveryStrategy, currentLocation, Constants.DETECTION_DRONE_SPEED);
    }

    private ArrayList<Path> calculateSearchPattern() {
        // Produces a basic hexagon pattern

        ArrayList<Path> deployments = new ArrayList<>();

        for (int segment = 0; segment < 6; segment++) {
            Location l1 = getLocation().locationAt(segment * 60, edgeDistance);
            Location l2 = getLocation().locationAt((segment * 60 + 60) % 360, edgeDistance);

            List<Waypoint> waypoints = Arrays.asList(new Waypoint(l1), new Waypoint(l2), new Waypoint(getLocation()));

            Path p = new Path(waypoints);
            deployments.add(p);

        }

        return deployments;

    }

    @Override
    public void run() {
        addTasks(calculateSearchPattern());
        super.run();
    }

    @Override
    public synchronized void handle(DetectionDroneHeartbeat message) {
        super.handle(message);
    }

    @Override
    protected void taskListEmpty() {
        deployable = false; // stop attempting to deploy drones when task done
        Log.info("Detection sweep completed");
    }

    @Override
    protected void handleTimeout(AllocatedTask task) {
        // When a detection drone stops responding re-allocate its task to another drone
        addTask(task.task);
        unallocateTask(task.drone);
        
    }

}
