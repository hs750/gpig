package gpig.drones.detection;

import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.util.Log;

public class DetectionPathHandler implements SetPathHandler{
    private final DetectionDrone det;

    public DetectionPathHandler(DetectionDrone det) {
        this.det = det;
    }

    @Override
    public void handle(SetPath message) {
        if (message.isFor(det.thisDrone)) {
            Log.info("Received path");
            det.dcLocation = message.path.getInitialLocation();
            det.movementBehaviour.setPath(message.path);
            det.movementBehaviour.step();
            Log.info("Detection Drone Deployment made to location %s", message.path.get(0).location);
            det.setDeployed();
        }
    }
}
