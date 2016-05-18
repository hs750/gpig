package gpig.c2;

import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.c2.data.C2Data;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

import gpig.c2.gui.GUI;
import gpig.common.util.Log;

public class C2 {
	private GUI gui;
	private C2Config config;

    public C2(C2Config config) {
        Log.info("Starting C2");
   
        this.config = config;

        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(config.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);
        C2Data c2data = new C2Data();
        c2data.addAllHandlers(msgFromDCs);

        // Allocate DCs to deliver to detections
        new DetectionAllocator(msgToDCs, msgFromDCs, c2data);
        
        //data generation must come here
    }

    public void run() {
        //create and update the gui in the event dispatch thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            gui = new GUI(config.victimDetections,config.dcLocations);
            gui.createAndShowGUI();
        });
    }

    public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        C2Config conf = C2Config.getConfig(configPath, C2Config.class);

        C2 c2 = new C2(conf);
        c2.run();
    }
}
