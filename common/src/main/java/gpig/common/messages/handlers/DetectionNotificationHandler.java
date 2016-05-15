package gpig.common.messages.handlers;

import gpig.common.messages.DetectionNotification;

@FunctionalInterface
public interface DetectionNotificationHandler {
    public void handle(DetectionNotification message);
}
