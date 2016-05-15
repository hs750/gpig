package gpig.common.messages.handlers;

import gpig.common.messages.DeliveryNotification;

@FunctionalInterface
public interface DeliveryNotificationHandler {
    public void handle(DeliveryNotification message);
}
