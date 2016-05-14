package gpig.common.units;

import com.fasterxml.jackson.annotation.JsonValue;

public class KMPH implements Comparable<KMPH> {
    private final double kmph;

    public KMPH(double kmph) {
        this.kmph = kmph;
    }

    @JsonValue
    public double value() {
        return kmph;
    }

    public MPH inMPH() {
        return new MPH(kmph * Kilometres.MILES_CONVERSION_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        KMPH kph1 = (KMPH) o;

        return Double.compare(kph1.kmph, kmph) == 0;

    }

    @Override
    public int compareTo(KMPH o) {
        return Double.compare(this.kmph, o.kmph);
    }

    private KMPH() {
        kmph = 0.0;
    }
}
