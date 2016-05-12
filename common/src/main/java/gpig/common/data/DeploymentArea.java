package gpig.common.data;

import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * The circle which represents the circle covered by a single deployment centre.
 */
public class DeploymentArea {
    public final CircularArea deploymentArea;

    public DeploymentArea(Location center) {
        this.deploymentArea = new CircularArea(center, Constants.DEPLOYMENT_RADIUS, LengthUnit.KILOMETER);
    }
}
