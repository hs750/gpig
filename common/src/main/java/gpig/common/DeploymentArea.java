package gpig.common;

import gpig.common.exceptions.UnimplementedException;

public class DeploymentArea {
    public final static double DEPLOYMENT_RADIUS = 17.0; /* km */

    public final Location location;

    public DeploymentArea(Location location) {
        this.location = location;
    }

    public boolean contains(Location location) {
        // TODO
        throw new UnimplementedException();
    }

}
