package gpig.drones.detection.config;

import gpig.common.data.Location;
import gpig.common.data.Person.PersonType;

public class ConfigVictim {
    public final Location location;
    public final PersonType type;
    
    public ConfigVictim(Location l, PersonType t){
        location = l;
        type = t;
    }
    
    private ConfigVictim() {
        location = null;
        type = null;
    }
}
