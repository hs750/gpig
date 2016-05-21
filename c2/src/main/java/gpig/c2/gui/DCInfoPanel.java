package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import java.util.UUID;

import gpig.common.data.ActorType;
import gpig.common.data.Location;

/**
 *	Displays information about a DC
 */
public class DCInfoPanel extends InfoPanel{
	
	private Location location;
	
	public DCInfoPanel(UUID id, Location location, ActorType actorType, URL imageURL, Dimension size) {
		super(actorType,imageURL, size);
		this.location = location;

		//Set parent field contents
		actorIdL.setText(""+id);
		actorTypeL.setText("Deployment Center");
		actorLatL.setText(""+String.format ("%.6f", location.latitude()));
		actorLonL.setText(""+String.format ("%.6f", location.longitude()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

}
