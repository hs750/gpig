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
        detections.add(message.detection);
    }
}
