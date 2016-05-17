package gpig.c2.data.handlers;

import gpig.common.data.Assignment;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.handlers.DeliveryNotificationHandler;

import java.util.List;

public class C2DeliveryNotificationHandler implements DeliveryNotificationHandler{
    private final List<Assignment> assignments;

    public C2DeliveryNotificationHandler(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public void handle(DeliveryNotification message) {
        message.assignment.status = Assignment.AssignmentStatus.DELIVERED;
    }
}
