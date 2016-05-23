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
import gpig.common.data.DroneState;
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
	private GUIAdapterOutbound adapterOutbound;
	
	//resource URLs
	private URL undeliveredDetectionURL = GUI.class.getResource("/DetectionUndelivered.png");
	private URL deliveredDetectionURL = GUI.class.getResource("/DetectionDelivered.png");
	private URL dcURL = GUI.class.getResource("/DC.png");
	private URL detectionDroneNormalURL = GUI.class.getResource("/DetectionDroneNormal.png");
	private URL deliveryDroneNormalURL = GUI.class.getResource("/DeliveryDroneNormal.png");
	private URL detectionDroneSoftFailURL = GUI.class.getResource("/DetectionDroneSoftFail.png");
	private URL deliveryDroneSoftFailURL = GUI.class.getResource("/DelivereryDroneSoftFail.png");
	private URL detectionDroneHardFailURL = GUI.class.getResource("/DetectionDroneHardFail.png");
	private URL deliveryDroneHardFailURL = GUI.class.getResource("/DeliveryDroneHardFail.png");
	
	
	private JFrame detailsFrame;
	private ControlPanel controlPanel;
	private InfoPanel infoPanel;
	
	private Dimension infoPanelSize;
	private Dimension controlPanelSize;
	
	public GUI(GUIAdapterInbound adapterInbound, GUIAdapterOutbound adapterOutbound){
		this.adapterInbound = adapterInbound;
		this.adapterOutbound = adapterOutbound;
		
		appletRunner = new AppletRunner(this);
		appletRunner.start();
	}

    /**
     * GUI rendering entry point.
     */
    public void createAndShowGUI() {
    	
    	/*
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	// The screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        detailsFrame = new JFrame("Details");
        
        // Task bar and other obstruction sizes
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(detailsFrame.getGraphicsConfiguration());
        int width = (int)(screenSize.width / 3 - scnMax.left - scnMax.right); // 1/3 for details
        int height = screenSize.height - scnMax.top - scnMax.bottom; // full height
        
        infoPanelSize = new Dimension(width, (height/5)*4);
        controlPanelSize = new Dimension(width, height/5);
        
		
		detailsFrame.setLayout(new BorderLayout());
		detailsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		detailsFrame.setResizable(false);
		detailsFrame.getContentPane().setBackground(new Color(153, 153, 255));
		detailsFrame.setLocation(screenSize.width-screenSize.width/3, 0);
		detailsFrame.setSize(width, height);
		detailsFrame.setExtendedState(JFrame.MAXIMIZED_VERT);
		
		controlPanel = new ControlPanel(this,controlPanelSize);
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
    		Detection detection = adapterInbound.getDetectionByID(id);
   		
    		if(adapterInbound.hasBeenDeliveredTo(id)){
    			infoPanel = new PersonInfoPanel(detection,true,ActorType.PERSON,deliveredDetectionURL,infoPanelSize);
    		}else{
    			infoPanel = new PersonInfoPanel(detection,false,ActorType.PERSON,undeliveredDetectionURL,infoPanelSize);
    		}
    		
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    	
    		
    	case DEPLOYMENT_CENTRE:
    		Location dcLocation = adapterInbound.getDCLocationByID(id);
   		
    		infoPanel = new DCInfoPanel(id,dcLocation,ActorType.DEPLOYMENT_CENTRE,dcURL,infoPanelSize);
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    		
    		
    	case DETECTION_DRONE:
    		Location dtdLocation = adapterInbound.getDetectionDroneLocationByID(id);
    		DroneState dtdState = adapterInbound.getDetectionDroneStateByID(id);
   		
    		infoPanel = new DetectionDroneInfoPanel(id,dtdLocation,dtdState,ActorType.DETECTION_DRONE,detectionDroneNormalURL,infoPanelSize);
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    		
    	case DELIVERY_DRONE:
    		Location dldLocation = adapterInbound.getDeliveryDroneLocationByID(id);
    		DroneState dldState = adapterInbound.getDeliveryDroneStateByID(id);
    		
    		infoPanel = new DeliveryDroneInfoPanel(id,dldLocation,dldState,ActorType.DELIVERY_DRONE,deliveryDroneNormalURL,infoPanelSize);
    		detailsFrame.getContentPane().add(infoPanel,BorderLayout.CENTER);
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    	}
    	
    }
    
    //control panel methods
    public void updateSelectedCoordinates(de.fhpotsdam.unfolding.geo.Location geoLocation){
    	controlPanel.selectLocation(new Location(geoLocation.getLat(), geoLocation.getLon()));
    	
    	if(adapterInbound.canDeploy()){
    		controlPanel.enableDeployment();
    	}else{
    		controlPanel.enableRedeployment();
    	}
    	
		detailsFrame.revalidate();
		detailsFrame.repaint();
    }
    
    public void requestDeploy(Location location){
    	adapterOutbound.DeployRedeploy(location);
    }
    
    public void requestRedeploy(Location location){
    	adapterOutbound.DeployRedeploy(location);
    }

    
    //initial draw data for unfoding maps
	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDetectionLocationsUndelivered() {
		
		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		
		for(Detection detection:adapterInbound.getUndeliveredDetections()){
			unfLocations.put(detection.person.id,
					new de.fhpotsdam.unfolding.geo.Location(detection.person.location.latitude(),detection.person.location.longitude()));
		}
		
		
		return unfLocations;
	}
	
	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDetectionLocationsDelivered() {
		
		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		
		for(Detection detection:adapterInbound.getDeliveredDetections()){
			unfLocations.put(detection.person.id,
					new de.fhpotsdam.unfolding.geo.Location(detection.person.location.latitude(),detection.person.location.longitude()));
		}
		
		
		return unfLocations;
	}

	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDcLocations() {


		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		HashMap<UUID,Location> adapterData = adapterInbound.getDCLocations();
		
		for(UUID id : adapterData.keySet()){
			unfLocations.put(id,
					new de.fhpotsdam.unfolding.geo.Location(adapterData.get(id).latitude(),adapterData.get(id).longitude()));
		}

		return unfLocations;
	}
	
	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDeliveryDroneLocations() {


		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		HashMap<UUID,Location> adapterData = adapterInbound.getDeliveryDroneLocations();
		
		for(UUID id : adapterData.keySet()){
			unfLocations.put(id,
					new de.fhpotsdam.unfolding.geo.Location(adapterData.get(id).latitude(),adapterData.get(id).longitude()));
		}

		return unfLocations;
	}
	
	public HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> getDetectionDroneLocations() {


		HashMap<UUID,de.fhpotsdam.unfolding.geo.Location> unfLocations = new HashMap<UUID,de.fhpotsdam.unfolding.geo.Location>();
		HashMap<UUID,Location> adapterData = adapterInbound.getDetectionDroneLocations();
		
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

	//image url getters
	public URL getDcURL() {
		return dcURL;
	}
	
	public URL getUndeliveredDetectionURL() {
		return undeliveredDetectionURL;
	}
	
	public URL getDeliveredDetectionURL() {
		return deliveredDetectionURL;
	}

	public URL getDetectionDroneNormalURL() {
		return detectionDroneNormalURL;
	}

	public URL getDeliveryDroneNormalURL() {
		return deliveryDroneNormalURL;
	}

	public URL getDetectionDroneSoftFailURL() {
		return detectionDroneSoftFailURL;
	}

	public URL getDeliveryDroneSoftFailURL() {
		return deliveryDroneSoftFailURL;
	}

	public URL getDetectionDroneHardFailURL() {
		return detectionDroneHardFailURL;
	}

	public URL getDeliveryDroneHardFailURL() {
		return deliveryDroneHardFailURL;
	}
	
	public void setAdapterInbound(GUIAdapterInbound adapterInbound) {
		this.adapterInbound = adapterInbound;
	}
	
}
