package gpig.c2.data.handlers;

import gpig.common.data.DroneState;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class C2DeliveryDroneHeartbeatHandler implements DeliveryDroneHeartbeatHandler {
    private final ConcurrentHashMap<UUID, DroneState> states;

    public C2DeliveryDroneHeartbeatHandler(ConcurrentHashMap<UUID, DroneState> states) {
        this.states = states;
    }

    @Override
    public void handle(DeliveryDroneHeartbeat message) {
        states.put(message.origin, message.state);
    }
}
