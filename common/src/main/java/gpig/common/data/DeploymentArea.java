package gpig.common.data;

/**
 * The circle which represents the circle covered by a single deployment centre.
 */
public class DeploymentArea {
    public final CircularArea deploymentArea;

    public DeploymentArea(Location center) {
        this.deploymentArea = new CircularArea(center, Constants.DEPLOYMENT_RADIUS);
    }

    private DeploymentArea() {
        deploymentArea = null;
    }
}
