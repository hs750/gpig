package gpig.c2.data.handlers;

import gpig.common.data.Location;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.handlers.DeploymentCentreHeartbeatHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class C2DeploymentCentreHeartbeatHandler implements DeploymentCentreHeartbeatHandler {
    private final ConcurrentHashMap<UUID, Location> dcLocations;

    public C2DeploymentCentreHeartbeatHandler(ConcurrentHashMap<UUID, Location> dcLocations) {
        this.dcLocations = dcLocations;
    }

    @Override
    public void handle(DeploymentCentreHeartbeat message) {
        dcLocations.put(message.origin, message.location);
    }
}
