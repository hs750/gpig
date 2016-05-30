package gpig.c2.data;

import gpig.c2.data.handlers.*;
import gpig.common.data.Assignment;
import gpig.common.data.DCState;
import gpig.common.data.Detection;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.messages.handlers.DeliveryAssignmentHandler;
import gpig.common.messages.handlers.DetectionNotificationHandler;
import gpig.common.networking.MessageReceiver;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class C2Data {
	private int numberOfDCs = 2;
	private int numberOfUndeployedDCs = 2;
    private List<Assignment> assignments;
    private ConcurrentHashMap<UUID, DroneState> deliveryDronesState;
    private ConcurrentHashMap<UUID, Location> dcLocations;
    private ConcurrentHashMap<UUID, DCState> dcStates;
    private ConcurrentHashMap<UUID, DroneState> detectionDronesState;
    private List<Detection> detections;
    private Map<Assignment, LocalDateTime> deliveryTimes;
    
    //should only be populated with locations of drones that have been deployed
    private ConcurrentHashMap<UUID, Location> deliveryDronesLocation;
    private ConcurrentHashMap<UUID, Location> detectionDronesLocation;
    
    private DetectionNotificationHandler detectionHandler;
    private DeliveryAssignmentHandler deliveryAssignmentHandler;

    public C2Data() {
        assignments = Collections.synchronizedList(new ArrayList<>());
        deliveryTimes = new ConcurrentHashMap<>();
        deliveryDronesState = new ConcurrentHashMap<>();
        dcLocations = new ConcurrentHashMap<>();
        detectionDronesState = new ConcurrentHashMap<>();
        detections = Collections.synchronizedList(new ArrayList<>());
        deliveryDronesLocation = new ConcurrentHashMap<>();
        detectionDronesLocation = new ConcurrentHashMap<>();
        dcStates = new ConcurrentHashMap<>();
    }

    public void addAllHandlers(MessageReceiver receiver) {
        detectionHandler = new C2DetectionNotificationHandler(detections);
        deliveryAssignmentHandler = new C2DeliveryAssignmentHandler(assignments);
        receiver.addHandler(deliveryAssignmentHandler);
        receiver.addHandler(new C2DeliveryDroneHeartbeatHandler(deliveryDronesState, deliveryDronesLocation));
        receiver.addHandler(new C2DeliveryNotificationHandler(assignments, deliveryTimes));
        receiver.addHandler(new C2DeploymentCentreHeartbeatHandler(dcLocations, dcStates));
        receiver.addHandler(new C2DetectionDroneHeartbeatHandler(detectionDronesState, detectionDronesLocation));
        receiver.addHandler(detectionHandler);
    }
    
    public DetectionNotificationHandler getDetectionHandler(){
        return detectionHandler;
    }
    
    public DeliveryAssignmentHandler getDeliveryAssignmentHandler(){
        return deliveryAssignmentHandler;
    }

    public synchronized List<Assignment> getAssignments() {
        return assignments;
    }
    
    public Map<Assignment, LocalDateTime> getDeliveryTimes(){
        return deliveryTimes;
    }
    
    public synchronized List<Detection> getDetections() {
        return detections;
    }
    
    public Map<UUID, Location> getDCLocations(){
        return dcLocations;
    }
    
    public Map<UUID, Location> getDeliveryDronesLocation(){
        return deliveryDronesLocation;
    }
    
    public Map<UUID, Location> getDetectionDronesLocation(){
        return detectionDronesLocation;
    }
    
    public Map<UUID, DroneState> getDeliveryDronesState(){
        return deliveryDronesState;
    }
    
    public Map<UUID, DroneState> getDetectionDronesState(){
        return detectionDronesState;
    }
    
    public List<UUID> getActiveDCs(){
        ArrayList<UUID> ids = new ArrayList<>();
        synchronized (dcStates) {
            dcStates.forEach((k,v) -> {
                if(v == DCState.ACTIVE){
                    ids.add(k);
                }
             }); 
        }
        return ids;
    }
    
    public List<UUID> getInactiveDCs(){
        ArrayList<UUID> ids = new ArrayList<>();
        synchronized (dcStates) {
            dcStates.forEach((k,v) -> {
                if(v == DCState.INACTIVE){
                    ids.add(k);
                }
             });  
        }
        return ids;
    }

	public synchronized int getNumberOfDCs() {
		return numberOfDCs;
	}
	
	public synchronized int getNumberOfUndeployedDCs() {
		return numberOfUndeployedDCs;
	}
	
	//setters used to run the gui with mock data
	public synchronized void setNumberOfDCs(int numberOfDCs) {
		this.numberOfDCs = numberOfDCs;
	}

	public synchronized void setNumberOfUndeployedDCs(int numberOfUndeployedDCs) {
		this.numberOfUndeployedDCs = numberOfUndeployedDCs;
	}

	public synchronized void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}
	
	public synchronized void setDeliveryTimes(Map<Assignment, LocalDateTime> deliveries) {
        this.deliveryTimes = deliveries;
    }

	public void setDeliveryDronesState(ConcurrentHashMap<UUID, DroneState> deliveryDronesState) {
		this.deliveryDronesState = deliveryDronesState;
	}

	public void setDcLocations(ConcurrentHashMap<UUID, Location> dcLocations) {
		this.dcLocations = dcLocations;
	}

	public void setDetectionDronesState(ConcurrentHashMap<UUID, DroneState> detectionDronesState) {
		this.detectionDronesState = detectionDronesState;
	}

	public synchronized void setDetections(List<Detection> detections) {
		this.detections = detections;
	}

	public void setDeliveryDronesLocation(ConcurrentHashMap<UUID, Location> deliveryDronesLocation) {
		this.deliveryDronesLocation = deliveryDronesLocation;
	}

	public void setDetectionDronesLocation(ConcurrentHashMap<UUID, Location> detectionDronesLocation) {
		this.detectionDronesLocation = detectionDronesLocation;
	}
   
}
