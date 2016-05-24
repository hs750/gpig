package gpig.common.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import gpig.common.units.Kilometres;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static gpig.common.units.Units.kilometres;

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
    private Location initialLocation;

    public Path(List<Waypoint> waypoints, Location initialLocation) {
        this.waypoints = waypoints;
        this.initialLocation = initialLocation;
    }

    public Path(List<Waypoint> waypoints) {
        this(waypoints, null);
    }

    public Path(Waypoint... waypoints) {
        this.waypoints = Arrays.asList(waypoints);
    }

    public Path(Location... locations) {
        this.waypoints = Arrays.asList(locations).stream()
                .map(Waypoint::new)
                .collect(Collectors.toList());
    }

    public Waypoint get(int i) {
        return waypoints.get(i);
    }

    public Location getInitialLocation() {
        return initialLocation;
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

    public Path subPathUntilEnd(int startIndex) {
        return new Path(waypoints.subList(startIndex, waypoints.size()-1));
    }

    public Path subPath(int startIndex, int endIndex) {
        return new Path(waypoints.subList(startIndex, endIndex));
    }

    public Kilometres totalDistance() {
        List<Kilometres> edgeLengths = IntStream.range(1, waypoints.size())
                .mapToObj(i -> new Line(waypoints.get(i-1), waypoints.get(i)))
                .map(Line::length)
                .collect(Collectors.toList());

        Kilometres distance = kilometres(0);
        for (Kilometres edgeLength : edgeLengths) {
            distance = distance.add(edgeLength);
        }

        return distance;
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

    private class Line {
        private final Location start;
        private final Location end;

        public Line(Waypoint start, Waypoint end) {
            this.start = start.location;
            this.end = end.location;
        }

        public Kilometres length() {
            return start.distanceFrom(end);
        }
    }
}
