package gpig.common.movement.failsafe;

import gpig.common.battery.Battery;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.movement.BatteryFailsafeBehaviour;
import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;

import java.time.Duration;
import java.util.Optional;

public class CalculatedRemainingBatteryFailsafe implements BatteryFailsafeBehaviour {
    private static final double ESTIMATE_TOLERANCE = 1.10;

    private Battery battery;
    private Location homeLocation;
    private KMPH vehicleSpeed;

    public CalculatedRemainingBatteryFailsafe(Battery battery, Location homeLocation, KMPH vehicleSpeed) {
        this.battery = battery;
        this.homeLocation = homeLocation;
        this.vehicleSpeed = vehicleSpeed;
    }

    @Override
    public boolean isTriggered(Path remainingPath) {
        Kilometres remainingDistance = remainingPath.totalDistance();

        // This is in hours - so to convert to seconds, multiply by 360
        double secondsToCoverRemainingDistance = (remainingDistance.value() / vehicleSpeed.value()) * 60 * 60;
        double remainingBatterySeconds = battery.estimatedRemainingSeconds();

        double secondsToCoverDistancePlusTolerance = secondsToCoverRemainingDistance * ESTIMATE_TOLERANCE;

        return secondsToCoverDistancePlusTolerance < remainingBatterySeconds;
    }

    @Override
    public Optional<Path> path(Path remainingPath) {
        if (!isTriggered(remainingPath)) { return Optional.empty(); }

        Path toHome = new Path(homeLocation);
        return Optional.of(toHome);
    }
}
