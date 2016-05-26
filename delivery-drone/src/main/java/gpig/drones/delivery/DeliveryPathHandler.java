package gpig.drones.delivery;

import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.util.Log;

public class DeliveryPathHandler implements SetPathHandler{
    private final DeliveryDrone del;

    public DeliveryPathHandler(DeliveryDrone del) {
        this.del = del;
    }

    @Override
    public void handle(SetPath message) {
        if (message.isFor(del.thisDrone)) {
            Log.info("Received path");
            del.dcLocation = message.path.getInitialLocation();
            del.movementBehaviour.setPath(message.path);
            del.movementBehaviour.step();
            Log.info("Delivery Drone Deployment made to location %s", message.path.get(0).location);
            del.setDeployed();
        }
    }
}
