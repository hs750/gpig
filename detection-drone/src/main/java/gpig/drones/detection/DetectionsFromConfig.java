package gpig.drones.detection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gpig.common.data.*;
import gpig.common.util.Log;
import gpig.drones.detection.config.VictimsConfig;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DetectionsFromConfig implements DetectionBehaviour {
    private List<Person> allPeople;
    private Set<UUID> alreadyDetected;

    DetectionsFromConfig(File configFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        VictimsConfig config = mapper.readValue(configFile, VictimsConfig.class);
        this.allPeople = config.getPeople();
        this.alreadyDetected = new HashSet<>();

        Log.info("Loaded %d people", allPeople.size());
    }

    @Override
    public List<Detection> getDetections(Location location) {
        CircularArea detectionArea = new CircularArea(location, Constants.DETECTION_RADIUS);

        List<Person> undetectedVictimsInArea = allPeople.stream()
                .filter(person -> detectionArea.contains(person.location))
                .filter(this::notAlreadyDetected)
                .filter(this::needsSupplies)
                .collect(Collectors.toList());

        // TODO: add image data to the new detections
        List<Detection> detections = undetectedVictimsInArea.stream()
                .map(person -> new Detection(person, null, LocalDateTime.now()))
                .collect(Collectors.toList());

        detections.forEach(d -> alreadyDetected.add(d.person.id));

        return detections;
    }

    private boolean notAlreadyDetected(Person person) {
        return !alreadyDetected.contains(person.id);
    }

    private boolean needsSupplies(Person person) {
        return person.type == Person.PersonType.CIVILIAN;
    }
}
