package gpig.drones.detection.config;

import gpig.common.data.Person;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VictimsConfig {
    public List<ConfigVictim> victims;

    public VictimsConfig() {
        victims = new ArrayList<>();
    }

    public VictimsConfig(List<Person> victims) {
        this();
        victims.forEach(p -> {
            this.victims.add(new ConfigVictim(p.location, p.type));
        });
    }

    @JsonIgnore
    public List<Person> getPeople() {
        ArrayList<Person> people = new ArrayList<>();
        victims.forEach(v -> {
            people.add(new Person(v.type, v.location));
        });
        return people;
    }
}
