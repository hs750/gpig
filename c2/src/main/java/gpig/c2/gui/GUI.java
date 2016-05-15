package gpig.c2.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import gpig.common.data.Detection;
import gpig.common.data.Location;

public class GUI {
	
	private MapPanel mapPanel;
	private DetectionsPanel detectionPanel;
	
	private Location mapTopLeftCorner = new Location(54.049342, -1.253871);
	private Location mapBottomRightCorner = new Location(53.868755, -0.910548);
	private int mapWidth;
	private int mapHeight;
	
	private LatLongToXYConverter latLongToXYConverter;
	private AdapterInbound adapterInbound;
	

    public void createAndShowGUI() {

        JFrame frame = new JFrame("MainMapFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(589, 528);
        frame.setResizable(false);
        
        Image map = new ImageIcon("src/main/resources/YorkMap.png").getImage();
        mapWidth = map.getWidth(null);
        mapHeight = map.getHeight(null);
        
        
        latLongToXYConverter = new LatLongToXYConverter(mapWidth, mapHeight, mapTopLeftCorner, mapBottomRightCorner);
        adapterInbound = new AdapterInbound();
        ArrayList<Point> detectionsAsPoints = new ArrayList();
        
        for(Detection detection : adapterInbound.getDetections())
        	detectionsAsPoints.add(latLongToXYConverter.convertLocationToPoint(detection.location));
 
        mapPanel = new MapPanel(map);
        detectionPanel = new DetectionsPanel(
        		new Dimension(mapWidth,mapHeight),
        		new ImageIcon("src/main/resources/detection.png").getImage(),
        		detectionsAsPoints);

        
        JLayeredPane mapLayeredPane = new JLayeredPane();
        mapLayeredPane.setPreferredSize(new Dimension(mapWidth,mapHeight));
        
        //put the map in the bottom most layer
        mapLayeredPane.add(mapPanel,new Integer(10),0);
        mapLayeredPane.add(detectionPanel,new Integer(10),0);
        
        
        frame.getContentPane().add(mapLayeredPane);
        //frame.getContentPane().add(mapPanel);
        //frame.getContentPane().add(detectionPanel);
        frame.pack();
        frame.setVisible(true);
        
        frame.setLocationRelativeTo(null);
    }
    
    protected void placeObjectOnMap(Point location, Image object){

    	if(location.getX()>=mapPanel.getWidth() || location.getY()>=mapPanel.getHeight())
    		System.out.println("GUY: object out of map bounds");
    	
    	
    }
}
