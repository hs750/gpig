package gpig.drones.delivery;

import gpig.common.data.DroneState;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;

public class DeliveryDroneAssignmentHandler implements DeliveryAssignmentHandler {
    private final DeliveryDrone drone;

    public DeliveryDroneAssignmentHandler(DeliveryDrone drone) {
        this.drone = drone;
    }

    @Override
    public void handle(DeliveryAssignment message) {
        if (drone.getState() == DroneState.UNDEPLOYED) {
            drone.assign(message.assignment);
        }
    }
}
