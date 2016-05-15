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
	
	public ArrayList<Detection> getDetections(){
		
		ArrayList<Detection> detections;
		
		detections = detectionsConfig.detections;
		return detections;
	}
	
	public ArrayList<Location> GetPredefinedDCLocations(){
		ArrayList<Location> dcLocations;
		
		dcLocations = dcLocationsConfig.locations;
		
		return dcLocations;
	}

}
