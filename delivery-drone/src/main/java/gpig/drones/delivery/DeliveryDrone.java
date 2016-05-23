package gpig.drones.delivery;

import java.io.IOException;
import java.util.UUID;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.FallibleMessageSender;
import gpig.common.networking.MessageReceiver;
import gpig.common.util.Log;
import gpig.drones.delivery.config.DeliveryDroneConfig;

public class DeliveryDrone {
    private UUID thisDrone;
    public DeliveryDrone(DeliveryDroneConfig config) {
        Log.info("Starting delivery drone");

        thisDrone = UUID.randomUUID();
        
        MessageReceiver msgFromDC = new MessageReceiver();
        CommunicationChannel dtdcChannel = new CommunicationChannel(config.dedcChannel, msgFromDC);
        FallibleMessageSender msgToDC = new FallibleMessageSender(dtdcChannel, thisDrone);
        msgFromDC.addHandler(msgToDC); // Handle comms failures
        
    }

    public void run() {

    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        DeliveryDroneConfig conf = DeliveryDroneConfig.getConfig(configPath, DeliveryDroneConfig.class);

        DeliveryDrone drone = new DeliveryDrone(conf);
        drone.run();
    }
}
