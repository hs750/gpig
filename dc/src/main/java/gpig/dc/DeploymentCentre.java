package gpig.dc;

import java.io.IOException;

import gpig.common.data.Constants;
import gpig.common.data.DeploymentArea;
import gpig.common.data.Location;
import gpig.common.messages.handlers.DetectionDroneHeartbeatHandler;
import gpig.common.movement.ImmediateReturn;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.common.util.Log;
import gpig.dc.config.DCConfig;
import gpig.dc.dispatching.DeliveryDroneDispatcher;
import gpig.dc.dispatching.DetectionDroneDispatcher;

public class DeploymentCentre {

    public DeploymentCentre(DCConfig config) {
        Log.info("Starting mobile deployment centre");

        // C2-DC communication
        MessageReceiver msgFromC2 = new MessageReceiver();
        CommunicationChannel dcc2Channel = new CommunicationChannel(config.dcc2Channel, msgFromC2);
        MessageSender msgToC2 = new MessageSender(dcc2Channel);

        // DC-DetectionDrone communication
        MessageReceiver msgFromDts = new MessageReceiver();
        CommunicationChannel dcdtChannel = new CommunicationChannel(config.dcdtChannel, msgFromDts);
        MessageSender msgToDts = new MessageSender(dcdtChannel);

        // DC-DeliveryDrone communication
        MessageReceiver msgFromDes = new MessageReceiver();
        CommunicationChannel dcdeChannel = new CommunicationChannel(config.dcdeChannel, msgFromDes);
        MessageSender msgToDes = new MessageSender(dcdeChannel);
        
        DetectionDroneDispatcher dtdd = new DetectionDroneDispatcher(msgToDts, new ImmediateReturn(), new DeploymentArea(new Location(0, 0), Constants.DEPLOYMENT_SEARCH_RADIUS)); //TODO create this object when a true locaiton is known.
        msgFromDts.addHandler(dtdd);
        
        DeliveryDroneDispatcher dedd = new DeliveryDroneDispatcher(msgToDes, new ImmediateReturn(), new DeploymentArea(new Location(0, 0), Constants.DEPLOYMENT_DELIVERY_RADIUS)); //TODO create this object when a true locaiton is known.
        msgFromDes.addHandler((DetectionDroneHeartbeatHandler) dedd);

        //Forward messages from drones to C2
        new DroneMessageForwarder(msgToC2, msgFromDts, msgFromDes);
        
    }

    public void run() {
    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        DCConfig conf = DCConfig.getConfig(configPath, DCConfig.class);

        DeploymentCentre dc = new DeploymentCentre(conf);
        dc.run();
    }
}
