package gpig.c2.data.handlers;

import gpig.common.data.Detection;
import gpig.common.messages.DetectionNotification;
import gpig.common.messages.handlers.DetectionNotificationHandler;

import java.util.List;

public class C2DetectionNotificationHandler implements DetectionNotificationHandler {
    private final List<Detection> detections;

    public C2DetectionNotificationHandler(List<Detection> detections) {
        this.detections = detections;
    }

    @Override
    public void handle(DetectionNotification message) {

        // Only add detections that don't already exist (based on detection
        // Locations matching exactly)
        if (!detectionExists(message.detection)) {
            detections.add(message.detection);
        }

    }

    protected boolean detectionExists(Detection det) {
        return detections.stream().filter(d -> d.person.location.equals(det.person.location)).findFirst().isPresent();
    }
}
