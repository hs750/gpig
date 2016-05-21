package gpig.c2.gui.unfolding;

import processing.core.*;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.UUID;

import javax.swing.JFrame;

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
	
	//different providers that can be switched between
	//at runtime
	private AbstractMapProvider provider1;
	private AbstractMapProvider provider2;
	private AbstractMapProvider provider3;
	
	//map size
	private int w;
	private int h;
	
	private int imageMarkersWidth;
	private int imageMarkersHeight;
	
	public MapApp(GUI gui) {
		this.gui = gui;
	}
	
	public void setup() {
		
		// The screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Task bar and other obstruction sizes
        // Papplet impl of insets is wrong - taskbar is not included
        Insets scnMax = frame.getInsets(); 
        
        scnMax.set(scnMax.top, scnMax.left, gui.getBottomInset(), scnMax.right);
        this.w = (int)(screenSize.width / 3 * 2 - scnMax.right - scnMax.left); // 2/3 for map
        this.h = screenSize.height - scnMax.bottom - scnMax.top; // full height
        
        //height is used as width to keep the markers square
        this.imageMarkersWidth = height/28;
        this.imageMarkersHeight = height/28;
        
        //the default applet's frame
        //frame.setLocation(scnMax.right, scnMax.top);
        frame.setLocation(0,0);
        
        frame.setExtendedState(JFrame.MAXIMIZED_VERT);
        size(this.w, this.h, OPENGL);
        
		
		provider1 = new Microsoft.RoadProvider();
		provider2 = new Microsoft.AerialProvider();
		provider3 = new Microsoft.HybridProvider();
		
		Location centerLocation = new Location(53.957847, -1.083012f);
		map = new UnfoldingMap(this,new Microsoft.RoadProvider());
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
			ImageMarker imgMrk = new ImageMarker(
					imageMarkersWidth,
					imageMarkersHeight,
					id,
					idmap.get(id),ActorType.PERSON,
					loadImage(gui.getDetectionPath().toString())
					);
			map.addMarker(imgMrk);
		}
		
		//add dcs
		idmap = gui.getDcLocations();
		
		for(UUID id : idmap.keySet()){
			
			
			ImageMarker imgMrk = new ImageMarker(
					imageMarkersWidth,
					imageMarkersHeight,
					id, idmap.get(id),
					ActorType.DEPLOYMENT_CENTRE,
					loadImage(gui.getDcPath().toString())
					);
			map.addMarker(imgMrk);
		}
		
		map.draw();
		Location location = map.getLocation(mouseX, mouseY);
	    fill(0);
	    text(String.format ("%.6f", location.getLat()) + ", " + String.format ("%.6f", location.getLon()), mouseX, mouseY);
	}
	
	// event handlers
	public void mouseClicked(){
		
		//display actor info based on the clicked marker if any
		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
	    
		if (hitMarker != null && ImageMarker.class.equals(hitMarker.getClass())) {
			ImageMarker theMarker = (ImageMarker) hitMarker;

	        gui.displayActorInfo(theMarker.getActorId(), theMarker.getActorType());
	    }else{
	    	//send the selected coordinates
	    	gui.updateSelectedCoordinates(map.getLocation(mouseX, mouseY));
	    }
	}
	
	/* 
	 * Switch between map providers at runtime
	 */
	public void keyPressed() {
	    if (key == '1') {
	        map.mapDisplay.setProvider(provider1);
	    } else if (key == '2') {
	        map.mapDisplay.setProvider(provider2);
	    } else if (key == '3') {
	        map.mapDisplay.setProvider(provider3);
	    }
	}
}
