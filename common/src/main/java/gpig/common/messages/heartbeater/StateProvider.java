package gpig.common.messages.heartbeater;

import gpig.common.data.DroneState;

public class StateProvider {
    private DroneState state;
    
    public StateProvider() {
        setUndeployed();
    }
    
    public synchronized DroneState getState(){
        return state;
    }
    
    public synchronized void setOutbound(){
        state = DroneState.OUTBOUND;
    }
    
    public synchronized void setUndeployed(){
        state = DroneState.UNDEPLOYED;
    }
    
    public synchronized void setReturning(){
        state = DroneState.RETURNING;
    }
    
    public synchronized void setFaulty(){
        state = DroneState.FAULTY;
    }
    
    public synchronized void setCrashed(){
        state = DroneState.CRASHED;
    }
    
    public synchronized void setDelivering(){
        state = DroneState.DELIVERING;
    }
}
