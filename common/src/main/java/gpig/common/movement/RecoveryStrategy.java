package gpig.common.movement;

import gpig.common.data.Location;
import gpig.common.data.Path;

public interface RecoveryStrategy {
    public Path getPath(Location home);
}
