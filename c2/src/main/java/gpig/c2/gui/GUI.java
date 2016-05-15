package gpig.c2.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import gpig.common.data.Detection;
import gpig.common.data.Location;

/**
 * The main GUI class.
 * GUI rendering entry point.
 */
public class GUI {
	
	private MapPanel mapPanel;//the panel with the background map
	private DetectionsPanel detectionPanel;//the panel with the detection locations
	
	//lat long of the background image
	private Location mapTopLeftCorner = new Location(54.049342, -1.253871);
	private Location mapBottomRightCorner = new Location(53.868755, -0.910548);
	
	//size of the background image
	private int mapWidth;
	private int mapHeight;
	
	//data feeds and conversion utils
	private LatLongToXYConverter latLongToXYConverter;
	private AdapterInbound adapterInbound;
	
	//resource paths
	private String mapPath = "src/main/resources/YorkMap.png";
	private String detectionPath = "src/main/resources/detection.png";
	
	

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
        adapterInbound = new AdapterInbound();
        ArrayList<Point> detectionsAsPoints = new ArrayList<Point>();
        
        //get the detections and convert them to xy coordinates
        for(Detection detection : adapterInbound.getDetections())
        	detectionsAsPoints.add(latLongToXYConverter.convertLocationToPoint(detection.location));
 
        mapPanel = new MapPanel(map);
        detectionPanel = new DetectionsPanel(
        		new Dimension(mapWidth,mapHeight),
        		new ImageIcon(detectionPath).getImage(),
        		detectionsAsPoints);

        JLayeredPane mapLayeredPane = new JLayeredPane();
        mapLayeredPane.setPreferredSize(new Dimension(mapWidth,mapHeight));
        
        //put the map in the bottom most layer
        mapLayeredPane.add(mapPanel,new Integer(1),0);
        mapLayeredPane.add(detectionPanel,new Integer(2),0);
        
        
        frame.getContentPane().add(mapLayeredPane);
        frame.pack();
        frame.setVisible(true);
        
        frame.setLocationRelativeTo(null);
    }
}
