package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;

public abstract class DroneHeartbeat {
    public final DroneState state;
    public final UUID origin;
    
    public DroneHeartbeat(UUID origin, DroneState state){
        this.state = state;
        this.origin = origin;
    }
    
    private DroneHeartbeat() {
        state = null;
        origin = null;
    }
}
