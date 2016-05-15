package gpig.drones.detection;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.drones.detection.config.DetectionDroneConfig;

public class DetectionDrone {

    public static void main(String... args) throws IOException {
        System.out.println("Starting detection drone");
        
        DetectionDroneConfig conf = DetectionDroneConfig.getConfig(DetectionDroneConfig.class);
        
        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(conf.dtdcChannel, msgFromDC);
        MessageSender msgToDC = new MessageSender(dtdcChannel);
        
    }

}
