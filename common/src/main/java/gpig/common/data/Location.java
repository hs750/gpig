package gpig.common.data;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * A single point on a map, defined by latitude and longitude.
 */
public class Location {
    final LatLng location;

    public Location(double latitude, double longitude) {
        location = new LatLng(latitude, longitude);
    }

    public double latitude() {
        return location.getLatitude();
    }

    public double longitude() {
        return location.getLongitude();
    }

    /* TODO: this should not return a double - type safe units will
     * make confusion about units compile time errors */
    public double distanceFrom(Location that, LengthUnit unit) {
        return LatLngTool.distance(location, that.location, unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Location that = (Location) o;
        return location.equals(that.location);
    }
}
