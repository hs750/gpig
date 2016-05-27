package gpig.drones.detection;

import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.handlers.FailCommandHandler;

public class DetectionFatalFailureHandler implements FailCommandHandler {
    private final DetectionDrone det;

    public DetectionFatalFailureHandler(DetectionDrone det) {
        this.det = det;
    }

    @Override
    public void handle(FailCommand message) {
        if (message.drone.equals(det.thisDrone)) {
            if (message.type == FailType.FATAL) {
                det.movementBehaviour.clearPath();
            }
        }

    }

}
