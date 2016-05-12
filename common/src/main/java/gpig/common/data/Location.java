package gpig.common.data;

/**
 * A single point on a map, defined by latitude and longitude.
 */
public class Location {
    public final float latitude;
    public final float longitude;

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Location that = (Location) o;

        return Float.compare(this.latitude, that.latitude) == 0 && Float.compare(this.longitude, that.longitude) == 0;

    }
}
