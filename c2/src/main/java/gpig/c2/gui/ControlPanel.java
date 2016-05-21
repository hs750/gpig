package gpig.c2.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import gpig.common.data.ActorType;
import gpig.common.data.Detection;
import gpig.common.data.Location;

/**
 *	Displays information about an actor
 */
public class ControlPanel extends JPanel{

	protected Font fieldNameFont;
	protected Font fieldContentsFont;
	protected GridBagConstraints c;
	
	//Field names
	protected JLabel selectedLatLN;
	protected JLabel selectedLonLN;
	
	//Field contents
	protected JLabel selectedLatL;
	protected JLabel selectedLonL;
	
	
	public ControlPanel(Dimension size) {
		super();
		this.setSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(153, 153, 255));
		this.setBorder(new MatteBorder(0,0,1,0,Color.BLACK));
		
		fieldNameFont = new Font("Console", Font.BOLD, 15);
		fieldContentsFont = new Font("Console", Font.PLAIN, 15);
		
		addComponents();
	}
	
	private void addComponents(){
		
		c = new GridBagConstraints();		
		

		
		//Field names
		selectedLatLN = new JLabel("Selected Latitude: ");
		selectedLonLN = new JLabel("Selected Longtitude: ");
		
		//Field contents
		selectedLatL = new JLabel("None");
		selectedLonL = new JLabel("None");
		
		//fonts
		selectedLatLN.setFont(fieldNameFont);
		selectedLonLN.setFont(fieldNameFont);
		
		selectedLatL.setFont(fieldContentsFont);
		selectedLonL.setFont(fieldContentsFont);

		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		//cst.weightx = 0.3;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(selectedLatLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		//cst.weightx = 0.7;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
		add(selectedLatL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		//cst.weightx = 0.3;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(selectedLonLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		//cst.weightx = 0.7;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 1;
		add(selectedLonL,c);
		
	}
	
	public void selectLocation(Location location){
		selectedLatL.setText(""+String.format ("%.6f", location.latitude()));
		selectedLonL.setText(""+String.format ("%.6f", location.longitude()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
