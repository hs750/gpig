package gpig.common;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Path implements Iterable<Waypoint> {
    private List<Waypoint> path;

    public Path(List<Waypoint> path) {
        this.path = path;
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
}
