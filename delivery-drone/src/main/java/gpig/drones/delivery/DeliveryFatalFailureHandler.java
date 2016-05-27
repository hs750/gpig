package gpig.drones.delivery;

import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.handlers.FailCommandHandler;

public class DeliveryFatalFailureHandler implements FailCommandHandler {
    private final DeliveryDrone del;

    public DeliveryFatalFailureHandler(DeliveryDrone del) {
        this.del = del;
    }

    @Override
    public void handle(FailCommand message) {
        if (message.drone.equals(del.thisDrone)) {
            if (message.type == FailType.FATAL) {
                del.movementBehaviour.clearPath();
            }
        }

    }

}
