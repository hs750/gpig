package gpig.dc.dispatching;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;

public class DetectionDroneDispatcher extends DroneDispatcher implements DetectionDroneHeartbeatHandler {
    private final Kilometres edgeDistance = new Kilometres(8.6);

    public DetectionDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy,
            DeploymentArea currentLocation, MessageSender c2Messager) {
        super(messager, recoveryStrategy, currentLocation, Constants.DETECTION_DRONE_SPEED, c2Messager);
    }

    private ArrayList<Task> calculateSearchPattern() {
        // Produces a basic hexagon pattern

        ArrayList<Task> deployments = new ArrayList<>();

        for (int segment = 0; segment < 6; segment++) {
            Location l1 = getLocation().locationAt(segment * 60, edgeDistance);
            Location l2 = getLocation().locationAt((segment * 60 + 60) % 360, edgeDistance);

            List<Waypoint> waypoints = Arrays.asList(new Waypoint(l1), new Waypoint(l2), new Waypoint(getLocation()));

            Path p = new Path(waypoints, currentLocation.deploymentArea.centre);
            deployments.add(new Task(p, null));

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
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTimeout(AllocatedTask task) {
        // When a detection drone stops responding re-allocate its task to another drone
        if(firstTimeoutBeat(task.drone)){
            addTask(task.task);
        }
        if(task.expectedReturnTime.isBefore(LocalDateTime.now())){
            DetectionDroneHeartbeat ddh = new DetectionDroneHeartbeat(task.drone, DroneState.CRASHED, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }else{
            DetectionDroneHeartbeat ddh = new DetectionDroneHeartbeat(task.drone, DroneState.FAULTY, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }
        
    }

}
