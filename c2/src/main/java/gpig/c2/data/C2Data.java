package gpig.c2.data;

import gpig.c2.data.handlers.*;
import gpig.common.data.Assignment;
import gpig.common.data.Detection;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.networking.MessageReceiver;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class C2Data {
	private int numberOfDCs = 2;
	private int numberOfUndeployedDCs = 2;
    private List<Assignment> assignments;
    private ConcurrentHashMap<UUID, DroneState> deliveryDronesState;
    private ConcurrentHashMap<UUID, Location> dcLocations;
    private ConcurrentHashMap<UUID, DroneState> detectionDronesState;
    private List<Detection> detections;
    private Map<Assignment, LocalDateTime> deliveryTimes;
    
    //should only be populated with locations of drones that have been deployed
    private ConcurrentHashMap<UUID, Location> deliveryDronesLocation;
    private ConcurrentHashMap<UUID, Location> detectionDronesLocation;

    public C2Data() {
        assignments = Collections.synchronizedList(new ArrayList<>());
        deliveryTimes = new ConcurrentHashMap<>();
        deliveryDronesState = new ConcurrentHashMap<>();
        dcLocations = new ConcurrentHashMap<>();
        detectionDronesState = new ConcurrentHashMap<>();
        detections = Collections.synchronizedList(new ArrayList<>());
        deliveryDronesLocation = new ConcurrentHashMap<>();
        detectionDronesLocation = new ConcurrentHashMap<>();
    }

    public void addAllHandlers(MessageReceiver receiver) {
        receiver.addHandler(new C2DeliveryAssignmentHandler(assignments));
        receiver.addHandler(new C2DeliveryDroneHeartbeatHandler(deliveryDronesState));
        receiver.addHandler(new C2DeliveryNotificationHandler(assignments, deliveryTimes));
        receiver.addHandler(new C2DeploymentCentreHeartbeatHandler(dcLocations));
        receiver.addHandler(new C2DetectionDroneHeartbeatHandler(detectionDronesState));
        receiver.addHandler(new C2DetectionNotificationHandler(detections));
    }

    public synchronized List<Assignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
    
    public synchronized List<Detection> getDetections() {
        return Collections.unmodifiableList(detections);
    }
    
    public Map<UUID, Location> getDCLocations(){
        return Collections.unmodifiableMap(dcLocations);
    }
    
    public Map<UUID, Location> getDeliveryDronesLocation(){
        return Collections.unmodifiableMap(deliveryDronesLocation);
    }
    
    public Map<UUID, Location> getDetectionDronesLocation(){
        return Collections.unmodifiableMap(detectionDronesLocation);
    }
    
    public Map<UUID, DroneState> getDeliveryDronesState(){
        return Collections.unmodifiableMap(deliveryDronesState);
    }
    
    public Map<UUID, DroneState> getDetectionDronesState(){
        return Collections.unmodifiableMap(detectionDronesState);
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
