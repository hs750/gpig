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

    	//the main frame
        JFrame mainFrame = new JFrame("Vendor Lock-in");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
   
		mainFrame.setSize(1200, 600);
		
		BorderLayout mainFrameLayout = new BorderLayout();
		
        mainFrame.setLayout(new BorderLayout());
        
        JPanel mapPanel = new JPanel();
        mapPanel.setSize(800, 600);
        MapApp map = new MapApp(this);
        mapPanel.add(map);
        mainFrame.add(mapPanel,BorderLayout.WEST);
        
        map.init();

        
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
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
