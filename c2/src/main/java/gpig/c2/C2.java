package gpig.c2;

import java.io.IOException;

import gpig.c2.config.C2Config;
import gpig.c2.data.C2Data;
import gpig.common.data.Location;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

import gpig.c2.gui.*;
import gpig.common.util.Log;

public class C2 {
	private GUI gui;
	private GUIAdapterInbound guiAdapterInbound;
	private GUIAdapterOutbound guiAdapterOutbound;
	
	private C2Config config;
	private C2Data c2data;

    public C2(C2Config config) {
        Log.info("Starting C2");
   
        this.config = config;

        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(config.c2dcChannel, msgFromDCs);
        MessageSender msgToDCs = new MessageSender(c2dcChannel);
        c2data = new C2Data();
        c2data.addAllHandlers(msgFromDCs);

        guiAdapterInbound = new GUIAdapterInbound(config.victimDetections,config.dcLocations, c2data);
        guiAdapterOutbound = new GUIAdapterOutbound(this);
        
        // Allocate DCs to deliver to detections
        new DetectionAllocator(msgToDCs, msgFromDCs, c2data);
        
    }

    public void run() {
        //create and update the gui in the event dispatch thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            gui = new GUI(guiAdapterInbound,guiAdapterOutbound);
            gui.createAndShowGUI();
        });
    }
    
    //needs to send a deploy request to a dc
    public void DeployRedeployDC(Location location){
    	
    	if(c2data.getNumberOfUndeployedDCs() > 0){
    		//deployment
    		//send request here
    		
    		c2data.setNumberOfUndeployedDCs(c2data.getNumberOfUndeployedDCs()-1);
    	}else{
    		//re-deployment
    		//figure out the closest dc
    		//send deployment request
    	}
    	
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
    
    //methods used to mock c2data
    public GUI getGUI(){
    	return gui;
    }
    
    public C2Config getC2Config(){
    	return config;
    }
    
}
