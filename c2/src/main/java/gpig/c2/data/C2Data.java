package gpig.c2.data;

import gpig.c2.data.handlers.*;
import gpig.common.data.Assignment;
import gpig.common.data.Detection;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.common.networking.MessageReceiver;

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

    public C2Data() {
        assignments = Collections.synchronizedList(new ArrayList<>());
        deliveryDronesState = new ConcurrentHashMap<>();
        dcLocations = new ConcurrentHashMap<>();
        detectionDronesState = new ConcurrentHashMap<>();
        detections = Collections.synchronizedList(new ArrayList<>());
    }

    public void addAllHandlers(MessageReceiver receiver) {
        receiver.addHandler(new C2DeliveryAssignmentHandler(assignments));
        receiver.addHandler(new C2DeliveryDroneHeartbeatHandler(deliveryDronesState));
        receiver.addHandler(new C2DeliveryNotificationHandler(assignments));
        receiver.addHandler(new C2DeploymentCentreHeartbeatHandler(dcLocations));
        receiver.addHandler(new C2DetectionDroneHeartbeatHandler(detectionDronesState));
        receiver.addHandler(new C2DetectionNotificationHandler(detections));
    }

    public List<Detection> getDetections() {
        return Collections.unmodifiableList(detections);
    }
    
    public Map<UUID, Location> getDCLocations(){
        return Collections.unmodifiableMap(dcLocations);
    }

	public synchronized int getNumberOfDCs() {
		return numberOfDCs;
	}
	
	public synchronized int getNumberOfUndeployedDCs() {
		return numberOfUndeployedDCs;
	}

	public synchronized void setNumberOfDCs(int numberOfDCs) {
		this.numberOfDCs = numberOfDCs;
	}

	public synchronized void setNumberOfUndeployedDCs(int numberOfUndeployedDCs) {
		this.numberOfUndeployedDCs = numberOfUndeployedDCs;
	}
	
	
    
    
}
