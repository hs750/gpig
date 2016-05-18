package gpig.c2.gui.unfolding;

import processing.core.*;

import java.net.URL;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import gpig.c2.gui.GUI;
import de.fhpotsdam.unfolding.providers.*;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 */
public class MapApp extends PApplet {
	
	private GUI gui;
	private UnfoldingMap map;
	private Location berlinLocation = new Location(53.955130f, -1.070496f);
	private Location veniceLocation = new Location(53.965110f, -1.083042f);

	public MapApp(GUI gui) {
		this.gui = gui;
	}
	
	
	
	public void setup() {
		
		size(600, 600, OPENGL);
		
		Location centerLocation = new Location(53.957847, -1.083012f);
		//map = new UnfoldingMap(this,new Google.GoogleMapProvider());
		//map = new UnfoldingMap(this,new GeoMapApp.TopologicalGeoMapProvider());
		map = new UnfoldingMap(this,new Microsoft.RoadProvider());
		//map = new UnfoldingMap(this,new Microsoft.HybridProvider());
		map.zoomAndPanTo(10, centerLocation);
		//map.setPanningRestriction(centerLocation, 30f);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
	}

	public void draw() {
		
		//add detections
				for(Location location:gui.getDetectionLocations()){
					ImageMarker imgMrk = new ImageMarker(location, loadImage(gui.getDetectionPath().toString()));
					map.addMarker(imgMrk);
				}
				
				//add dcs
				for(Location location:gui.getDcLocations()){
					ImageMarker imgMrk = new ImageMarker(location, loadImage(gui.getDcPath().toString()));
					map.addMarker(imgMrk);
				}
		
		map.draw();
	}
}
