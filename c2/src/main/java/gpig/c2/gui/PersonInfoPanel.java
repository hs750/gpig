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
	
	public PersonInfoPanel(Detection detection, ActorType actorType, URL imageURL, Dimension size) {
		super(actorType,imageURL, size);
		this.detection = detection;

		//Set parent field contents
		actorIdL.setText(""+detection.person.id);
		actorTypeL.setText("Flood Victim");
		actorLatL.setText(""+String.format ("%.6f", detection.location.latitude()));
		actorLonL.setText(""+String.format ("%.6f", detection.location.longitude()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

}
