package gpig.common.data;

import com.javadocmd.simplelatlng.window.CircularWindow;

/**
 * The circle which represents the circle covered by a single deployment centre.
 */
public class DeploymentArea {

    public final Location centre;
    private final CircularWindow area;

    public DeploymentArea(Location centre) {
        this.centre = centre;
        this.area = new CircularWindow(centre.location, Constants.DEPLOYMENT_RADIUS);
    }

    public boolean contains(Location that) {
        return area.contains(that.location);
    }

    public boolean overlaps(DeploymentArea that) {
        return area.overlaps(that.area);
    }
}
