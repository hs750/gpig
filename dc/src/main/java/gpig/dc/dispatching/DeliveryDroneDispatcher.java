package gpig.dc.dispatching;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;

public class DeliveryDroneDispatcher extends DroneDispatcher
        implements DeliveryDroneHeartbeatHandler, DeliveryAssignmentHandler {

    public DeliveryDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy,
            DeploymentArea currentLocation, MessageSender c2Messager) {
        super(messager, recoveryStrategy, currentLocation, Constants.DELIVERY_DRONE_SPEED, c2Messager);
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
            addTask(p);
        }

    }

    @Override
    public void handle(DeliveryDroneHeartbeat message) {
        super.handle(message);

    }

    @Override
    protected void handleTimeout(AllocatedTask task) {
        // Only re-allocate delivery if drone doesn't return in the expected time.
        // This is the difference between dead drone and comms out drone
        if(task.expectedReturnTime.isBefore(LocalDateTime.now())){
            addTask(task.task);
            unallocateTask(task.drone);
            
            DeliveryDroneHeartbeat ddh = new DeliveryDroneHeartbeat(task.drone, DroneState.CRASHED, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }else{
            DeliveryDroneHeartbeat ddh = new DeliveryDroneHeartbeat(task.drone, DroneState.FAULTY, allDrones.get(task.drone).location);
            c2Messager.send(ddh);
        }
        
    }

}
