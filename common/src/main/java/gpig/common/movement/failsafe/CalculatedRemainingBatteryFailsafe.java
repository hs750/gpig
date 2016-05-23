package gpig.common.movement.failsafe;

import gpig.common.battery.Battery;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.movement.BatteryFailsafeBehaviour;

import java.util.Optional;

public class CalculatedRemainingBatteryFailsafe implements BatteryFailsafeBehaviour {
    private Battery battery;
    private Location homeLocation;

    public CalculatedRemainingBatteryFailsafe(Battery battery, Location homeLocation) {
        this.battery = battery;
        this.homeLocation = homeLocation;
    }

    @Override
    public boolean isTriggered() {
        // TODO: calculate remaining battery required for path
        return false;
    }

    @Override
    public Optional<Path> path() {
        if (!isTriggered()) { return Optional.empty(); }

        Path toHome = new Path(homeLocation);
        return Optional.of(toHome);
    }
}
