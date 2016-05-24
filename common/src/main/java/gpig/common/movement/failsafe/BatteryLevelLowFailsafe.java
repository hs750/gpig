package gpig.common.movement.failsafe;

import gpig.common.battery.Battery;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.movement.BatteryFailsafeBehaviour;

import java.util.Optional;

public class BatteryLevelLowFailsafe implements BatteryFailsafeBehaviour {
    private Battery battery;
    private Location homeLocation;

    public BatteryLevelLowFailsafe(Battery battery, Location homeLocation) {
        this.battery = battery;
        this.homeLocation = homeLocation;
    }

    @Override
    public boolean isTriggered(Path remainingPath) {
        return battery.percentage() <= 40.0;
    }

    @Override
    public Optional<Path> path(Path remainingPath) {
        if (!isTriggered(remainingPath)) { return Optional.empty(); }

        Path toHome = new Path(homeLocation);
        return Optional.of(toHome);
    }
}
