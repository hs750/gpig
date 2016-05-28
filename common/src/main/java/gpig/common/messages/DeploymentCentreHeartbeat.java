package gpig.common.messages;

import gpig.common.data.DCState;
import gpig.common.data.Location;

import java.util.UUID;

public class DeploymentCentreHeartbeat {
    public final UUID origin;
    public final Location location;
    public final DCState state;

    public DeploymentCentreHeartbeat(UUID origin, Location location, DCState state) {
        this.origin = origin;
        this.location = location;
        this.state = state;
    }
    
    private DeploymentCentreHeartbeat(){
        origin = null;
        location = null;
        state = null;
    }
}
