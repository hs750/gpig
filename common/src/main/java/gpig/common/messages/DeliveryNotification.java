package gpig.common.messages;

import java.time.LocalDateTime;
import java.util.UUID;

import gpig.common.data.Assignment;

/**
 * A command representing the successful delivery of supplies to a detected person
 */
public class DeliveryNotification {
    public final Assignment assignment;
    public final LocalDateTime timestamp;
    public final UUID deliveryDrone;
    
    public DeliveryNotification(LocalDateTime timestamp, Assignment assignment) {
        this.timestamp = timestamp;
        this.assignment = assignment;
        deliveryDrone = null;
    }
    
    public DeliveryNotification(LocalDateTime timestamp, UUID drone){
        this.timestamp = timestamp;
        this.deliveryDrone = drone;
        assignment = null;
    }

    private DeliveryNotification() {
        timestamp = null;
        assignment = null;
        deliveryDrone = null;
    }
}
