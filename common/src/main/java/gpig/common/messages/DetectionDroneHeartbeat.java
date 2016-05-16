package gpig.common.messages;

import java.util.UUID;

public class DetectionDroneHeartbeat {
    public final Boolean deployed;
    public final UUID origin;
    
    private DetectionDroneHeartbeat() {
        deployed = null;
        origin = null;
    }

}
