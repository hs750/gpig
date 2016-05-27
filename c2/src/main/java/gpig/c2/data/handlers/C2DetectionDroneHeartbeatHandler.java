package gpig.c2.data.handlers;

import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class C2DetectionDroneHeartbeatHandler implements DetectionDroneHeartbeatHandler {
    private final ConcurrentHashMap<UUID, DroneState> states;
    private final ConcurrentHashMap<UUID, Location> locations;

    public C2DetectionDroneHeartbeatHandler(ConcurrentHashMap<UUID, DroneState> states,
            ConcurrentHashMap<UUID, Location> locations) {
        this.states = states;
        this.locations = locations;
    }

    @Override
    public void handle(DetectionDroneHeartbeat message) {
        states.put(message.origin, message.state);
        if (message.state == DroneState.UNDEPLOYED) {
            locations.remove(message.origin);
        } else {
            locations.put(message.origin, message.location);
        }
    }
}
