package gpig.c2.gui;

import java.util.ArrayList;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;

import gpig.common.config.LocationsConfig;
import gpig.common.util.Log;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class AdapterInbound {
	private ArrayList<Detection> detections;
	private LocationsConfig dcLocationsConfig;
	
	public AdapterInbound(LocationsConfig dcLocationsConfig){
		this.dcLocationsConfig = dcLocationsConfig;
	}
	
	public ArrayList<Detection> getDetections(){
		
		detections = new ArrayList();
		detections.add(new Detection(new Location(53.955130, -1.070496), new Person(PersonType.CIVILIAN)));
		detections.add(new Detection(new Location(53.965110, -1.083042), new Person(PersonType.CIVILIAN)));
		return detections;
	}
	
	public ArrayList<Location> GetPredefinedDCLocations(){
		ArrayList<Location> dcLocations;
		
		dcLocations = dcLocationsConfig.locations;
		Log.info("mcreadsize" + dcLocations.size());
		
		return dcLocations;
	}

}
