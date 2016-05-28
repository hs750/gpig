package gpig.drones.detection;

import java.util.List;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.messages.DetectionNotification;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

public class Detector {
    // Detect every 10 meters along the path
    private final static Kilometres detectionInterval = new Kilometres(0.01);
    private DetectionBehaviour detBehaviour;
    private MessageSender messenger;

    public Detector(DetectionBehaviour detectionBehaviour, MessageSender messenger) {
        this.detBehaviour = detectionBehaviour;
        this.messenger = messenger;
    }

    public void detect(Location start, Location end) {
        Kilometres distance = start.distanceFrom(end);
        double bearing = start.bearingOf(end);
        Kilometres detectDistance = new Kilometres(0);

        detect(detBehaviour.getDetections(start));
        detectDistance = detectDistance.add(detectionInterval);

        while (detectDistance.value() <= distance.value()) {
            Location l = start.locationAt(bearing, detectDistance);
            detect(detBehaviour.getDetections(l));
            detectDistance = detectDistance.add(detectionInterval);
        }

        detect(detBehaviour.getDetections(end));
    }

    private void detect(List<Detection> detections) {
        detections.forEach(d -> detect(d));
    }

    private void detect(Detection detection) {
        messenger.send(new DetectionNotification(detection));
        Log.info("Detected %s at %s", detection.person.type.toString(), detection.person.location.toString());
    }
}
