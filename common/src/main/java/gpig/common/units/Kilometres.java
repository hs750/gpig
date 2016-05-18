package gpig.common.units;

import com.fasterxml.jackson.annotation.JsonValue;
import com.javadocmd.simplelatlng.util.LengthUnit;
import gpig.common.exceptions.UnimplementedException;

public class Kilometres implements Comparable<Kilometres> {
    public static final double MILES_CONVERSION_FACTOR = 0.62137119;

    private final double kilometres;

    public Kilometres(double kilometres) {
        this.kilometres = kilometres;
    }

    @JsonValue
    public double value() {
        return kilometres;
    }

    public Kilometres add(Kilometres o) {
        return new Kilometres(kilometres + o.kilometres);
    }

    public Kilometres minus(Kilometres o) {
        return new Kilometres(kilometres - o.kilometres);
    }

    public Miles inMiles() {
        return new Miles(kilometres * MILES_CONVERSION_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Kilometres that = (Kilometres) o;

        return Double.compare(that.kilometres, kilometres) == 0;

    }

    @Override
    public int compareTo(Kilometres o) {
        return Double.compare(this.kilometres, o.kilometres);
    }

    private Kilometres() {
        kilometres = 0.0;
    }
}
