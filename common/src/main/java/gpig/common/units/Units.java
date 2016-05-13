package gpig.common.units;

public class Units {
    public static Kilometres kilometres(double value) {
        return new Kilometres(value);
    }

    public static Miles miles(double value) {
        return new Miles(value);
    }

    public static KMPH kilometresPerHour(double value) {
        return new KMPH(value);
    }

    public static MPH milesPerHour(double value) {
        return new MPH(value);
    }
}
