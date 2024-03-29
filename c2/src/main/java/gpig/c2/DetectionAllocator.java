package gpig.c2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    private ConcurrentLinkedQueue<DetectionNotification> unableDeliveries;
    private Set<Location> serviceLocations;

    public DetectionAllocator(MessageSender dcMessageSender, MessageReceiver dcMessenger, C2Data database) {
        this.database = database;
        dcMessenger.addHandler(this);
        this.dcMessageSender = dcMessageSender;
        unallocatedDeliveries = new ConcurrentLinkedQueue<>();
        unableDeliveries = new ConcurrentLinkedQueue<>();
        serviceLocations = Collections.synchronizedSet(new HashSet<>());
        start();
    }

    @Override
    public void handle(DetectionNotification message) {
        boolean newLoc = serviceLocations.add(message.detection.person.location);
        if (newLoc) {
            unallocatedDeliveries.add(message);
        }

    }

    @Override
    public void run() {
        super.run();
        while (true) {
            DetectionNotification message = unallocatedDeliveries.poll();

            if (message != null) {

                Map<UUID, Location> dcs = database.getDCLocations();
                List<UUID> activeDCs = database.getActiveDCs();
                Kilometres closestDCDist = new Kilometres(Double.MAX_VALUE);
                UUID closestDC = null;

                synchronized (dcs) {
                    for (Entry<UUID, Location> DCs : dcs.entrySet()) {
                        if (activeDCs.contains(DCs.getKey())) {
                            Kilometres dist = DCs.getValue().distanceFrom(message.detection.person.location);
                            if (dist.value() <= Constants.DEPLOYMENT_DELIVERY_RADIUS.value()) {
                                if (dist.value() < closestDCDist.value()) {
                                    closestDC = DCs.getKey();
                                }
                            }
                        }
                    }
                }

                if (closestDC != null) {
                    Assignment a = new Assignment(message.detection, closestDC);
                    
                    if(!database.getAssignments().contains(a)){
                        DeliveryAssignment da = new DeliveryAssignment(a);
                        dcMessageSender.send(da);
                        database.getDeliveryAssignmentHandler().handle(da);
                        
                        Log.info("Delivery to %s assigned to %s", message.detection.person.location.toString(), closestDC);
                    }else{
                        Log.info("Delivery to %s has already been assigned.", message.detection.person.location.toString(), closestDC);
                    }
                    

                    
                } else {
                    // Attempt delivery assignment later.
                    unableDeliveries.add(message);
                    Log.warn("Unable to assign delivery to %s", message.detection.person.location.toString());
                }
            } else {
                // If detection was un-assignable first time around, only
                // re-attempt one assignment a second.
                DetectionNotification dn = unableDeliveries.poll();
                if (dn != null) {
                    unallocatedDeliveries.add(dn);
                }

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
