package gpig.drones.delivery;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.drones.delivery.config.DeliveryDroneConfig;

public class DeliveryDrone {

    public DeliveryDrone(DeliveryDroneConfig config) {
        Log.info("Starting delivery drone");

        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dedcChannel, msgFromDC);
        MessageSender msgToDC = new MessageSender(dtdcChannel);
    }

    public void run() {

    }

    public static void main(String... args) throws IOException {
        DeliveryDroneConfig conf = DeliveryDroneConfig.getConfig(DeliveryDroneConfig.class);
        DeliveryDrone drone = new DeliveryDrone(conf);
        drone.run();
    }
}
