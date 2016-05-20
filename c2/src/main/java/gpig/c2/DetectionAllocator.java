package gpig.c2;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import gpig.c2.data.C2Data;
import gpig.common.data.Assignment;
import gpig.common.data.Constants;
import gpig.common.data.Location;
import gpig.common.messages.DeliveryAssignment;
import gpig.common.messages.DetectionNotification;
import gpig.common.messages.handlers.DetectionNotificationHandler;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.units.Kilometres;
import gpig.common.util.Log;

/**
 * Assign a DC to delivery to a detection. Currently no load balancing between
 * DCs
 */
public class DetectionAllocator extends Thread implements DetectionNotificationHandler {
    private C2Data database;
    private MessageSender dcMessageSender;
    private ConcurrentLinkedQueue<DetectionNotification> unallocatedDeliveries;

    public DetectionAllocator(MessageSender dcMessageSender, MessageReceiver dcMessenger, C2Data database) {
        this.database = database;
        dcMessenger.addHandler(this);
        this.dcMessageSender = dcMessageSender;
        unallocatedDeliveries = new ConcurrentLinkedQueue<>();
        start();
    }

    @Override
    public void handle(DetectionNotification message) {
        unallocatedDeliveries.add(message);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            DetectionNotification message = unallocatedDeliveries.poll();

            if (message != null) {

                Map<UUID, Location> dcs = database.getDCLocations();
                Kilometres closestDCDist = new Kilometres(Double.MAX_VALUE);
                UUID closestDC = null;

                for (Entry<UUID, Location> DCs : dcs.entrySet()) {
                    Kilometres dist = DCs.getValue().distanceFrom(message.detection.person.location);
                    if (dist.value() <= Constants.DEPLOYMENT_DELIVERY_RADIUS.value()) {
                        if (dist.value() < closestDCDist.value()) {
                            closestDC = DCs.getKey();
                        }
                    }
                }

                if (closestDC != null) {
                    Assignment a = new Assignment(message.detection, closestDC);
                    DeliveryAssignment da = new DeliveryAssignment(a);
                    dcMessageSender.send(da);
                    Log.info("Delivery to %s assigned to %s", message.detection.person.location.toString(), closestDC);
                } else {
                    // Attempt delivery assignment later.
                    unallocatedDeliveries.add(message);
                    Log.warn("Unable to assign delivery to %s", message.detection.person.location.toString());
                }
            } else {
                // Wait for detections to need servicing.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
