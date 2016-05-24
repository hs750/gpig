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

import gpig.common.data.ActorType;
import gpig.common.data.Detection;

/**
 *	Displays information about an actor
 */
public class InfoPanel extends JPanel{

	protected Font fieldNameFont;
	protected Font fieldContentsFont;
	protected GridBagConstraints c;
	
	protected ActorType actorType;
	
	//Actor image
	protected URL actorImageURL;
	protected JLabel actorImage;
	
	//Field names
	protected JLabel actorIdLN;
	protected JLabel actorTypeLN;
	protected JLabel actorStateLN;
	protected JLabel actorLatLN;
	protected JLabel actorLonLN;
	
	//Field contents
	protected JLabel actorIdL;
	protected JLabel actorTypeL;
	protected JLabel actorStateL;
	protected JLabel actorLatL;
	protected JLabel actorLonL;
	
	
	public InfoPanel(ActorType actorType, URL actorImageURL, Dimension size) {
		super();
		this.actorType = actorType;
		this.actorImageURL = actorImageURL;
		this.setSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(153, 153, 255));
		
		fieldNameFont = new Font("Console", Font.BOLD, 15);
		fieldContentsFont = new Font("Console", Font.PLAIN, 15);
		
		addComponents();
	}
	
	private void addComponents(){
		
		c = new GridBagConstraints();		
		
		actorImage = new JLabel("", SwingConstants.CENTER);
		actorImage.setIcon(new ImageIcon(actorImageURL));
		
		//Field names
		actorIdLN = new JLabel("ID: ");
		actorTypeLN = new JLabel("Type: ");
		actorStateLN = new JLabel("State: ");
		actorLatLN = new JLabel("Latitude: ");
		actorLonLN = new JLabel("Longtitude: ");
		
		//Field contents
		actorIdL = new JLabel("");		
		actorTypeL = new JLabel(""+actorType);
		actorStateL = new JLabel("");
		actorLatL = new JLabel("");
		actorLonL = new JLabel("");
		
		//fonts
		actorIdLN.setFont(fieldNameFont);
		actorTypeLN.setFont(fieldNameFont);
		actorStateLN.setFont(fieldNameFont);
		actorLatLN.setFont(fieldNameFont);
		actorLonLN.setFont(fieldNameFont);
		
		actorIdL.setFont(fieldContentsFont);
		actorTypeL.setFont(fieldContentsFont);
		actorStateL.setFont(fieldContentsFont);
		actorLatL.setFont(fieldContentsFont);
		actorLonL.setFont(fieldContentsFont);
			
		//grid positioning
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.5;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(actorImage,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(actorTypeLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 1;
		add(actorTypeL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(actorStateLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 2;
		add(actorStateL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		add(actorIdLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 3;
		add(actorIdL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		add(actorLonLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 4;
		add(actorLonL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		add(actorLatLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 5;
		add(actorLatL,c);
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
