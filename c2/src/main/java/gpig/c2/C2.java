package gpig.c2;

import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

public class C2 {

    public static void main(String... args) throws IOException {
        System.out.println("Starting C2");
        
<<<<<<< Upstream, based on origin/master
        C2Config conf = C2Config.getConfig(C2Config.class);
        
        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(conf.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);
        
=======
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
>>>>>>> a4b514d The most basic GUI.
    }

}
