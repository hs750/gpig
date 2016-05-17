package gpig.common.data;

import gpig.common.units.KMPH;
import gpig.common.units.Kilometres;

import static gpig.common.units.Units.*;

public class Constants {
    public final static KMPH DRONE_SPEED = kilometresPerHour(1.2);

    public final static Kilometres DEPLOYMENT_SEARCH_RADIUS = kilometres(10.0);
    public final static Kilometres DEPLOYMENT_DELIVERY_RADIUS = kilometres(10.0);
}
