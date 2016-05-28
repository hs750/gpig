package gpig.c2.data.handlers;

import gpig.common.data.DCState;
import gpig.common.data.Location;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.handlers.DeploymentCentreHeartbeatHandler;

import java.util.Map;
import java.util.UUID;

public class C2DeploymentCentreHeartbeatHandler implements DeploymentCentreHeartbeatHandler {
    private final Map<UUID, Location> dcLocations;
    private final Map<UUID, DCState> states;

    public C2DeploymentCentreHeartbeatHandler(Map<UUID, Location> dcLocations, Map<UUID, DCState> states) {
        this.dcLocations = dcLocations;
        this.states = states;
    }

    @Override
    public void handle(DeploymentCentreHeartbeat message) {
        dcLocations.put(message.origin, message.location);
        states.put(message.origin, message.state);
    }
}
