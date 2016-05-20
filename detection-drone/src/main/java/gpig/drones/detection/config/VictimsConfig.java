package gpig.drones.detection.config;

import gpig.common.data.Person;

import java.util.ArrayList;
import java.util.List;

public class VictimsConfig {
    private List<Person> victims;

    public VictimsConfig() {
        victims = new ArrayList<>();
    }

    public VictimsConfig(List<Person> victims) {
        this.victims = victims;
    }

    public List<Person> getPeople() {
        return victims;
    }
}
