package gpig.dc;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.dc.config.DCConfig;

public class DeploymentCentre {

    public DeploymentCentre(DCConfig config) {
        System.out.println("Starting mobile deployment centre");

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
    }

    public void run() {
    }

    public static void main(String... args) throws IOException {
        DCConfig conf = DCConfig.getConfig(DCConfig.class);
        DeploymentCentre dc = new DeploymentCentre(conf);
        dc.run();
    }
}
