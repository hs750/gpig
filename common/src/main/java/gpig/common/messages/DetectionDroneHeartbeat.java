package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;

public class DetectionDroneHeartbeat extends DroneHeartbeat{

    public DetectionDroneHeartbeat(UUID origin, DroneState state) {
        super(origin, state);
    }

    private DetectionDroneHeartbeat() {
        super(null, null);
    }

}
