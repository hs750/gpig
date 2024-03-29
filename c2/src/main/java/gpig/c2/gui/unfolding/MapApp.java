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
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import gpig.c2.gui.GUI;
import gpig.common.data.ActorType;
import gpig.common.data.Constants;
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
		
		//add actors
		HashMap<UUID,Location> idmap;
		
		if(gui.isShowUndeliveredDetections()){
			idmap = gui.getDetectionLocationsUndelivered();
			for(UUID id : idmap.keySet()){
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id,
						idmap.get(id),ActorType.PERSON,
						loadImage(gui.getUndeliveredDetectionURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDeliveredDetections()){
		idmap = gui.getDetectionLocationsDelivered();
		for(UUID id : idmap.keySet()){
			ImageMarker imgMrk = new ImageMarker(
					imageMarkersWidth,
					imageMarkersHeight,
					id,
					idmap.get(id),ActorType.PERSON,
					loadImage(gui.getDeliveredDetectionURL().toString())
					);
			map.addMarker(imgMrk);
		}
		}
		
		if(gui.isShowUndeliveredDetections() && gui.isShowExternalDetections()){
		
		idmap = gui.getOtherTeamLocationsUndelivered();
        for(UUID id : idmap.keySet()){
            ImageMarker imgMrk = new ImageMarker(
                    imageMarkersWidth,
                    imageMarkersHeight,
                    id,
                    idmap.get(id),ActorType.PERSON,
                    loadImage(gui.getOtherTeamDetectionURL().toString())
                    );
            map.addMarker(imgMrk);
        }
		}
		
		if(gui.isShowDeliveredDetections() && gui.isShowExternalDetections()){
		
		idmap = gui.getOtherTeamLocationsDelivered();
        for(UUID id : idmap.keySet()){
            ImageMarker imgMrk = new ImageMarker(
                    imageMarkersWidth,
                    imageMarkersHeight,
                    id,
                    idmap.get(id),ActorType.PERSON,
                    loadImage(gui.getOtherTeamDeliveredURL().toString())
                    );
            map.addMarker(imgMrk);
        }
		}
		
		//add dcs
		if(gui.isShowDCs()){
		
			idmap = gui.getDcLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DEPLOYMENT_CENTRE,
						loadImage(gui.getDcURL().toString())
						);
				map.addMarker(imgMrk);
				
				
				
				ScreenPosition markerPosition = imgMrk.getScreenPosition(map);
				ScreenPosition referencePosition = map.getScreenPosition(
						GeoUtils.getDestinationLocation(
								imgMrk.getLocation(),
								90,
								(float) Constants.DEPLOYMENT_SEARCH_RADIUS.value())
						);
				float screenDiff = Math.abs(referencePosition.x - markerPosition.x);
				
				
				strokeWeight(5);
				stroke(67, 211, 227, 50);
				noFill();
				ellipse(markerPosition.x, markerPosition.y, screenDiff*2, screenDiff*2);
			}
		}
		
		//add drones
		if(gui.isShowDetectionDrones()){
			
			idmap = gui.getDetectionDroneLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DETECTION_DRONE,
						loadImage(gui.getDetectionDroneNormalURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDetectionDrones()){
			
			idmap = gui.getFaultyDetectionDroneLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DETECTION_DRONE,
						loadImage(gui.getDetectionDroneSoftFailURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDetectionDrones()){
		
			idmap = gui.getCrashedDetectionDroneLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DETECTION_DRONE,
						loadImage(gui.getDetectionDroneHardFailURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDeliveryDrones()){
		
			idmap = gui.getDeliveryDroneLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DELIVERY_DRONE,
						loadImage(gui.getDeliveryDroneNormalURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDeliveryDrones()){
		
			idmap = gui.getFaultyDeliveryDroneLocations();
			for(UUID id : idmap.keySet()){
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DELIVERY_DRONE,
						loadImage(gui.getDeliveryDroneSoftFailURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		if(gui.isShowDeliveryDrones()){
			
			idmap = gui.getCrashedDeliveryDroneLocations();
			for(UUID id : idmap.keySet()){
				
				
				ImageMarker imgMrk = new ImageMarker(
						imageMarkersWidth,
						imageMarkersHeight,
						id, idmap.get(id),
						ActorType.DELIVERY_DRONE,
						loadImage(gui.getDeliveryDroneHardFailURL().toString())
						);
				map.addMarker(imgMrk);
			}
		}
		
		
		map.draw();
		
		gui.updateActorInfo();
		
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

			
			javax.swing.SwingUtilities.invokeLater(() -> {
				gui.displayActorInfo(theMarker.getActorId(), theMarker.getActorType());
			 });
	    }else{
	    	//send the selected coordinates
	    	
	    	javax.swing.SwingUtilities.invokeLater(() -> {
	    	gui.updateSelectedCoordinates(map.getLocation(mouseX, mouseY));
	    	});
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
