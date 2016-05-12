package gpig.common.data;

import gpig.common.exceptions.UnimplementedException;

/**
 * The circle which represents the circle covered by a single deployment centre.
 */
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
