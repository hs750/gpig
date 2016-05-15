package gpig.common.messages.handlers;

import gpig.common.messages.DeliveryDroneHeartbeat;

@FunctionalInterface
public interface DeliveryDroneHeartbeadHandler {
    public void handle(DeliveryDroneHeartbeat message);
}
