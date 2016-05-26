package gpig.common.movement;

import static gpig.common.units.Units.kilometres;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import gpig.common.data.Constants;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.messages.heartbeater.LocationProvider;
import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public class WaypointBasedMovement implements MovementBehaviour, LocationProvider {
    private Location currentLocation;
    private InTraversalPath path;
    private LocalDateTime lastUpdateTime;
    private KMPH vehicleSpeed;
    private BatteryFailsafeBehaviour failsafeBehaviour;

    public WaypointBasedMovement(Location initialLocation, KMPH speed, BatteryFailsafeBehaviour failsafeBehaviour) {
        this.currentLocation = initialLocation;
        this.vehicleSpeed = speed;
        this.path = null;
        this.lastUpdateTime = null;
        this.failsafeBehaviour = failsafeBehaviour;
    }

    public void setPath(Path newPath) {
        path = new InTraversalPath(newPath);

        // Teleport to destination if the teleport location exists
        path.teleportLocation().ifPresent(loc -> currentLocation = loc);
    }

    public Location currentLocation() {
        return currentLocation;
    }

    public boolean isMoving() {
        return path != null && !path.isAtEnd();
    }

    public Location step() {
        if (!isMoving()) {
            // No target, so the drone stays where it is
            return currentLocation();
        }

        if (lastUpdateTime == null) {
            lastUpdateTime = LocalDateTime.now();
            return currentLocation();
        }

        // Switch over to the failsafe path if the battery failsafe behaviour is triggered
        if (failsafeBehaviour.isTriggered(path.remainingPath())) {
            Path failsafePath = failsafeBehaviour.path(path.remainingPath())
                    .orElseThrow(() -> new IllegalStateException("Failsafe behaviour was triggered but did not provide a failsafe path"));
            setPath(failsafePath);
        }

        double speedScalingFactor = Constants.SPEED_SCALING_FACTOR;

        // Find the amount of time which elapsed since the drone's location was
        // last calculated
        LocalDateTime currentUpdateTime = LocalDateTime.now();
        long millisecondsSinceLastEvent = ChronoUnit.MILLIS.between(lastUpdateTime, currentUpdateTime);
        double secondsSinceLastEvent = millisecondsSinceLastEvent / 100.0;
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
                Log.info("Path end reached");
                return new TravelStatus(newLocation, kilometres(0.0), Status.FINISHED);
            } else {
                return new TravelStatus(newLocation, remainingDistance, Status.REACHED_WAYPOINT);
            }
        }
    }


    private class InTraversalPath {
        private Path path;
        private int currentWaypoint;
        private Optional<Location> initialLocation;

        public InTraversalPath(Path path) {
            this.path = path;
            this.currentWaypoint = 0;
            this.initialLocation = Optional.ofNullable(path.getInitialLocation());
        }

        public Path.Waypoint currentDestination() {
            return path.get(currentWaypoint);
        }

        public Path.Waypoint advance() {
            currentWaypoint += 1;
            return currentDestination();
        }

        public Optional<Location> teleportLocation() {
            if (initialLocation.isPresent()) {
                Optional<Location> initLocation = initialLocation;
                initialLocation = Optional.empty();
                return initLocation;
            }

            return Optional.empty();
        }

        public boolean isAtEnd() {
            return currentWaypoint >= path.length();
        }

        public Path remainingPath() {
            return path.subPathUntilEnd(currentWaypoint);
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
