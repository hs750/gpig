package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;

public class DetectionDroneHeartbeat {
    public final DroneState state;
    public final UUID origin;
    
    public DetectionDroneHeartbeat(UUID origin, DroneState state){
        this.state = state;
        this.origin = origin;
    }
    
    private DetectionDroneHeartbeat() {
        state = null;
        origin = null;
    }

}
