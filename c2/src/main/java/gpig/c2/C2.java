package gpig.c2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import gpig.c2.config.C2Config;
import gpig.c2.data.C2Data;
import gpig.common.data.Location;
import gpig.common.data.Path;
import gpig.common.messages.FailCommand;
import gpig.common.messages.FailCommand.FailType;
import gpig.common.messages.SetPath;
import gpig.common.networking.CommunicationChannel;
import gpig.common.networking.FallibleMessageSender;
import gpig.common.networking.MessageReceiver;
import gpig.common.networking.MessageSender;

import gpig.c2.gui.*;
import gpig.common.util.Log;

public class C2 {
	private GUI gui;
	private GUIAdapterInbound guiAdapterInbound;
	private GUIAdapterOutbound guiAdapterOutbound;
	private MessageSender msgToDCs;
	
	private C2Config config;
	private C2Data c2data;

    public C2(C2Config config) {
        Log.info("Starting C2");
   
        this.config = config;

        MessageReceiver msgFromDCs = new MessageReceiver();
        CommunicationChannel c2dcChannel = new CommunicationChannel(config.c2dcChannel, msgFromDCs);
        msgToDCs = new MessageSender(c2dcChannel);
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
    
    //GUI called methods
    //needs to send a deploy request to a dc
    public void deployRedeployDC(Location location){
    	
    	if(c2data.getNumberOfUndeployedDCs() > 0){
    		//deployment
    		//send request here

            // TODO: find first undeployed DC
            UUID dcToDeployTo = c2data.getDCLocations().keySet().stream().collect(Collectors.toList()).get(0);

            // TODO: movement to the path instead of teleporting
            Path path = new Path(location);
            SetPath setPathCmd = new SetPath(path, dcToDeployTo);

            msgToDCs.send(setPathCmd);
    		
    		c2data.setNumberOfUndeployedDCs(c2data.getNumberOfUndeployedDCs()-1);
    	}else{
    		//re-deployment
    		//figure out the closest dc
    		//send deployment request
    	}
    	
    }
    
    //need to send requests to dcs here
    public void failBattery(UUID id){
    	 msgToDCs.send(new FailCommand(id, 51));
    	
    }
    public void failComms(UUID id){
    	msgToDCs.send(new FailCommand(id, FailType.COMMS));
    }
    public void failEngine(UUID id){
    	msgToDCs.send(new FailCommand(id, FailType.FATAL));
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
