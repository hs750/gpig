package gpig.c2.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

import gpig.common.data.Detection;
import gpig.common.data.Location;
import gpig.common.config.DetectionsConfig;
import gpig.common.config.LocationsConfig;
import gpig.common.util.Log;

import gpig.c2.gui.unfolding.MapApp;

/**
 * The main GUI class.
 * GUI rendering entry point.
 */
public class GUI {
	
	private DetectionsConfig detectionsConfig;
	private LocationsConfig dcLocationsConfig;
	private AdapterInbound adapterInbound;	
	
	//resource URLs
	private URL detectionPath = GUI.class.getResource("/Detection.png");
	private URL dcPath = GUI.class.getResource("/DC.png");
	
	public GUI(DetectionsConfig detectionsConfig, LocationsConfig dcLocationsConfig){
		this.detectionsConfig = detectionsConfig;
		this.dcLocationsConfig = dcLocationsConfig;
		this.adapterInbound = new AdapterInbound(detectionsConfig, dcLocationsConfig);
	}

    /**
     * GUI rendering entry point.
     */
    public void createAndShowGUI() {

    	//the map frame
        JFrame mapFrame = new JFrame("Vendor Lock-in");
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setResizable(false);
		mapFrame.setSize(600, 600);
		
		JFrame controlFrame = new JFrame("Details");
		controlFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		controlFrame.setResizable(false);
		controlFrame.setSize(400, 600);
		controlFrame.setVisible(true);
		
        
        MapApp map = new MapApp(this);
        mapFrame.add(map);
        
        
        map.init();
        mapFrame.setVisible(true);
        mapFrame.setLocation(100,50);
        controlFrame.setLocation(mapFrame.getLocation().x + mapFrame.getWidth() + controlFrame.getInsets().left*4, mapFrame.getLocation().y);
    }
    
	public URL getDetectionPath() {
		return detectionPath;
	}

	public URL getDcPath() {
		return dcPath;
	}

	public ArrayList<de.fhpotsdam.unfolding.geo.Location> getDetectionLocations() {
		
		ArrayList<de.fhpotsdam.unfolding.geo.Location> unfLocations = new ArrayList<de.fhpotsdam.unfolding.geo.Location>();
		
		for(Location location:adapterInbound.getDetectionLocationsPredefined()){
			unfLocations.add( new de.fhpotsdam.unfolding.geo.Location(location.latitude(),location.longitude()));
		}
		
		
		return unfLocations;
	}

	public ArrayList<de.fhpotsdam.unfolding.geo.Location> getDcLocations() {


		ArrayList<de.fhpotsdam.unfolding.geo.Location> unfLocations = new ArrayList<de.fhpotsdam.unfolding.geo.Location>();
		
		for(Location location:adapterInbound.getDCLocationsPredefined()){
			unfLocations.add( new de.fhpotsdam.unfolding.geo.Location(location.latitude(),location.longitude()));
		}

		return unfLocations;
	}
	
	
}
