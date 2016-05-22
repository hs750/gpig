package gpig.c2.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

import gpig.common.data.Assignment;
import gpig.common.data.Assignment.AssignmentStatus;
import gpig.common.data.Detection;
import gpig.common.data.DroneState;
import gpig.common.data.Location;
import gpig.c2.data.C2Data;
import gpig.common.config.DetectionsConfig;
import gpig.common.config.LocationsConfig;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class GUIAdapterInbound {
	
	private DetectionsConfig detectionsConfig;
	private LocationsConfig dcLocationsConfig;
	private C2Data c2Data;
	
	private HashMap<UUID, Location> configDcLocations;
	private ArrayList<Detection> configDetections;
	
	public GUIAdapterInbound(DetectionsConfig detectionsConfig, LocationsConfig dcLocationsConfig, C2Data c2data){
		this.detectionsConfig = detectionsConfig;
		this.dcLocationsConfig = dcLocationsConfig;
		this.c2Data = c2data;
		
		this.configDetections = readDetectionsPredefined();
		this.configDcLocations = readDCLocationsPredefined();
	}
	
	//bulk live data from c2
	
	public HashMap<UUID,Location> getDCLocations(){
		
		HashMap<UUID,Location> dcLocations = new HashMap<UUID,Location>(c2Data.getDCLocations());
		return dcLocations;
	}
	
	public ArrayList<Detection> getUndeliveredDetections(){
		ArrayList<Detection> undeliveredDetections = new ArrayList<Detection>();
		
		List<Detection> allDetections = c2Data.getDetections();
		List<Assignment> allAssignments = c2Data.getAssignments();
		ArrayList<UUID> deliveredDetectionIDs = new ArrayList<UUID>();
		
		for(Assignment assignment : allAssignments){
			if(assignment.status == AssignmentStatus.DELIVERED){
				deliveredDetectionIDs.add(assignment.detection.person.id);
			}
		}
		
		for(Detection detection : allDetections){
			if( ! deliveredDetectionIDs.contains(detection.person.id) ){
				undeliveredDetections.add(detection);
			}
		}
		
		
		return undeliveredDetections;
	}
	
	public ArrayList<Detection> getDeliveredDetections(){
		ArrayList<Detection> deliveredDetections = new ArrayList<Detection>();
		
		List<Assignment> allAssignments = c2Data.getAssignments();
		ArrayList<UUID> deliveredDetectionIDs = new ArrayList<UUID>();
		
		
		for(Assignment assignment : allAssignments){
			if( assignment.status == AssignmentStatus.DELIVERED){
				deliveredDetections.add(assignment.detection);
			}
		}
		
		
		return deliveredDetections;
	}
	
	public HashMap<UUID,Location> getDeliveryDroneLocations(){
		
		HashMap<UUID,Location> deliveryDroneLocations = new HashMap<UUID,Location>(c2Data.getDeliveryDronesLocation());

		return deliveryDroneLocations;
	}
	
	public HashMap<UUID,Location> getDetectionDroneLocations(){
		
		HashMap<UUID,Location> detectionDroneLocations = new HashMap<UUID,Location>(c2Data.getDetectionDronesLocation());

		return detectionDroneLocations;
	}
	
	
	//individual live data from c2
	
	//get detection data from c2 by detection id
	public Detection getDetectionByID(UUID id){
		Detection result = null;
		List<Detection> detections =  c2Data.getDetections();
		
		for(Detection detection : detections){
			if(detection.person.id == id){
				result = detection;
				break;
			}	
		}
	
		return result;
	}
	
	//get dc location data from c2 by detection id
	public Location getDCLocationByID(UUID id) {
		
		Location result;
		
		result = c2Data.getDCLocations().get(id);
		
		return result;
	}
	
	//get detection drone location data from c2 by drone id
	public Location getDetectionDroneLocationByID(UUID id) {
		
		return c2Data.getDetectionDronesLocation().get(id);
	}
	
	//get delivery drone location data from c2 by drone id
	public Location getDeliveryDroneLocationByID(UUID id) {
		
		return c2Data.getDetectionDronesLocation().get(id);
	}
	
	//get detection drone state data from c2 by drone id
	public DroneState getDetectionDroneStateByID(UUID id) {
		
		return c2Data.getDetectionDronesState().get(id);
	}
	
	//get delivery drone state data from c2 by drone id
	public DroneState getDeliveryDroneStateByID(UUID id) {
		
		return c2Data.getDeliveryDronesState().get(id);
	}
	
	//tell the gui if there are any free dcs for deployment
	public boolean canDeploy(){
		return ( c2Data.getNumberOfUndeployedDCs() > 0);
	}
	
	
	
	//predefined data from config

	//get detection data from config by detection id
	public Detection getPredefinedDetectionByID(UUID id){
		Detection result = null;
		
		for(Detection detection : configDetections){
			if(detection.person.id == id){
				result = detection;
				break;
			}
				
		}
	
		return result;
	}
	
	
	//get dc location data from config by detection id
	public Location getPredefinedDCLocationByID(UUID id) {
		Location result = null;
		
		for(UUID tid: configDcLocations.keySet()){
			if(tid == id){
				result = configDcLocations.get(tid);
				break;
			}
		}
		
		return result;
	}
	
	
	/**
	 * Read the list of predefined detections from config
	 */
	private ArrayList<Detection> readDetectionsPredefined(){
		
		ArrayList<Detection> detections;
		
		detections = detectionsConfig.detections;
		return detections;
	}
	
	
	
	/**
	 * Read in the list of predefined deployment center locations from config
	 */
	private HashMap<UUID,Location> readDCLocationsPredefined(){
		HashMap<UUID,Location> dcsInfo = new HashMap<UUID,Location>();
		
		ArrayList<Location> dcLocations = dcLocationsConfig.locations;
		
		for(Location location : dcLocations){
			dcsInfo.put(UUID.randomUUID(), location);
		}
		
		return dcsInfo;
	}
	
	
	/**
	 * @return The list of predefined deployment center locations
	 */
	public HashMap<UUID,Location> getDCLocationsPredefined(){
		return configDcLocations;
	}
	
	/**
	 * @return The list of predefined detections.
	 */
	public ArrayList<Detection> getDetectionsPredefined(){
		
		return configDetections;
	}
	
	

}
