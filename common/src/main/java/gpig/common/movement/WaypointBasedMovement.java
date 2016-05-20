package gpig.common.movement;

import com.javadocmd.simplelatlng.LatLngTool;
import gpig.common.data.Constants;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static gpig.common.units.Units.kilometres;

public class WaypointBasedMovement implements MovementBehaviour {
    private Location currentLocation;
    private InTraversalPath path;
    private LocalDateTime lastUpdateTime;
    private KMPH vehicleSpeed;

    public WaypointBasedMovement(Location initialLocation, KMPH speed) {
        this.currentLocation = initialLocation;
        this.vehicleSpeed = speed;
        this.path = null;
        this.lastUpdateTime = LocalDateTime.now();
    }

    public void setPath(Path newPath) {
        path = new InTraversalPath(newPath);
    }

    public Location currentLocation() {
        return currentLocation;
    }

    public Location step() {
        if (path == null) {
            // No target, so the drone stays where it is
            return currentLocation;
        }

        double speedScalingFactor = Constants.SPEED_SCALING_FACTOR;

        // Find the amount of time which elapsed since the drone's location was
        // last calculated
        LocalDateTime currentUpdateTime = LocalDateTime.now();
        long secondsSinceLastEvent = ChronoUnit.SECONDS.between(lastUpdateTime, currentUpdateTime);
        double hoursSinceLastEvent = ((secondsSinceLastEvent / 60.0) / 60.0);

        // Calculate the distance travelled by the drone since the last update
        Kilometres totalDistanceTravelled = kilometres(vehicleSpeed.value() * speedScalingFactor * hoursSinceLastEvent);

        // A TravelStatus is a way of handling the possibility that a drone travels
        // over multiple waypoints in a single step.
        // If a drone is travelling towards a waypoint and reaches it, but it is
        // calculated to have travelled further, then it must recalculate its bearing
        // to ensure it does not continue travelling in the same destination, overshooting
        // the waypoint.
        TravelStatus r = new TravelStatus(currentLocation, totalDistanceTravelled, Status.TRAVELLING);

        while (r.status != Status.FINISHED) {
            r = goTowardsTarget(r);
        }

        assert r.remainingDistance == kilometres(0.0);

        currentLocation = r.newLocation;
        lastUpdateTime = currentUpdateTime;

        return currentLocation;
    }

    private TravelStatus goTowardsTarget(TravelStatus last) {
        Path.Waypoint targetWaypoint = path.currentDestination();
        Location targetLocation = targetWaypoint.location;

        Kilometres distanceToTarget = last.newLocation.distanceFrom(targetLocation);
        double bearingToTarget = currentLocation.bearingOf(targetLocation);

        if (last.remainingDistance.value() <= distanceToTarget.value()) {

            // Trivial case: The vehicle has exhausted its remaining
            // travellable distance without reaching another waypoint

            // The vehicle travels the remaining distance along the bearing to its target
            Location newLocation = last.newLocation.locationAt(bearingToTarget, last.remainingDistance);
            Kilometres remainingDistance = kilometres(0.0);
            return new TravelStatus(newLocation, remainingDistance, Status.FINISHED);

        } else {
            // Non-trivial case: The vehicle reaches its waypoint with distance
            // to spare. The vehicle must set its next waypoint and travel in a
            // new direction.

            Location newLocation = targetLocation;
            Kilometres remainingDistance = last.remainingDistance.minus(distanceToTarget);

            // The status depends on whether the waypoint reached was the final destination
            // If so, the remaining distance is disregarded since no more travelling will occur
            path.advance();

            if (path.isAtEnd()) {
                return new TravelStatus(newLocation, kilometres(0.0), Status.FINISHED);
            } else {
                return new TravelStatus(newLocation, remainingDistance, Status.REACHED_WAYPOINT);
            }
        }
    }


    private class InTraversalPath {
        private Path path;
        private int currentWaypoint;

        public InTraversalPath(Path path) {
            this.path = path;
            this.currentWaypoint = 0;
        }

        public Path.Waypoint currentDestination() {
            return path.get(currentWaypoint);
        }

        public Path.Waypoint advance() {
            currentWaypoint += 1;
            return currentDestination();
        }

        public boolean isAtEnd() {
            return currentWaypoint >= path.length();
        }
    }

    private class TravelStatus {
        public final Location newLocation;
        public final Kilometres remainingDistance;
        public final Status status;

        public TravelStatus(Location newLocation, Kilometres remainingDistance, Status status) {
            this.newLocation = newLocation;
            this.remainingDistance = remainingDistance;
            this.status = status;
        }
    }

    private enum Status { TRAVELLING, REACHED_WAYPOINT, FINISHED }
}
