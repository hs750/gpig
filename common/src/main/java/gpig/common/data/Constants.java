package gpig.common.data;

import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;

import static gpig.common.units.Units.*;

public class Constants {
    public final static KMPH DETECTION_DRONE_SPEED = kilometresPerHour(80);
    public final static KMPH DELIVERY_DRONE_SPEED = kilometresPerHour(20);

    public final static Kilometres DEPLOYMENT_SEARCH_RADIUS = kilometres(10.0);
    public final static Kilometres DEPLOYMENT_DELIVERY_RADIUS = kilometres(10.0);

    public final static Kilometres DETECTION_RADIUS = kilometres(2.5);

    public final static double SPEED_SCALING_FACTOR = 1.0;
    
    public final static int DRONE_HEARTBEAT_INTERVAL = 1000; // milliseconds
    public final static int DRONE_HEARTBEAT_TIMEOUT = 5000; // milliseconds
    
}
