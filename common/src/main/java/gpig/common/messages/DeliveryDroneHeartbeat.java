package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.DroneState;

public class DeliveryDroneHeartbeat extends DroneHeartbeat{

    public DeliveryDroneHeartbeat(UUID origin, DroneState state){
        super(origin, state);
    }
    private DeliveryDroneHeartbeat() {
        super(null, null);
    }
}
