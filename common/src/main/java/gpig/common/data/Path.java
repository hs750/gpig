package gpig.common.data;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("waypoints")
    private List<Waypoint> waypoints;

    public Path(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Waypoint get(int i) {
        return waypoints.get(i);
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
    }

    public boolean contains(Waypoint waypoint) {
        return waypoints.contains(waypoint);
    }

    public boolean contains(Location location) {
        Optional<Waypoint> loc = waypoints.stream()
                .filter(w -> w.location.equals(location))
                .findFirst();

        return loc.isPresent();
    }

    public int length() {
        return waypoints.size();
    }

    @Override
    public Iterator<Waypoint> iterator() {
        return waypoints.iterator();
    }

    /** Represents a single waypoint in a path */
    public static class Waypoint {
        public final Location location;

        public Waypoint(Location location) {
            this.location = location;
        }

        private Waypoint() {
            location = null;
        }
    }

    private Path() {
        waypoints = null;
    }
}
