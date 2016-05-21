package gpig.c2.gui;

import processing.core.PApplet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.*;

import com.rabbitmq.tools.Tracer.Logger;

import gpig.common.data.ActorType;
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
	
	private AppletRunner appletRunner;
	private GUIAdapterInbound adapterInbound;
	
	//resource URLs
	private URL detectionPath = GUI.class.getResource("/Detection.png");
	private URL dcPath = GUI.class.getResource("/DC.png");
	
	private JFrame detailsFrame;
	private ControlPanel controlPanel;
	private InfoPanel infoPanel;
	
	private Dimension infoPanelSize;
	private Dimension controlPanelSize;
	
	public GUI(GUIAdapterInbound GUIAdapterInbound){
		this.adapterInbound = GUIAdapterInbound;
		
		appletRunner = new AppletRunner(this);
		appletRunner.start();
	}

    /**
     * GUI rendering entry point.
     */
    public void createAndShowGUI() {
    	
    	// The screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        detailsFrame = new JFrame("Details");
        
        // Task bar and other obstruction sizes
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(detailsFrame.getGraphicsConfiguration());
        int width = (int)(screenSize.width / 3 - scnMax.left - scnMax.right); // 1/3 for details
        int height = screenSize.height - scnMax.top - scnMax.bottom; // full height
        
        infoPanelSize = new Dimension(width, (height/3)*2);
        controlPanelSize = new Dimension(width, height/3);
        
		
		detailsFrame.setLayout(new BorderLayout());
		detailsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		detailsFrame.setResizable(false);
		detailsFrame.getContentPane().setBackground(new Color(153, 153, 255));
		detailsFrame.setLocation(screenSize.width-screenSize.width/3, 0);
		detailsFrame.setSize(width, height);
		detailsFrame.setExtendedState(JFrame.MAXIMIZED_VERT);
		
		controlPanel = new ControlPanel(controlPanelSize);
		detailsFrame.add(controlPanel,BorderLayout.NORTH);
		detailsFrame.revalidate();
		detailsFrame.repaint();
		
		detailsFrame.setVisible(true);
    }
    
    public void displayActorInfo(UUID id, ActorType actorType){  	
    	
		if(infoPanel != null){
			detailsFrame.remove(infoPanel);
		}
    	
    	switch(actorType){
    	case PERSON:
    		Detection detection = adapterInbound.getPredefinedDetectionByID(id);
   		
    		infoPanel = new PersonInfoPanel(detection,ActorType.PERSON,detectionPath,infoPanelSize);
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    		
    	case DEPLOYMENT_CENTRE:
    		Location location = adapterInbound.getPredefinedDCLocationByID(id);
   		
    		infoPanel = new DCInfoPanel(id,location,ActorType.DEPLOYMENT_CENTRE,dcPath,infoPanelSize);
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    	}
    	
    }
    
    public void updateSelectedCoordinates(de.fhpotsdam.unfolding.geo.Location geoLocation){
    	controlPanel.selectLocation(new Location(geoLocation.getLat(), geoLocation.getLon()));
		detailsFrame.revalidate();
		detailsFrame.repaint();
    }
    
	public URL getDetectionPath() {
		return detectionPath;
	}

	public URL getDcPath() {
		return dcPath;
	}

	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDetectionLocations() {
		
		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		
		for(Detection detection:adapterInbound.getDetectionsPredefined()){
			unfLocations.put(detection.person.id,
					new de.fhpotsdam.unfolding.geo.Location(detection.person.location.latitude(),detection.person.location.longitude()));
		}
		
		
		return unfLocations;
	}

	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDcLocations() {


		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		HashMap<UUID,Location> adapterData = adapterInbound.getDCLocationsPredefined();
		
		for(UUID id : adapterData.keySet()){
			unfLocations.put(id,
					new de.fhpotsdam.unfolding.geo.Location(adapterData.get(id).latitude(),adapterData.get(id).longitude()));
		}

		return unfLocations;
	}
	
	public int getBottomInset(){
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(detailsFrame.getGraphicsConfiguration());
		return scnMax.bottom;
	}
	
	
}
