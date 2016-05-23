package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;
import gpig.common.data.Location;

public class DeliveryDroneHeartbeat extends DroneHeartbeat{

    public DeliveryDroneHeartbeat(UUID origin, DroneState state, Location location){
        super(origin, state, location);
    }
    private DeliveryDroneHeartbeat() {
        super(null, null, null);
    }
}
