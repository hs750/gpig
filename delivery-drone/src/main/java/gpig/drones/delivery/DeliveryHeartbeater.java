package gpig.drones.delivery;

import java.util.UUID;

import gpig.common.data.Constants;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.heartbeater.Heartbeater;
import gpig.common.messages.heartbeater.LocationProvider;
import gpig.common.messages.heartbeater.StateProvider;
import gpig.common.networking.MessageSender;

public class DeliveryHeartbeater extends Heartbeater {

    public DeliveryHeartbeater(UUID thisDrone, MessageSender messenger, LocationProvider locationProvider, StateProvider stateProvider, int heartbeatRate) {
        super(thisDrone, heartbeatRate, messenger, locationProvider, stateProvider);
    }

    @Override
    protected void beat() {
        DeliveryDroneHeartbeat ddh = new DeliveryDroneHeartbeat(thisDrone, stateProvier.getState(), locationProvider.currentLocation());
        messenger.send(ddh);
    }
}
