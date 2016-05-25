package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import gpig.common.data.ActorType;
import gpig.common.data.Detection;


/**
 *	Displays information about a person
 */
public class PersonInfoPanel extends InfoPanel{
	
	private Detection detection;
	private Boolean deliveredTo;
	private URL detectionImageURL;
	private JLabel detectionImageL;
	
	public PersonInfoPanel(Detection detection, boolean deliveredTo,URL detectionURL, ActorType actorType, URL imageURL, Dimension size) {
		super(actorType,imageURL, size);
		this.detection = detection;
		this.deliveredTo = deliveredTo;
		this.detectionImageURL = detectionURL;
		
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
		
		addComponents();
	
	}
	
	private void addComponents(){
		
		detectionImageL = new JLabel("", SwingConstants.CENTER);
		
		BufferedImage detectionImageBuf = null;
		try {
			detectionImageBuf = ImageIO.read(detectionImageURL);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		if(detectionImageBuf != null){
			
			Image detectionImage = detectionImageBuf.getScaledInstance((this.getWidth()/3)*2, this.getHeight()/3,
			        Image.SCALE_SMOOTH);
			
			detectionImageL.setIcon(new ImageIcon(detectionImage));
			
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.weighty = 0.5;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = c.gridy+1;
			add(detectionImageL,c);
		}
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

}
