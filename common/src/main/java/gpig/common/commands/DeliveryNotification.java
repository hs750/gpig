package gpig.common.commands;

import gpig.common.data.Assignment;

/**
 * A command representing the successful delivery of supplies to a detected person
 */
public class DeliveryNotification {
    public final Assignment assignment;

    public DeliveryNotification(Assignment assignment) {
        this.assignment = assignment;
    }
}
