package gpig.c2.data.handlers;

import gpig.common.data.Assignment;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;

import java.util.List;

public class C2DeliveryAssignmentHandler implements DeliveryAssignmentHandler{
    private final List<Assignment> assignments;

    public C2DeliveryAssignmentHandler(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public void handle(DeliveryAssignment message) {
        assignments.add(message.assignment);
    }
}
