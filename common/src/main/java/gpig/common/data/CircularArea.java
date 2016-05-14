package gpig.common.data;

import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.CircularWindow;
import gpig.common.units.Kilometres;

/**
 * Represents a generic circular area defined by a lat-long centre
 * and a radius. This can be used for representing various parts of
 * the solution e.g. a DC deployment area, or a drone's detection
 * radius.
 */
public class CircularArea {
    public final Location centre;
    public final Kilometres radius;
    private final CircularWindow area;

    public CircularArea(Location centre, Kilometres radius) {
        this.centre = centre;
        this.radius = radius;
        this.area = new CircularWindow(centre.location, radius.value(), LengthUnit.KILOMETER);
    }

    public boolean contains(Location that) {
        return area.contains(that.location);
    }

    public boolean overlaps(CircularArea that) {
        return area.overlaps(that.area);
    }

    private CircularArea() {
        centre = null;
        radius = null;
        area = null;
    }
}
