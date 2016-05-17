package gpig.dc.dispatching;

import java.util.Arrays;
import java.util.List;

import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.movement.RecoveryStrategy;
import gpig.common.networking.MessageSender;

public class DeliveryDroneDispatcher extends DroneDispatcher
        implements DeliveryDroneHeartbeatHandler, DeliveryAssignmentHandler {

    public DeliveryDroneDispatcher(MessageSender messager, RecoveryStrategy recoveryStrategy,
            Location currentLocation) {
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
        List<Waypoint> wps = Arrays.asList(new Waypoint(deliveryLocation), new Waypoint(currentLocation));
        Path p = new Path(wps);
        return p;
    }

    @Override
    public void handle(DeliveryAssignment message) {
        Path p = calculatePathToDelivery(message.assignment.detection.location);
        addTask(p);

    }

    @Override
    public void handle(DeliveryDroneHeartbeat message) {
        super.handle(message);

    }

}
