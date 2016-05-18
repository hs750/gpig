package gpig.c2.gui;

import java.util.ArrayList;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;
import gpig.common.config.DetectionsConfig;
import gpig.common.config.LocationsConfig;
import gpig.common.util.Log;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class AdapterInbound {
	
	private DetectionsConfig detectionsConfig;
	private LocationsConfig dcLocationsConfig;
	
	public AdapterInbound(DetectionsConfig detectionsConfig, LocationsConfig dcLocationsConfig){
		this.detectionsConfig = detectionsConfig;
		this.dcLocationsConfig = dcLocationsConfig;
		
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
