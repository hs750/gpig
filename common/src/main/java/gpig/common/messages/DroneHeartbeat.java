package gpig.common.messages;

import java.time.LocalDateTime;
import java.util.UUID;

import gpig.common.data.DroneState;

public abstract class DroneHeartbeat {
    public final DroneState state;
    public final UUID origin;
    public final LocalDateTime timestamp;
    
    public DroneHeartbeat(UUID origin, DroneState state){
        this.state = state;
        this.origin = origin;
        this.timestamp = LocalDateTime.now();
    }
    
    private DroneHeartbeat() {
        state = null;
        origin = null;
        timestamp = null;
    }
}
