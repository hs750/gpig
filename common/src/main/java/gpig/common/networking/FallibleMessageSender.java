package gpig.common.networking;

import java.util.UUID;

import gpig.common.data.DroneState;
import gpig.common.messages.DeliveryDroneHeartbeat;
import gpig.common.messages.DetectionDroneHeartbeat;
import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.MessageType;
import gpig.common.messages.handlers.FailCommandHandler;
import gpig.common.util.Log;

public class FallibleMessageSender extends MessageSender implements FailCommandHandler {
    private boolean failed = false;
    private UUID thisDrone;

    public FallibleMessageSender(CommunicationChannel channel, UUID thisDrone) {
        super(channel);
        this.thisDrone = thisDrone;
    }

    public void setFailed(boolean failed) {
        if(failed != this.failed){
            this.failed = failed;
            Log.info("Comminications are %s", failed ? "down" : "restored");
        }
        
    }
    
    @Override
    public boolean send(DeliveryDroneHeartbeat message) {
        returnedToBase(message.origin, message.state);
        return super.send(message);
    }
    
    @Override
    public boolean send(DetectionDroneHeartbeat message) {
        returnedToBase(message.origin, message.state);
        return super.send(message);
    }
    
    private void returnedToBase(UUID drone, DroneState state){
        if(thisDrone.equals(drone)){
            if(state == DroneState.UNDEPLOYED){
                setFailed(false);
            }
        }
    }

    @Override
    protected boolean send(Object message, MessageType type) {
        if (failed) {
            return false;
        }
        return super.send(message, type);
    }

    @Override
    public void handle(FailCommand message) {
        if (thisDrone.equals(message.drone)) {
            if (message.type == FailType.COMMS) {
                setFailed(true);
            } else if (message.type == FailType.RESTORE_COMMS) {
                setFailed(false);
            } else if (message.type == FailType.FATAL){
                setFailed(true);
            }
            // Ignore others
        }

    }

}
