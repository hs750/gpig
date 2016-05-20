package gpig.common.movement;

import gpig.common.data.Location;

public interface MovementBehaviour {
    Location currentLocation();
    Location step();
}
