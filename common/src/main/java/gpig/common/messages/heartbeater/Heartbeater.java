package gpig.common.messages.heartbeater;

import java.util.UUID;

import gpig.common.networking.MessageSender;

public abstract class Heartbeater extends Thread{
    protected final UUID thisDrone;
    private final long period;
    protected final MessageSender messenger;
    protected final LocationProvider locationProvider;
    protected final StateProvider stateProvier;
    
    public Heartbeater(UUID thisDrone, long period, MessageSender messenger, LocationProvider locationProvider, StateProvider stateProvider) {
        this.thisDrone = thisDrone;
        this.period = period;
        this.messenger = messenger;
        this.locationProvider = locationProvider;
        this.stateProvier = stateProvider;
        
        start();
    }

    
    @Override
    public void run() {
        super.run();
        
        while(true){
            beat();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected abstract void beat();
    
}
