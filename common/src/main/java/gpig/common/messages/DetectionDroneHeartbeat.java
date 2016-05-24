package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;
import gpig.common.data.Location;

public class DetectionDroneHeartbeat extends DroneHeartbeat{

    public DetectionDroneHeartbeat(UUID origin, DroneState state, Location location) {
        super(origin, state, location);
    }

    private DetectionDroneHeartbeat() {
        super(null, null, null);
    }

}
