package gpig.common.networking;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import gpig.common.messages.AddToPath;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.DetectionNotification;
import gpig.common.messages.MessageType;
import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.AddToPathHandler;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DeliveryDroneHeartbeatHandler;
import gpig.common.messages.handlers.DeliveryNotificationHandler;
import gpig.common.messages.handlers.DeploymentCentreHeartbeatHandler;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.messages.handlers.DetectionNotificationHandler;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.common.util.Log;

public class MessageReceiver implements ChannelReceiver{
    private ArrayList<AddToPathHandler> addToPathHandlers;
    private ArrayList<DeliveryAssignmentHandler> deliveryAssignmentHandlers;
    private ArrayList<DeliveryDroneHeartbeatHandler> deliveryDroneHeartbeatHandlers;
    private ArrayList<DeliveryNotificationHandler> deliveryNotificationHandlers;
    private ArrayList<DeploymentCentreHeartbeatHandler> deploymentCentreHeartbeatHandlers;
    private ArrayList<DetectionDroneHeartbeatHandler> detectionDroneHeartbeatHandlers;
    private ArrayList<DetectionNotificationHandler> detectionNotificationHandlers;
    private ArrayList<SetPathHandler> setPathHandlers;
    
    public MessageReceiver() {
        addToPathHandlers = new ArrayList<>();
        deliveryAssignmentHandlers = new ArrayList<>();
        deliveryDroneHeartbeatHandlers = new ArrayList<>();
        deliveryNotificationHandlers = new ArrayList<>();
        deploymentCentreHeartbeatHandlers = new ArrayList<>();
        detectionDroneHeartbeatHandlers = new ArrayList<>();
        detectionNotificationHandlers = new ArrayList<>();
        setPathHandlers = new ArrayList<>();
    }
    

    @Override
    public void messageReceived(String message) {
        ObjectMapper mapper = new ObjectMapper();
        String[] splitMessage = message.split(MessageSender.MESSAGE_DELIMITER, 2);
        MessageType type = MessageType.valueOf(splitMessage[0]);
        try {
            switch (type) {
            case ADD_TO_PATH:
                AddToPath atp = mapper.readValue(splitMessage[1], AddToPath.class);
                addToPathHandlers.forEach((handler) -> {
                    handler.handle(atp);
                });
                break;
            case DELIVERY_ASSIGNMENT:
                DeliveryAssignment da = mapper.readValue(splitMessage[1], DeliveryAssignment.class);
                deliveryAssignmentHandlers.forEach((handler) -> {
                    handler.handle(da);
                });
                break;
            case DELIVERY_NOTIFICATION:
                DeliveryNotification dn = mapper.readValue(splitMessage[1], DeliveryNotification.class);
                deliveryNotificationHandlers.forEach((handler) -> {
                    handler.handle(dn);
                });
                break;
            case DETECTION_NOTIFICATION:
                DetectionNotification dtn = mapper.readValue(splitMessage[1], DetectionNotification.class);
                detectionNotificationHandlers.forEach((handler) -> {
                    handler.handle(dtn);
                });
                break;
            case HEARTBEAT_DC:
                DeploymentCentreHeartbeat dch = mapper.readValue(splitMessage[1], DeploymentCentreHeartbeat.class);
                deploymentCentreHeartbeatHandlers.forEach((handler) -> {
                    handler.handle(dch);
                });
                break;
            case HEARTBEAT_DELIVERY:
                DeliveryDroneHeartbeat ddh = mapper.readValue(splitMessage[1], DeliveryDroneHeartbeat.class);
                deliveryDroneHeartbeatHandlers.forEach((handler) -> {
                    handler.handle(ddh);
                });
                break;
            case HEARTBEAT_DETECTION:
                DetectionDroneHeartbeat dtdh = mapper.readValue(splitMessage[1], DetectionDroneHeartbeat.class);
                detectionDroneHeartbeatHandlers.forEach((handler) -> {
                    handler.handle(dtdh);
                });
                break;
            case SET_PATH:
                SetPath sp = mapper.readValue(splitMessage[1], SetPath.class);
                setPathHandlers.forEach((handler) -> {
                    handler.handle(sp);
                });
                break;
            }
        } catch (IOException e) {
            Log.error("Error on receiving message: {}", message);
            e.printStackTrace();
        }
    }
    
    public void addHandler(AddToPathHandler handler){
        addToPathHandlers.add(handler);
    }
    
    public void addHandler(DeliveryAssignmentHandler handler){
        deliveryAssignmentHandlers.add(handler);
    }
    
    public void addHandler(DeliveryDroneHeartbeatHandler handler){
        deliveryDroneHeartbeatHandlers.add(handler);
    }
    
    public void addHandler(DeliveryNotificationHandler handler){
        deliveryNotificationHandlers.add(handler);
    }
    
    public void addHandler(DeploymentCentreHeartbeatHandler handler){
        deploymentCentreHeartbeatHandlers.add(handler);
    }
    
    public void addHandler(DetectionDroneHeartbeatHandler handler){
        detectionDroneHeartbeatHandlers.add(handler);
    }
    
    public void addHandler(DetectionNotificationHandler handler){
        detectionNotificationHandlers.add(handler);
    }
    
    public void addHandler(SetPathHandler handler){
        setPathHandlers.add(handler);
    }
    
    
}
