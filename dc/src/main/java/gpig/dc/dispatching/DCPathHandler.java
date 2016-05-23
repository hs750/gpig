package gpig.dc.dispatching;

import gpig.common.data.Location;
import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.dc.DeploymentCentre;

public class DCPathHandler implements SetPathHandler{
    private final DeploymentCentre dc;

    public DCPathHandler(DeploymentCentre dc) {
        this.dc = dc;
    }

    @Override
    public void handle(SetPath message) {
        if (message.assignee.equals(dc.id)) {
            dc.location = message.path.get(message.path.length() - 1).location;
        }
    }
}
