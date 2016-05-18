package gpig.common.units;

public interface Units {
    static Kilometres kilometres(double n) {
        return new Kilometres(n);
    }

    static Miles miles(double n) {
        return new Miles(n);
    }

    static KMPH kilometresPerHour(double n) {
        return new KMPH(n);
    }

    static MPH milesPerHour(double n) {
        return new MPH(n);
    }
}
