package gpig.common.data;

import gpig.common.units.Kilometres;

/**
 * The circle which represents the circle covered by a single deployment centre.
 */
public class DeploymentArea {
    public final CircularArea deploymentArea;

    public DeploymentArea(Location center, Kilometres radius) {
        this.deploymentArea = new CircularArea(center, radius);
    }

    private DeploymentArea() {
        deploymentArea = null;
    }
}
