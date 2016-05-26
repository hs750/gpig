package gpig.c2.data.handlers;

import gpig.common.data.Assignment;
import gpig.common.messages.DeliveryNotification;
import gpig.common.messages.handlers.DeliveryNotificationHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class C2DeliveryNotificationHandler implements DeliveryNotificationHandler {
    private final List<Assignment> assignments;
    private final Map<Assignment, LocalDateTime> deliveryTimes;
    
    public C2DeliveryNotificationHandler(List<Assignment> assignments, Map<Assignment, LocalDateTime> deliveryTimes) {
        this.assignments = assignments;
        this.deliveryTimes = deliveryTimes;
    }

    @Override
    public void handle(DeliveryNotification message) {
        Optional<Assignment> ass = assignments.stream().filter(a -> a.equals(message.assignment)).findFirst();
        if (ass.isPresent()) {
            ass.get().status = Assignment.AssignmentStatus.DELIVERED;
            deliveryTimes.put(ass.get(), message.timestamp);
        }
    }
}
