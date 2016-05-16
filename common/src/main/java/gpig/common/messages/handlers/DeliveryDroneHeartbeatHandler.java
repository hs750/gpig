package gpig.common.messages.handlers;

import gpig.common.messages.DeliveryDroneHeartbeat;

@FunctionalInterface
public interface DeliveryDroneHeartbeatHandler {
    public void handle(DeliveryDroneHeartbeat message);
}
