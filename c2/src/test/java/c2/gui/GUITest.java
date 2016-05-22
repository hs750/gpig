package c2.gui;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import gpig.c2.C2;
import gpig.c2.config.C2Config;
import gpig.c2.data.C2Data;
import gpig.c2.gui.GUI;
import gpig.c2.gui.GUIAdapterInbound;
import gpig.c2.gui.unfolding.MapApp;
import gpig.common.data.Assignment;
import gpig.common.data.Assignment.AssignmentStatus;
import gpig.common.data.Detection;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;
import processing.core.PApplet;

public class GUITest extends Thread{

	private C2 c2;
	private GUI gui;
	private C2Data mockC2Data;
	private C2Config c2Config;
	private long updateSpeed;
	
	public GUITest(C2 c2, int updateSpeed) {

		this.c2 = c2;
		
		while(c2.getGUI() == null){
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		this.gui = c2.getGUI();
		this.c2Config = c2.getC2Config();
		this.updateSpeed = updateSpeed;
	}
	
	@Override
	public void run() {

		//set up
		
		int numIterations = 8;
		
		mockC2Data = new C2Data();
	
		ArrayList<UUID> dcIDs = new ArrayList<UUID>();
		ArrayList<UUID> detectionDronesIDs = new ArrayList<UUID>();
		ArrayList<UUID> deliveryDronesIDs = new ArrayList<UUID>();
		ArrayList<UUID> detectionsIDs = new ArrayList<UUID>();
		
		for(int i=0;i<2;i++){
			dcIDs.add(UUID.randomUUID());
		}
		
		for(int i=0;i<2;i++){
			deliveryDronesIDs.add(UUID.randomUUID());
		}
		
		for(int i=0;i<2;i++){
			detectionDronesIDs.add(UUID.randomUUID());
		}
		
		for(int i=0;i<2;i++){
			detectionsIDs.add(UUID.randomUUID());
		}
		
		
	    List<Assignment> assignments;
	    ConcurrentHashMap<UUID, DroneState> deliveryDronesState;
	    ConcurrentHashMap<UUID, Location> dcLocations;
	    ConcurrentHashMap<UUID, DroneState> detectionDronesState;
	    List<Detection> detections;
	    ConcurrentHashMap<UUID, Location> deliveryDronesLocation;
	    ConcurrentHashMap<UUID, Location> detectionDronesLocation;
	    
	    //run data simulation
	    
        assignments = Collections.synchronizedList(new ArrayList<>());
        deliveryDronesState = new ConcurrentHashMap<>();
        dcLocations = new ConcurrentHashMap<>();
        detectionDronesState = new ConcurrentHashMap<>();
        detections = Collections.synchronizedList(new ArrayList<>());
        deliveryDronesLocation = new ConcurrentHashMap<>();
        detectionDronesLocation = new ConcurrentHashMap<>();
	    

        
        //move dcs
        
        Location dc1Current = new Location(53.918668,-0.989199);
        Location dc2Current = new Location(53.927462,-0.973063);
        
       
	    for(int i=0;i<numIterations;i++){
	        dcLocations = new ConcurrentHashMap<>();
	        dcLocations.put(dcIDs.get(0), dc1Current);
	        dcLocations.put(dcIDs.get(1), dc2Current);
	        
	        mockC2Data.setDcLocations(dcLocations);
	        gui.setAdapterInbound(new GUIAdapterInbound(c2Config.victimDetections,c2Config.dcLocations, mockC2Data));
	        
	        dc1Current = new Location(dc1Current.latitude()+0.01,dc1Current.longitude()+0.01);
	        dc2Current = new Location(dc2Current.latitude()+0.01,dc2Current.longitude()-0.01);

	        try {
				Thread.sleep(updateSpeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
        //deploy search drones
        
        Location detd1Current = dc1Current;
        Location detd2Current = dc2Current;
        
       
	    for(int i=0;i<numIterations;i++){
	    	detectionDronesLocation = new ConcurrentHashMap<>();
	        detectionDronesLocation.put(detectionDronesIDs.get(0), detd1Current);
	        detectionDronesLocation.put(detectionDronesIDs.get(1), detd2Current);
	        
	        detectionDronesState = new ConcurrentHashMap<>();
	        detectionDronesState.put(detectionDronesIDs.get(0), DroneState.OUTBOUND);
	        detectionDronesState.put(detectionDronesIDs.get(1), DroneState.OUTBOUND);
	        
	        
	        mockC2Data.setDetectionDronesLocation(detectionDronesLocation);
	        mockC2Data.setDetectionDronesState(detectionDronesState);
	        gui.setAdapterInbound(new GUIAdapterInbound(c2Config.victimDetections,c2Config.dcLocations, mockC2Data));
	        
	        detd1Current = new Location(detd1Current.latitude()+0.01,detd1Current.longitude()+0.01);
	        detd2Current = new Location(detd2Current.latitude()+0.01,detd2Current.longitude()-0.01);

	        try {
				Thread.sleep(updateSpeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	    //create detections
	    
	    detections = Collections.synchronizedList(new ArrayList<>());
	    
        Location det1Current = new Location(detd1Current.latitude()+0.05,detd1Current.longitude()+0.05);
        Location det2Current = new Location(detd2Current.latitude()+0.05,detd2Current.longitude()-0.05);
        
        Person person1 = new Person(PersonType.CIVILIAN, det1Current);
        Person person2 = new Person(PersonType.CIVILIAN, det2Current);
        person1.id = detectionsIDs.get(0);
        person2.id = detectionsIDs.get(1);
        
        detections.add(new Detection(person1,null,null));
        detections.add(new Detection(person2,null,null));
        
        mockC2Data.setDetections(detections);
        gui.setAdapterInbound(new GUIAdapterInbound(c2Config.victimDetections,c2Config.dcLocations, mockC2Data));
        
        //deploy delivery drones
        
        Location deld1Current = dc1Current;
        Location deld2Current = dc2Current;
        
       
	    for(int i=0;i<numIterations;i++){
	    	deliveryDronesLocation = new ConcurrentHashMap<>();
	        deliveryDronesLocation.put(deliveryDronesIDs.get(0), deld1Current);
	        deliveryDronesLocation.put(deliveryDronesIDs.get(1), deld2Current);
	        
	        deliveryDronesState = new ConcurrentHashMap<>();
	        deliveryDronesState.put(deliveryDronesIDs.get(0), DroneState.OUTBOUND);
	        deliveryDronesState.put(deliveryDronesIDs.get(1), DroneState.OUTBOUND);
	        
	        
	        mockC2Data.setDeliveryDronesLocation(deliveryDronesLocation);
	        mockC2Data.setDeliveryDronesState(deliveryDronesState);
	        gui.setAdapterInbound(new GUIAdapterInbound(c2Config.victimDetections,c2Config.dcLocations, mockC2Data));
	        
	        deld1Current = new Location(deld1Current.latitude()+0.02,deld1Current.longitude()+0.02);
	        deld2Current = new Location(deld2Current.latitude()+0.02,deld2Current.longitude()-0.02);

	        try {
				Thread.sleep(updateSpeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	    //create assignments
	    assignments = Collections.synchronizedList(new ArrayList<>());
	    Assignment assignment1 = new Assignment(detections.get(0), dcIDs.get(0));
	    Assignment assignment2 = new Assignment(detections.get(1), dcIDs.get(1));
	    
	    assignment1.status = AssignmentStatus.DELIVERED;
	    assignment2.status = AssignmentStatus.DELIVERED;
	    
	    assignments.add(assignment1);
	    assignments.add(assignment2);
        
	    
	    
        mockC2Data.setAssignments(assignments);
        gui.setAdapterInbound(new GUIAdapterInbound(c2Config.victimDetections,c2Config.dcLocations, mockC2Data));
	    
	    
        
    }
	
	public static void main(String... args) throws IOException {
        if (args.length != 1) {
            throw new IOException("Did not specify a config path");
        }

        String configPath = args[0];
        C2Config conf = C2Config.getConfig(configPath, C2Config.class);

        
        C2 c2 = new C2(conf);
        c2.run();
        
        GUITest guiTest = new GUITest(c2,500);
        guiTest.run();
    }

}
