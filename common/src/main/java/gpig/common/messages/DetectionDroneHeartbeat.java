package gpig.common.messages;

import java.util.UUID;

public class DetectionDroneHeartbeat {
    public final Boolean deployed;
    public final UUID origin;
    
    public DetectionDroneHeartbeat(UUID origin, boolean deployed){
        this.deployed = deployed;
        this.origin = origin;
    }
    
    private DetectionDroneHeartbeat() {
        deployed = null;
        origin = null;
    }

}
