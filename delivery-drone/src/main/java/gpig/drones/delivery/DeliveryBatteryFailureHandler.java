package gpig.drones.delivery;

import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.handlers.FailCommandHandler;

public class DeliveryBatteryFailureHandler implements FailCommandHandler {
    private final DeliveryDrone del;

    public DeliveryBatteryFailureHandler(DeliveryDrone del) {
        this.del = del;
    }

    @Override
    public void handle(FailCommand message) {
        if (message.drone.equals(del.thisDrone)) {
            if (message.type == FailType.BATTERY) {
                del.battery.setBatteryLevel(message.batteryPercentage);
                del.state.setFaulty();
            }
        }

    }

}
