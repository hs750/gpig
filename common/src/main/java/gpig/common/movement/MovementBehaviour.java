package gpig.common.movement;

import gpig.common.data.Location;
import gpig.common.data.Path;

public interface MovementBehaviour {
    void setPath(Path path);
    Location currentLocation();
    Location step();
}
