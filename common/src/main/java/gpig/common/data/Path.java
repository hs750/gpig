package gpig.common.data;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a list of waypoints forming a path which should be traversed
 * by a vehicle (i.e. an MDC or a drone).
 *
 * Paths are mutable since it should be possible to add points to the end of
 * of the path. However, if the entire path of the vehicle should change
 * (e.g. on recall), a new path should be sent using the `SetPath` command.
 */
public class Path implements Iterable<Path.Waypoint> {
    private List<Waypoint> path;

    public Path(List<Waypoint> path) {
        this.path = path;
    }

    public void addWaypoint(Waypoint waypoint) {
        path.add(waypoint);
    }

    public boolean contains(Waypoint waypoint) {
        return path.contains(waypoint);
    }

    public boolean contains(Location location) {
        Optional<Waypoint> loc = path.stream()
                .filter(w -> w.location.equals(location))
                .findFirst();

        return loc.isPresent();
    }

    @Override
    public Iterator<Waypoint> iterator() {
        return path.iterator();
    }

    /** Represents a single waypoint in a path */
    public static class Waypoint {
        public final Location location;

        public Waypoint(Location location) {
            this.location = location;
        }
    }
}
