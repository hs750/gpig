package gpig.c2.gui;

import java.util.ArrayList;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.data.Person;
import gpig.common.data.Person.PersonType;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class AdapterInbound {
	private ArrayList<Detection> detections;
	
	public ArrayList<Detection> getDetections(){
		
		detections = new ArrayList();
		detections.add(new Detection(new Location(53.956380, -1.069473), new Person(PersonType.CIVILIAN)));
		detections.add(new Detection(new Location(53.965154, -1.083100), new Person(PersonType.CIVILIAN)));
		return detections;
	}

}
