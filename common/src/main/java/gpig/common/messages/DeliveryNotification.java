package gpig.common.messages;

import java.time.LocalDateTime;

import gpig.common.data.Assignment;

/**
 * A command representing the successful delivery of supplies to a detected person
 */
public class DeliveryNotification {
    public final Assignment assignment;
    public final LocalDateTime timestamp;
    
    public DeliveryNotification(LocalDateTime timestamp, Assignment assignment) {
        this.timestamp = timestamp;
        this.assignment = assignment;
    }

    private DeliveryNotification() {
        timestamp = null;
        assignment = null;
    }
}
