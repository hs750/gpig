package gpig.drones.detection;

import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.handlers.FailCommandHandler;

public class DetectionBatteryFailureHandler implements FailCommandHandler {
    private final DetectionDrone det;

    public DetectionBatteryFailureHandler(DetectionDrone det) {
        this.det = det;
    }

    @Override
    public void handle(FailCommand message) {
        if (message.drone.equals(det.thisDrone)) {
            if (message.type == FailType.BATTERY) {
                det.battery.setBatteryLevel(message.batteryPercentage);
                det.state.setFaulty();
            }
        }

    }

}
