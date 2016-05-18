package gpig.dc.dispatching;

import java.util.UUID;

import gpig.common.messages.SetPath;
import gpig.common.messages.handlers.SetPathHandler;
import gpig.common.networking.MessageReceiver;
import gpig.common.util.Log;

/**
 * If a DC receives a request to move, it will recall all its drones.
 *
 */
public class DroneRecaller implements SetPathHandler{
    private UUID thisDC;
    private DroneDispatcher[] dispatchers;
    
    public DroneRecaller(UUID thisDC, MessageReceiver receiver, DroneDispatcher... dispatchers) {
        this.thisDC = thisDC;
        this.dispatchers = dispatchers;
        receiver.addHandler(this);
    }

    @Override
    public void handle(SetPath message) {
        if(message.assignee.equals(thisDC)){
            Log.info("Initiating drone recall");
            for(DroneDispatcher d : dispatchers){
                d.recoverDrones();
            }
        }
    }
}
