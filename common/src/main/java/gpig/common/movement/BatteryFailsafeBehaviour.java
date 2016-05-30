package gpig.common.movement;

import gpig.common.data.Location;
import gpig.common.data.Path;

import java.util.Optional;

public interface BatteryFailsafeBehaviour {
    boolean isTriggered(Path remainingPath);
    Optional<Path> path(Path remainingPath);
    void setHomeLocation(Location l);
    void restoreBattery();
}
