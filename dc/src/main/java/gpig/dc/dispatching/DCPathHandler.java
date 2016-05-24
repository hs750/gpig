package gpig.dc.dispatching;

import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.common.util.Log;
import gpig.dc.DeploymentCentre;

public class DCPathHandler implements SetPathHandler{
    private final DeploymentCentre dc;

    public DCPathHandler(DeploymentCentre dc) {
        this.dc = dc;
    }

    @Override
    public void handle(SetPath message) {
        if (message.isFor(dc.id)) {
            dc.movementBehaviour.setPath(message.path);
            Log.info("DC Deployment made to location %s", message.path.get(0).location);
            dc.setDeployed();
        }
    }
}
