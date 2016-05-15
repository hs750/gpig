package gpig.dc;

import java.io.IOException;

import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;
import gpig.dc.config.DCConfig;

public class DeploymentCentre {

    public static void main(String... args) throws IOException {
        System.out.println("Starting mobile deployment centre");
        
        DCConfig conf = DCConfig.getConfig(DCConfig.class);
        
        // C2-DC communication
        MessageReceiver msgFromC2 = new MessageReceiver();
        CommunicationChannel dcc2Channel = new CommunicationChannel(conf.dcc2Channel, msgFromC2);
        MessageSender msgToC2 = new MessageSender(dcc2Channel);
   
        // DC-DetectionDrone communication 
        MessageReceiver msgFromDts = new MessageReceiver();
        CommunicationChannel dcdtChannel = new CommunicationChannel(conf.dcdtChannel, msgFromDts);
        MessageSender msgToDts = new MessageSender(dcdtChannel);
        
        // DC-DeliveryDrone communication
        MessageReceiver msgFromDes = new MessageReceiver();
        CommunicationChannel dcdeChannel = new CommunicationChannel(conf.dcdeChannel, msgFromDes);
        MessageSender msgToDes = new MessageSender(dcdeChannel);
        
    }

}
