package gpig.common.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gpig.common.messages.AddToPath;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.DetectionNotification;
import gpig.common.messages.MessageType;
import gpig.common.messages.SetPath;

public class MessageSender {
    protected static final String MESSAGE_DELIMITER = "~";
    private CommunicationChannel channel;

    public MessageSender(CommunicationChannel channel) {
        this.channel = channel;
    }

    public boolean send(AddToPath message) {
        return send(message, MessageType.ADD_TO_PATH);
    }

    public boolean send(DeliveryAssignment message) {
        return send(message, MessageType.DELIVERY_ASSIGNMENT);
    }

    public boolean send(DeliveryDroneHeartbeat message) {
        return send(message, MessageType.HEARTBEAT_DELIVERY);
    }

    public boolean send(DeliveryNotification message) {
        return send(message, MessageType.DELIVERY_NOTIFICATION);
    }

    public boolean send(DeploymentCentreHeartbeat message) {
        return send(message, MessageType.HEARTBEAT_DC);
    }

    public boolean send(DetectionDroneHeartbeat message) {
        return send(message, MessageType.HEARTBEAT_DETECTION);
    }

    public boolean send(DetectionNotification message) {
        return send(message, MessageType.DETECTION_NOTIFICATION);
    }

    public boolean send(SetPath message) {
        return send(message, MessageType.SET_PATH);
    }

    private boolean send(Object message, MessageType type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String typeString = type.name();
        try {
            String messageString = mapper.writeValueAsString(message);
            String fullMessage = typeString + MESSAGE_DELIMITER + messageString;
            channel.sendMessage(fullMessage);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }

    }

}
