package gpig.common.movement;

import gpig.common.data.Location;
import gpig.common.data.Path;

public interface MovementBehaviour {
    Location currentLocation();
    boolean isMoving();

    default boolean isStationary() {
        return !isMoving();
    }

    void setPath(Path path);
    Location step();
    
    void clearPath();

}
