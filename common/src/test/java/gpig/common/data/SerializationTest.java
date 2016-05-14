package gpig.common.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static gpig.common.units.Units.kilometres;

public class SerializationTest {

    private static Location locat = new Location(0.0, 0.0);
    private static Person detectedPerson = new Person(Person.PersonType.CIVILIAN);
    private static Detection detection = new Detection(locat, detectedPerson);
    private static UUID deploymentCentreUUID = UUID.randomUUID();
    private static CircularArea area = new CircularArea(locat, kilometres(2));
    private static List<Path.Waypoint> waypoints = new ArrayList<Path.Waypoint>() {{
        add(new Path.Waypoint(locat));
        add(new Path.Waypoint(locat));
        add(new Path.Waypoint(locat));
    }};

    @Test
    public void serializationTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String s = mapper.writeValueAsString(new SerializationTestClass());
        System.out.println(s);
        SerializationTestClass out = mapper.readValue(s, SerializationTestClass.class);
    }

    public static class SerializationTestClass {
        @JsonProperty("actorType") public ActorType actorType = ActorType.C2;
        @JsonProperty("assignment") public Assignment assignment = new Assignment(new Detection(locat, detectedPerson), deploymentCentreUUID);
        @JsonProperty("area") public CircularArea carea = area;
        @JsonProperty("deploymentArea") public DeploymentArea deploymentArea = new DeploymentArea(locat);
        @JsonProperty("detection") public Detection dec = new Detection(locat, detectedPerson);
        @JsonProperty("location") public Location loc = locat;
        @JsonProperty("path") public Path path = new Path(waypoints);
        @JsonProperty("waypoint") public Path.Waypoint waypoint = new Path.Waypoint(locat);
        @JsonProperty("person") public Person person = detectedPerson;

        public SerializationTestClass() {
        }

        @JsonCreator
        public SerializationTestClass(
                @JsonProperty("actorType") ActorType actorType,
                @JsonProperty("assignment") Assignment assignment,
                @JsonProperty("area") CircularArea carea,
                @JsonProperty("deploymentArea") DeploymentArea deploymentArea,
                @JsonProperty("detection") Detection dec,
                @JsonProperty("location") Location loc,
                @JsonProperty("path") Path path,
                @JsonProperty("waypoint") Path.Waypoint waypoint,
                @JsonProperty("person") Person person) {
            this.actorType = actorType;
            this.assignment = assignment;
            this.carea = carea;
            this.deploymentArea = deploymentArea;
            this.dec = dec;
            this.loc = loc;
            this.path = path;
            this.waypoint = waypoint;
            this.person = person;
        }
    }

}
