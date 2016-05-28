package gpig.dc.dispatching;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import gpig.common.data.Assignment;
import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.messages.handlers.DeliveryNotificationHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;

public class DeliveryDroneDispatcher extends DroneDispatcher
        implements DeliveryDroneHeartbeatHandler, DeliveryAssignmentHandler, DeliveryNotificationHandler {
    private Set<UUID> pendingTimeouts;

    public DeliveryDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy,
            DeploymentArea currentLocation, MessageSender c2Messager) {
        super(messager, recoveryStrategy, currentLocation, Constants.DELIVERY_DRONE_SPEED, c2Messager);
        pendingTimeouts = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    protected void taskListEmpty() {
        // Wait for another delivery assignment to be given
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Path calculatePathToDelivery(Location deliveryLocation) {
        if(currentLocation.deploymentArea.contains(deliveryLocation)){
            List<Waypoint> wps = Arrays.asList(new Waypoint(deliveryLocation), new Waypoint(getLocation()));
            Path p = new Path(wps, currentLocation.deploymentArea.centre);
            return p;
        }
        
        return null;
    }

    @Override
    public void handle(DeliveryAssignment message) {
        Location l = message.assignment.detection.person.location;
        Path p = calculatePathToDelivery(l);
        if(p == null){
            Log.warn("Unable to schedule delivery to %s", l);
        }else{
            Log.info("Delivery scheduled to %s", l);
            addTask(new Task(p, message.assignment));
        }

    }

    @Override
    public void handle(DeliveryDroneHeartbeat message) {
        if(pendingTimeouts.contains(message.origin)){
            // If the drone was timed out but returned (comms failure) send notification that the delivery did indeed happen
            sendDeliveryNotification(message.origin, LocalDateTime.now());
            pendingTimeouts.remove(message.origin);
        }
        super.handle(message);
        
    }

    @Override
    protected void handleTimeout(AllocatedTask task) {
        pendingTimeouts.add(task.drone);
        // Only re-allocate delivery if drone doesn't return in the expected time.
        // This is the difference between dead drone and comms out drone
        if(task.expectedReturnTime.isBefore(LocalDateTime.now())){
            if(firstTimeoutBeat(task.drone)){
                addTask(task.task);
            }
            
            DeliveryDroneHeartbeat ddh = new DeliveryDroneHeartbeat(task.drone, DroneState.CRASHED, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }else{
            DeliveryDroneHeartbeat ddh = new DeliveryDroneHeartbeat(task.drone, DroneState.FAULTY, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }
        
    }

    @Override
    public void handle(DeliveryNotification message) {
        sendDeliveryNotification(message.deliveryDrone, message.timestamp);
    }
    
    private void sendDeliveryNotification(UUID drone, LocalDateTime timestamp){
        Optional<Assignment> a = allocatedTasks.get(drone).task.assignment;
        if(a.isPresent()){
            c2Messager.send(new DeliveryNotification(timestamp, a.get()));
        }else{
            Log.error("Unable to forward delivery notification for drone " + drone);
        }
    }

}
