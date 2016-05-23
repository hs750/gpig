package gpig.drones.detection;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import gpig.common.data.Constants;
import gpig.common.data.Location;
import gpig.common.movement.MovementBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.FallibleMessageSender;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.detection.config.DetectionDroneConfig;

public class DetectionDrone {
    private UUID thisDrone;
    
    MovementBehaviour movementBehaviour;
    DetectionBehaviour detectionBehaviour;

    DetectionDrone(DetectionDroneConfig config) throws IOException {
        Log.info("Starting detection drone");
        thisDrone = UUID.randomUUID();
        
        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dtdcChannel, msgFromDC);
        FallibleMessageSender msgToDC = new FallibleMessageSender(dtdcChannel, thisDrone);
        msgFromDC.addHandler(msgToDC); // Handle comms failures

        // TODO: Actual initial location
        movementBehaviour = new WaypointBasedMovement(new Location(0.0, 0.0), Constants.DETECTION_DRONE_SPEED);
        detectionBehaviour = new DetectionsFromConfig(new File("victims.json"));
    }

    void run() {
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

}
