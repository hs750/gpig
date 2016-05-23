package gpig.common.messages.heartbeater;

import gpig.common.data.DroneState;

@FunctionalInterface
public interface StateProvider {

    public DroneState getState();
}
