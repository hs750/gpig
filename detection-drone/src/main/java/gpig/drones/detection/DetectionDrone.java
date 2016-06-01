package gpig.drones.detection;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.io.SegmentedStringWriter;

import gpig.common.battery.Battery;
import gpig.common.data.Constants;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.messages.DeploymentCentreHeartbeat;
import gpig.common.messages.heartbeater.LocationProvider;
import gpig.common.messages.heartbeater.StateProvider;
import gpig.common.movement.BatteryFailsafeBehaviour;
import gpig.common.movement.MovementBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.movement.failsafe.BatteryLevelLowFailsafe;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.FallibleMessageSender;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.detection.config.DetectionDroneConfig;

public class DetectionDrone {
    UUID thisDrone;
    
    MovementBehaviour movementBehaviour;
    Detector detector;
    Battery battery;
    StateProvider state;
    Location dcLocation;
    int heartbeatRate;

    DetectionDrone(DetectionDroneConfig config) throws IOException {
        Log.info("Starting detection drone");
        thisDrone = UUID.randomUUID();
        heartbeatRate = config.heartbeatRate;
        
        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dtdcChannel, msgFromDC);
        FallibleMessageSender msgToDC = new FallibleMessageSender(dtdcChannel, thisDrone);
        msgFromDC.addHandler(msgToDC); // Handle comms failures
        
        state = new StateProvider();
        dcLocation = new Location(0,0);
        
        battery = new Battery(Constants.AERIAL_VEHICLE_BATTERY_DURATION);

        // TODO: Actual initial location
        Location homeLocation = new Location(0.0, 0.0);
        movementBehaviour = new WaypointBasedMovement(homeLocation, Constants.DETECTION_DRONE_SPEED, new BatteryLevelLowFailsafe(battery, homeLocation));
        DetectionBehaviour db = new DetectionsFromConfig(config.detectionConfig);
        detector = new Detector(db, msgToDC);
        
        msgFromDC.addHandler(new DetectionPathHandler(this));
        msgFromDC.addHandler(new DetectionFatalFailureHandler(this));
        msgFromDC.addHandler(new DetectionBatteryFailureHandler(this));
        
        new DetectionHeartbeater(thisDrone, msgToDC, (LocationProvider) movementBehaviour, state, heartbeatRate);
    }

    void run() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(() -> {
            if(isDeployed()){
                step();
                synchronized (this) {
                    if(movementBehaviour.currentLocation().equals(dcLocation) && movementBehaviour.isStationary()){
                        Log.info("Returned to DC");
                        setReturned();
                    }
                }
            }
            
        }, 0, heartbeatRate, TimeUnit.MILLISECONDS);
    }
    
    private void step(){
        Location start = movementBehaviour.currentLocation();
        movementBehaviour.step();
        Location end = movementBehaviour.currentLocation();
        detector.detect(start, end);
    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        DetectionDroneConfig conf = DetectionDroneConfig.getConfig(configPath, DetectionDroneConfig.class);

        DetectionDrone drone = new DetectionDrone(conf);
        drone.run();
    }
    
    public void setDeployed(){
        state.setOutbound();
    }
    
    public void setReturned(){
        state.setUndeployed();
    }
    
    public boolean isDeployed(){
        return state.getState() != DroneState.UNDEPLOYED;
    }
    
    public void setCrashed(){
        state.setCrashed();
    }

}
