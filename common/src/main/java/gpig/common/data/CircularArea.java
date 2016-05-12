package gpig.common.data;

import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.CircularWindow;

/**
 * Represents a generic circular area defined by a lat-long centre
 * and a radius. This can be used for representing various parts of
 * the solution e.g. a DC deployment area, or a drone's detection
 * radius.
 */
public class CircularArea {
    public final Location centre;
    private final CircularWindow area;

    /* TODO: this should also be using type-safe units */
    public CircularArea(Location centre, double radius, LengthUnit unit) {
        this.centre = centre;
        this.area = new CircularWindow(centre.location, radius, unit);
    }

    public boolean contains(Location that) {
        return area.contains(that.location);
    }

    public boolean overlaps(CircularArea that) {
        return area.overlaps(that.area);
    }

}
