package gpig.common.battery;

import gpig.common.data.Constants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Battery {
    private double batteryPercentage;
    private Duration batteryDuration;
    private LocalDateTime lastUpdateTime;

    public Battery(Duration batteryDuration) {
        this.batteryDuration = batteryDuration;

        this.batteryPercentage = 100.0;
        this.lastUpdateTime = LocalDateTime.now();
    }

    public double percentage() {
        return batteryPercentage;
    }

    public double step() {
        LocalDateTime currentTime = LocalDateTime.now();
        double scalingFactor = Constants.SPEED_SCALING_FACTOR;

        long secondsSinceLastEvent = ChronoUnit.SECONDS.between(lastUpdateTime, currentTime);

        // FIXME: Does in seconds get the total seconds, or just the seconds component?
        double percentageOfBatteryTime = secondsSinceLastEvent / batteryDuration.getSeconds();

        batteryPercentage -= percentageOfBatteryTime;

        if (batteryPercentage < 0.0) {
            batteryPercentage = 0.0;
        }

        return batteryPercentage;
    }

    public boolean isRunDown() {
        return batteryPercentage < 0.01;
    }
}
