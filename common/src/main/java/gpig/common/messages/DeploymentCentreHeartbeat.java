package gpig.common.messages;

import gpig.common.data.Location;

import java.util.UUID;

public class DeploymentCentreHeartbeat {
    public final UUID origin;
    public final Location location;

    public DeploymentCentreHeartbeat(UUID origin, Location location) {
        this.origin = origin;
        this.location = location;
    }
    
    private DeploymentCentreHeartbeat(){
        origin = null;
        location = null;
    }
}
