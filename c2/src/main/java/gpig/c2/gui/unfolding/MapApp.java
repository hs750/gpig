package gpig.c2.gui.unfolding;

import processing.core.*;

import java.net.URL;
import java.util.UUID;
import java.util.HashMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import gpig.c2.gui.GUI;
import gpig.common.data.ActorType;
import de.fhpotsdam.unfolding.providers.*;


/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class MapApp extends PApplet {
	
	private GUI gui;
	private UnfoldingMap map;
	
	//used to reset the mouse position if it gets misaligned
	private AbstractMapProvider provider0;
	
	private AbstractMapProvider provider1;
	private AbstractMapProvider provider2;
	private AbstractMapProvider provider3;
	
	
	private Location berlinLocation = new Location(53.955130f, -1.070496f);
	private Location veniceLocation = new Location(53.965110f, -1.083042f);

	public MapApp(GUI gui) {
		this.gui = gui;
	}
	
	
	
	public void setup() {
		
		size(600, 600, OPENGL);
		
		 provider1 = new Microsoft.RoadProvider();
		 provider2 = new Microsoft.AerialProvider();
		 provider3 = new Microsoft.HybridProvider();
		 
		 provider0 = new Google.GoogleMapProvider();
		
		Location centerLocation = new Location(53.957847, -1.083012f);
		map = new UnfoldingMap(this,new Google.GoogleMapProvider());
		//map = new UnfoldingMap(this,new Google.GoogleMapProvider());
		map.zoomAndPanTo(10, centerLocation);
		//map.setPanningRestriction(centerLocation, 30f);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
	}

	public void draw() {
		
		map.getMarkerManager(0).clearMarkers();
		
		//add detections
		HashMap<UUID,Location> idmap;
		idmap = gui.getDetectionLocations();
		
		for(UUID id : idmap.keySet()){
			ImageMarker imgMrk = new ImageMarker(id, idmap.get(id),ActorType.PERSON, loadImage(gui.getDetectionPath().toString()));
			map.addMarker(imgMrk);
		}
		
		//add dcs
		idmap = gui.getDcLocations();
		
		for(UUID id : idmap.keySet()){
			ImageMarker imgMrk = new ImageMarker(id, idmap.get(id), ActorType.DEPLOYMENT_CENTRE, loadImage(gui.getDcPath().toString()));
			map.addMarker(imgMrk);
		}
		
		map.draw();
		Location location = map.getLocation(mouseX, mouseY);
	    fill(0);
	    text(location.getLat() + ", " + location.getLon(), mouseX, mouseY);
	}
	
	// event handlers
	public void mouseClicked(){
		
		
		
		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
	    
		if (hitMarker != null && ImageMarker.class.equals(hitMarker.getClass())) {
			ImageMarker theMarker = (ImageMarker) hitMarker;

	        gui.displayActorInfo(theMarker.getActorId(), theMarker.getActorType());
	    }
	}
	
	public void keyPressed() {
	    if (key == '1') {
	        map.mapDisplay.setProvider(provider1);
	    } else if (key == '2') {
	        map.mapDisplay.setProvider(provider2);
	    } else if (key == '3') {
	        map.mapDisplay.setProvider(provider3);
	    } else if (key == '0') {
	        map.mapDisplay.setProvider(provider0);
	    }
	}
}
