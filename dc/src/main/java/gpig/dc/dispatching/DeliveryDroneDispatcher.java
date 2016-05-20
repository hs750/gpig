package gpig.dc.dispatching;

import java.util.Arrays;
import java.util.List;

import gpig.common.data.DeploymentArea;
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
            DeploymentArea currentLocation) {
        super(messager, recoveryStrategy, currentLocation);
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
            Path p = new Path(wps);
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

}
