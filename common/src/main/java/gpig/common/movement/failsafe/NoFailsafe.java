package gpig.common.movement.failsafe;

import gpig.common.data.Path;
import gpig.common.movement.BatteryFailsafeBehaviour;

import java.util.Optional;

public class NoFailsafe implements BatteryFailsafeBehaviour {

    @Override
    public boolean isTriggered(Path remainingPath) {
        return false;
    }

    @Override
    public Optional<Path> path(Path remainingPath) {
        return Optional.empty();
    }
}
