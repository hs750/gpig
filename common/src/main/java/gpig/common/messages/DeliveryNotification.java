package gpig.common.messages;

import java.util.Date;

import gpig.common.data.Assignment;

/**
 * A command representing the successful delivery of supplies to a detected person
 */
public class DeliveryNotification {
    public final Assignment assignment;
    public final Date timestamp;
    
    public DeliveryNotification(Date timestamp, Assignment assignment) {
        this.timestamp = timestamp;
        this.assignment = assignment;
    }

    private DeliveryNotification() {
        timestamp = null;
        assignment = null;
    }
}
