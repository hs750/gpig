package gpig.common.messages;

import java.time.LocalDateTime;
import java.util.UUID;

import gpig.common.data.DroneState;
import gpig.common.data.Location;

public abstract class DroneHeartbeat {
    public final DroneState state;
    public final UUID origin;
    public final Location location;
    public final LocalDateTime timestamp;
    
    public DroneHeartbeat(UUID origin, DroneState state, Location location){
        this.state = state;
        this.origin = origin;
        this.location = location;
        this.timestamp = LocalDateTime.now();
        
    }
    
    private DroneHeartbeat() {
        state = null;
        origin = null;
        location = null;
        timestamp = null;
    }
}
