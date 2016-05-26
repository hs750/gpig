package gpig.common.messages.heartbeater;

import gpig.common.data.Location;

@FunctionalInterface
public interface LocationProvider {

    public Location currentLocation();
}
