package gpig.drones.detection;

import java.util.UUID;

import gpig.common.data.Constants;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.heartbeater.Heartbeater;
import gpig.common.messages.heartbeater.LocationProvider;
import gpig.common.messages.heartbeater.StateProvider;
import gpig.common.networking.MessageSender;

public class DetectionHeartbeater extends Heartbeater {

    public DetectionHeartbeater(UUID thisDrone, MessageSender messenger, LocationProvider locationProvider, StateProvider stateProvider, int heartbeatRate) {
        super(thisDrone, heartbeatRate, messenger, locationProvider, stateProvider);
    }

    @Override
    protected void beat() {
        DetectionDroneHeartbeat ddh = new DetectionDroneHeartbeat(thisDrone, stateProvier.getState(), locationProvider.currentLocation());
        messenger.send(ddh);
    }
}
