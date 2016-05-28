package gpig.common.data;

import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import static gpig.common.units.Units.*;

public class Constants {
    public final static KMPH DETECTION_DRONE_SPEED = kilometresPerHour(80);
    public final static KMPH DELIVERY_DRONE_SPEED = kilometresPerHour(20);
    public final static KMPH MDC_SPEED = kilometresPerHour(350);

    public final static Kilometres DEPLOYMENT_SEARCH_RADIUS = kilometres(10.0);
    public final static Kilometres DEPLOYMENT_DELIVERY_RADIUS = kilometres(10.0);

    public final static Kilometres DETECTION_RADIUS = kilometres(2.5);

    public final static Duration AERIAL_VEHICLE_BATTERY_DURATION = Duration.of(20, ChronoUnit.MINUTES);

    public static double SPEED_SCALING_FACTOR = 10.0;
    
    public final static int DRONE_HEARTBEAT_INTERVAL = 1000; // milliseconds
    public final static int DRONE_HEARTBEAT_TIMEOUT = 5000; // milliseconds
    
    public final static File EXTERNAL_OUTPUT_LOCATION = new File("Group4.xml");
    public final static int EXTERNAL_OUTPUT_RATE = 1000; // milliseconds
    public final static int EXTERNAL_INPUT_RATE = 1000; // milliseconds
    
    public final static String DETECTION_IMAGE_DIR = ".." + File.separator + "c2" + File.separator + "DetectionImages";
    
}
