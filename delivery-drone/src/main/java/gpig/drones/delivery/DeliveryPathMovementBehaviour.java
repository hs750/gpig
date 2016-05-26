package gpig.drones.delivery;

import java.time.LocalDateTime;
import java.util.UUID;

import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.data.Path.Waypoint;
import gpig.common.messages.DeliveryNotification;
import gpig.common.movement.BatteryFailsafeBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.networking.MessageSender;
import gpig.common.units.KMPH;

public class DeliveryPathMovementBehaviour extends WaypointBasedMovement{
    private MessageSender dcMessenger;
    boolean delivered;
    private UUID thisDrone;
    
    public DeliveryPathMovementBehaviour(Location initialLocation, KMPH speed,
            BatteryFailsafeBehaviour failsafeBehaviour, MessageSender dcMessenger, UUID thisDrone) {
        super(initialLocation, speed, failsafeBehaviour);
        this.dcMessenger = dcMessenger;
        this.delivered = false;
        this.thisDrone = thisDrone;
    }

    @Override
    protected Waypoint advancePath() {
        // Delivery notification upon reaching the first waypoint in the delivery path
        if(!delivered){
            delivered = true;
            dcMessenger.send(new DeliveryNotification(LocalDateTime.now(), thisDrone));
        }
        return super.advancePath();
    }
    
    @Override
    public void setPath(Path newPath) {
        delivered = false;
        super.setPath(newPath);
    }
}
