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

/**
 * The main GUI class.
 * GUI rendering entry point.
 */
public class GUI {
	
	private DetectionsConfig detectionsConfig;
	private LocationsConfig dcLocationsConfig;
	
	private MapPanel mapPanel;//the panel with the background map
	private ActorsPanel detectionPanel;//the panel with the detection locations
	private ActorsPanel dcLocationsPanel;//the panel with the deployment centers locations
	
	//lat long of the background image
	private Location mapTopLeftCorner = new Location(54.049342, -1.253871);
	private Location mapBottomRightCorner = new Location(53.868755, -0.910548);
	
	//size of the background image
	private int mapWidth;
	private int mapHeight;
	
	//data feeds and conversion utils
	private LatLongToXYConverter latLongToXYConverter;
	private AdapterInbound adapterInbound;
	
	//resource URLs
	private URL mapPath = GUI.class.getResource("/YorkMap.png");
	private URL detectionPath = GUI.class.getResource("/Detection.png");
	private URL dcPath = GUI.class.getResource("/DC.png");
	
	public GUI(DetectionsConfig detectionsConfig, LocationsConfig dcLocationsConfig){
		this.detectionsConfig = detectionsConfig;
		this.dcLocationsConfig = dcLocationsConfig;
	}

    /**
     * GUI rendering entry point.
     */
    public void createAndShowGUI() {

    	//the main frame
        JFrame frame = new JFrame("Vendor Lock-in");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        //get the background map
        Image map = new ImageIcon(mapPath).getImage();
        mapWidth = map.getWidth(null);
        mapHeight = map.getHeight(null);
        
        //set up required conversion and the inbound data feed
        latLongToXYConverter = new LatLongToXYConverter(mapWidth, mapHeight, mapTopLeftCorner, mapBottomRightCorner);
        adapterInbound = new AdapterInbound(detectionsConfig,dcLocationsConfig);
        
        ArrayList<Point> detectionsAsPoints = new ArrayList<Point>();
        //get the detections and convert them to xy coordinates
        for(Detection detection : adapterInbound.getDetections()){
        	detectionsAsPoints.add(latLongToXYConverter.convertLocationToPoint(detection.person.location));
        }
        
        ArrayList<Point> dcLocationsAsPoints = new ArrayList<Point>();
        for(Location dcLocation : adapterInbound.GetPredefinedDCLocations()){
        	dcLocationsAsPoints.add(latLongToXYConverter.convertLocationToPoint(dcLocation));
        }
 
        mapPanel = new MapPanel(map);
        detectionPanel = new ActorsPanel(
        		new Dimension(mapWidth,mapHeight),
        		new ImageIcon(detectionPath).getImage(),
        		detectionsAsPoints);
        dcLocationsPanel = new ActorsPanel(
        		new Dimension(mapWidth,mapHeight),
        		new ImageIcon(dcPath).getImage(),
        		dcLocationsAsPoints);

        JLayeredPane mapLayeredPane = new JLayeredPane();
        mapLayeredPane.setPreferredSize(new Dimension(mapWidth,mapHeight));
        
        //put the map in the bottom most layer
        mapLayeredPane.add(mapPanel,new Integer(1),0);
        mapLayeredPane.add(detectionPanel,new Integer(2),0);
        mapLayeredPane.add(dcLocationsPanel,new Integer(3),0);
        
        
        frame.getContentPane().add(mapLayeredPane);
        frame.pack();
        frame.setVisible(true);
        
        frame.setLocationRelativeTo(null);
    }
}
