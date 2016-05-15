package gpig.common.messages.handlers;

import gpig.common.messages.DetectionDroneHeartbeat;

@FunctionalInterface
public interface DetectionDroneHeartbeatHandler {
    public void handle(DetectionDroneHeartbeat message);
}
