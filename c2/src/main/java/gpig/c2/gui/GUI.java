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
	
	JFrame detailsFrame;
	
	public GUI(GUIAdapterInbound GUIAdapterInbound){
		this.adapterInbound = GUIAdapterInbound;
		
		appletRunner = new AppletRunner(this);
		appletRunner.start();
	}

    /**
     * GUI rendering entry point.
     */
    public void createAndShowGUI() {
		
		detailsFrame = new JFrame("Details");
		detailsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		detailsFrame.setResizable(false);
		detailsFrame.getContentPane().setBackground(new Color(153, 153, 255));
		detailsFrame.setSize(400, 600);
		detailsFrame.setVisible(true);
            }
    
    public void displayActorInfo(UUID id, ActorType actorType){
    	detailsFrame.getContentPane().removeAll();
    	
    	switch(actorType){
    	case PERSON:
    		Detection detection = adapterInbound.getPredefinedDetectionByID(id);
   		
    		detailsFrame.getContentPane().add(new PersonInfoPanel(detection,ActorType.PERSON,detectionPath,detailsFrame.getSize()));
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    		
    	case DEPLOYMENT_CENTRE:
    		Location location = adapterInbound.getPredefinedDCLocationByID(id);
   		
    		detailsFrame.getContentPane().add(new DCInfoPanel(id,location,ActorType.DEPLOYMENT_CENTRE,dcPath,detailsFrame.getSize()));
    		detailsFrame.revalidate();
    		detailsFrame.repaint();
    		break;
    	}
    	
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
					new de.fhpotsdam.unfolding.geo.Location(detection.location.latitude(),detection.location.longitude()));
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
	
	
}
