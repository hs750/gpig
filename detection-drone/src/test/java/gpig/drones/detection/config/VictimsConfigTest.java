package gpig.drones.detection.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gpig.common.data.Location;
import gpig.common.data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class VictimsConfigTest {
    @Test
    public void createVictimsConfig() throws JsonProcessingException {
        Location loc = new Location(1.0, 2.0);

        List<Person> people = new ArrayList<Person>() {{
            add(new Person(Person.PersonType.CIVILIAN, loc));
            add(new Person(Person.PersonType.OTHER, loc));
            add(new Person(Person.PersonType.RESCUE_TEAM, loc));
            add(new Person(Person.PersonType.CIVILIAN, loc));
            add(new Person(Person.PersonType.CIVILIAN, loc));
        }};

        VictimsConfig config = new VictimsConfig(people);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(mapper.writeValueAsString(config));
    }
}
