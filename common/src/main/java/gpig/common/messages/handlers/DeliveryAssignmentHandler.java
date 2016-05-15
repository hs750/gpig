package gpig.common.messages.handlers;

import gpig.common.messages.DeliveryAssignment;

@FunctionalInterface
public interface DeliveryAssignmentHandler {
    public void handle(DeliveryAssignment message);
}
