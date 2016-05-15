package gpig.c2;

import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

import gpig.c2.gui.GUI;

public class C2 {
	private GUI gui;

    public static void main(String... args) throws IOException {
        System.out.println("Starting C2");
        C2 c2 = new C2();
        
        C2Config conf = C2Config.getConfig(C2Config.class);
        
        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(conf.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);
        
        //data generation must come here
        
        //create and update the gui in the event dispatch thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	c2.gui = new GUI();
                c2.gui.createAndShowGUI();
            }
        });
    }
    
    

}
