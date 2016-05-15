package gpig.drones.detection;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.detection.config.DetectionDroneConfig;

public class DetectionDrone {

    public DetectionDrone(DetectionDroneConfig config) {
        Log.info("Starting detection drone");

        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dtdcChannel, msgFromDC);
        MessageSender msgToDC = new MessageSender(dtdcChannel);
    }

    public void run() {
    }

    public static void main(String... args) throws IOException {
        DetectionDroneConfig conf = DetectionDroneConfig.getConfig(DetectionDroneConfig.class);
        DetectionDrone drone = new DetectionDrone(conf);
        drone.run();
    }

}
