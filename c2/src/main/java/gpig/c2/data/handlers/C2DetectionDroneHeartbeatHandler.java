package gpig.c2.data.handlers;

import gpig.common.data.DroneState;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class C2DetectionDroneHeartbeatHandler implements DetectionDroneHeartbeatHandler {
    private final ConcurrentHashMap<UUID, DroneState> states;

    public C2DetectionDroneHeartbeatHandler(ConcurrentHashMap<UUID, DroneState> states) {
        this.states = states;
    }

    @Override
    public void handle(DetectionDroneHeartbeat message) {
        states.put(message.origin, message.state);
    }
}
