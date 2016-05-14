package gpig.common.units;

import com.fasterxml.jackson.annotation.JsonValue;

public class MPH implements Comparable<MPH> {
    private final double mph;

    public MPH(double mph) {
        this.mph = mph;
    }

    @JsonValue
    public double value() {
        return mph;
    }

    public KMPH inKPH() {
        return new KMPH(mph * Miles.KM_CONVERSION_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MPH mph1 = (MPH) o;

        return Double.compare(mph1.mph, mph) == 0;

    }

    @Override
    public int compareTo(MPH o) {
        return Double.compare(this.mph, o.mph);
    }

    private MPH() {
        mph = 0.0;
    }
}
