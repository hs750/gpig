package gpig.common.units;

import com.fasterxml.jackson.annotation.JsonValue;
import com.javadocmd.simplelatlng.util.LengthUnit;
import gpig.common.exceptions.UnimplementedException;

public class Miles implements Comparable<Miles> {
    public static final double KM_CONVERSION_FACTOR = 1.609344;

    private final double miles;

    public Miles(double miles) {
        this.miles = miles;
    }

    @JsonValue
    public double value() {
        return miles;
    }

    public Kilometres inKilometres() {
        return new Kilometres(miles * KM_CONVERSION_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Miles miles1 = (Miles) o;

        return Double.compare(miles1.miles, miles) == 0;

    }

    @Override
    public int compareTo(Miles o) {
        return Double.compare(this.miles, o.miles);
    }

    private Miles() {
        miles = 0.0;
    }
}
