package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gpig.common.data.Detection;

public class PersonInfoPanel extends JPanel{
	
	private Detection detection;
	private URL imageURL;
	
	public PersonInfoPanel(Detection detection, URL imageURL, Dimension size) {
		super();
		this.detection = detection;
		this.imageURL = imageURL;
		
		this.setSize(size);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
		Font font = new Font("Console", Font.BOLD, 15);
		
		JLabel img = new JLabel();
		img.setIcon(new ImageIcon(imageURL));
		JLabel id = new JLabel("ID: "+detection.person.id);
		JLabel type = new JLabel("Type: "+detection.person.type);
		JLabel lat = new JLabel("Latitude: "+detection.location.latitude());
		JLabel lon = new JLabel("Longtitude: "+detection.location.longitude());
		
		id.setFont(font);
		type.setFont(font);
		lat.setFont(font);
		lon.setFont(font);
		
		add(img);
		add(id);
		add(type);
		add(lat);
		add(lon);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

}
