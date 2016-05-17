package gpig.dc;

import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.DetectionNotification;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.messages.handlers.DeliveryNotificationHandler;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.messages.handlers.DetectionNotificationHandler;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

public class DroneMessageForwarder implements DetectionDroneHeartbeatHandler, DeliveryDroneHeartbeatHandler, DeliveryNotificationHandler, DetectionNotificationHandler{
    private MessageSender c2messenger;

    public DroneMessageForwarder(MessageSender c2messenger, MessageReceiver detectionReceiver, MessageReceiver deliveryReceiver) {
        this.c2messenger = c2messenger;
        
        // Forward messages from detection drones
        detectionReceiver.addHandler((DetectionDroneHeartbeatHandler) this);
        detectionReceiver.addHandler((DetectionNotificationHandler) this);
        // Forward messages from delivery drones
        deliveryReceiver.addHandler((DeliveryDroneHeartbeatHandler) this);
        deliveryReceiver.addHandler((DeliveryNotificationHandler) this);
    }

    @Override
    public void handle(DeliveryDroneHeartbeat message) {
        c2messenger.send(message);
        
    }

    @Override
    public void handle(DetectionDroneHeartbeat message) {
        c2messenger.send(message);
        
    }

    @Override
    public void handle(DetectionNotification message) {
        c2messenger.send(message);
        
    }

    @Override
    public void handle(DeliveryNotification message) {
        c2messenger.send(message);
        
    }
}
