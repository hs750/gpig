package gpig.c2;

import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

import gpig.c2.gui.GUI;
import gpig.common.util.Log;

public class C2 {
	private GUI gui;

    public C2(C2Config config) {
        Log.info("Starting C2");

        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(config.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);

        //data generation must come here
    }

    public void run() {
        //create and update the gui in the event dispatch thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            gui = new GUI();
            gui.createAndShowGUI();
        });
    }

    public static void main(String... args) throws IOException {
        C2Config conf = C2Config.getConfig(C2Config.class);
        C2 c2 = new C2(conf);
        c2.run();
    }
}
