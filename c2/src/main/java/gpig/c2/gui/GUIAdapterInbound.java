package gpig.c2.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;
import gpig.c2.data.C2Data;
import gpig.common.config.DetectionsConfig;
import gpig.common.config.LocationsConfig;
import gpig.common.util.Log;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class GUIAdapterInbound {
	
	private DetectionsConfig detectionsConfig;
	private LocationsConfig dcLocationsConfig;
	private C2Data c2Data;
	
	public GUIAdapterInbound(DetectionsConfig detectionsConfig, LocationsConfig dcLocationsConfig, C2Data c2data){
		this.detectionsConfig = detectionsConfig;
		this.dcLocationsConfig = dcLocationsConfig;
		this.c2Data = c2data;
	}
	
	public Detection getDetectionByID(UUID id){
		Detection result = null;
		List<Detection> detections;
		detections = c2Data.getDetections();
		
		if(!detections.isEmpty()){
			for(Detection detection : c2Data.getDetections()){
				if(detection.person.id == id){
					result = detection;
					break;
				}
					
			}
		}
		
		if(result == null){
			result = detectionsConfig.detections.get(1);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * @return List of predefined detection locations.
	 */
	public ArrayList<Location> getDetectionLocationsPredefined(){
		
		ArrayList<Location> locations = new ArrayList<Location>();
		
		for(Detection detection : detectionsConfig.detections){
			locations.add(detection.location);
		}
		return locations;
	}

	
	/**
	 * @return The list of predefined detections.
	 */
	public ArrayList<Detection> getDetectionsPredefined(){
		
		ArrayList<Detection> detections;
		
		detections = detectionsConfig.detections;
		return detections;
	}
	
	
	/**
	 * @return The list of predefined deployment center locations
	 */
	public ArrayList<Location> getDCLocationsPredefined(){
		ArrayList<Location> dcLocations;
		
		dcLocations = dcLocationsConfig.locations;
		
		return dcLocations;
	}
	
	

}
