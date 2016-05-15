package gpig.drones.delivery;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.drones.delivery.config.DeliveryDroneConfig;

public class DeliveryDrone {

    public static void main(String... args) throws IOException {
        System.out.println("Starting delivery drone");
        
        DeliveryDroneConfig conf = DeliveryDroneConfig.getConfig(DeliveryDroneConfig.class);
        
        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(conf.dedcChannel, msgFromDC);
        MessageSender msgToDC = new MessageSender(dtdcChannel);
        
    }

}
