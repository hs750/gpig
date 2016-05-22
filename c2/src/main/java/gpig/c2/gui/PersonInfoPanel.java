package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;

import gpig.common.data.ActorType;
import gpig.common.data.Detection;


/**
 *	Displays information about a person
 */
public class PersonInfoPanel extends InfoPanel{
	
	private Detection detection;
	private Boolean deliveredTo;
	
	public PersonInfoPanel(Detection detection, boolean deliveredTo, ActorType actorType, URL imageURL, Dimension size) {
		super(actorType,imageURL, size);
		this.detection = detection;
		this.deliveredTo = deliveredTo;
		
		//Set parent field contents
		actorIdL.setText(""+detection.person.id);
		actorTypeL.setText("Flood Victim");
		
		if(deliveredTo){
			actorStateL.setText("DELIVERED TO");
		}else{
			actorStateL.setText("NOT DELIVERED TO");
		}
		
		actorLatL.setText(""+String.format ("%.6f", detection.person.location.latitude()));
		actorLonL.setText(""+String.format ("%.6f", detection.person.location.longitude()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

}
