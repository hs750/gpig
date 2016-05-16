package gpig.common.movement;

import java.util.Arrays;
import java.util.List;

import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;

public class ImmediateReturn implements RecoveryStrategy {

    @Override
    public Path getPath(Location home) {
        Waypoint w = new Waypoint(home);
        List<Waypoint> wps = Arrays.asList(w);
        Path p = new Path(wps);
        return p;
    }

}
