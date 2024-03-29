package gpig.common.messages;

import gpig.common.data.Assignment;

/**
 * A command representing the assignment of a delivery to a single deployment centre.
 */
public class DeliveryAssignment {
    public final Assignment assignment;

    public DeliveryAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    private DeliveryAssignment() {
        assignment = null;
    }
}
