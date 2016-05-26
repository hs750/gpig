package gpig.common.messages.heartbeater;

import gpig.common.data.DroneState;

public class StateProvider {
    private DroneState state;
    
    public StateProvider() {
        setUndeployed();
    }
    
    public DroneState getState(){
        return state;
    }
    
    public void setOutbound(){
        state = DroneState.OUTBOUND;
    }
    
    public void setUndeployed(){
        state = DroneState.UNDEPLOYED;
    }
    
    public void setReturning(){
        state = DroneState.RETURNING;
    }
    
    public void setFaulty(){
        state = DroneState.FAULTY;
    }
    
    public void setCrashed(){
        state = DroneState.CRASHED;
    }
    
    public void setDelivering(){
        state = DroneState.DELIVERING;
    }
}
