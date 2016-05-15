package gpig.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private static final Logger log = Logger.getAnonymousLogger();

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tT [%4$s] %5$s%6$s%n");
    }

    public static void debug(String s) {
        log.fine(s);
    }

    public static void debug(String formatStr, Object... objs) {
        debug(String.format(formatStr, objs));
    }

    public static void info(String s) {
        log.info(s);
    }

    public static void info(String formatStr, Object... objs) {
        info(String.format(formatStr, objs));
    }

    public static void warn(String s) {
        log.warning(s);
    }

    public static void warn(String formatStr, Object... objs) {
        warn(String.format(formatStr, objs));
    }

    public static void error(String s) {
        log.severe(s);
    }

    public static void error(String formatStr, Object... objs) {
        error(String.format(formatStr, objs));
    }

    public static void setLevel(Level level) {
        log.setLevel(level);
    }
}
