package gpig.drones.detection;

import java.io.File;
import java.io.IOException;

import gpig.common.data.Constants;
import gpig.common.data.Location;
import gpig.common.movement.MovementBehaviour;
import gpig.common.movement.WaypointBasedMovement;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.detection.config.DetectionDroneConfig;

public class DetectionDrone {

    MovementBehaviour movementBehaviour;
    DetectionBehaviour detectionBehaviour;

    DetectionDrone(DetectionDroneConfig config) throws IOException {
        Log.info("Starting detection drone");

        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dtdcChannel, msgFromDC);
        MessageSender msgToDC = new MessageSender(dtdcChannel);

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
