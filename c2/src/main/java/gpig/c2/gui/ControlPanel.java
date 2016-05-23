package gpig.c2.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

	protected GUI gui;
	protected Color backgroundColour;
	protected Font fieldNameFont;
	protected Font fieldContentsFont;
	protected GridBagConstraints c;
	
	//Field names
	protected JLabel selectedLatLN;
	protected JLabel selectedLonLN;
	
	//Field contents
	protected JLabel selectedLatL;
	protected JLabel selectedLonL;
	
	//control buttons
	protected JButton deployB;
	protected JButton reDeployB;
	protected JButton failBatteryB;
	protected JButton failCommsB;
	protected JButton failEngineB;
	
	
	public ControlPanel(GUI gui,Dimension size) {
		super();
		
		this.gui = gui;
		this.backgroundColour = new Color(153, 153, 255);
		this.setSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setLayout(new GridBagLayout());
		this.setBackground(backgroundColour);
		this.setBorder(new MatteBorder(0,0,1,0,Color.BLACK));
		
		fieldNameFont = new Font("Console", Font.BOLD, 15);
		fieldContentsFont = new Font("Console", Font.PLAIN, 15);
		
		addComponents();
	}
	
	private void addComponents(){
		
		double buttonWidth = this.getSize().getWidth()/4;
		
		c = new GridBagConstraints();		
		
		//Field names
		selectedLatLN = new JLabel("Selected Latitude: ");
		selectedLonLN = new JLabel("Selected Longtitude: ");
		
		//Field contents
		selectedLatL = new JLabel("None");
		selectedLonL = new JLabel("None");
		
		//buttons
		deployB = new JButton("Deploy to");
		reDeployB = new JButton("Redeploy to");
		failBatteryB = new JButton("Fail Battery");
		failCommsB = new JButton("Fail Comms");
		failEngineB = new JButton("Fail Engine");
		
		//cant make the buttons the same size
		//deployB.setPreferredSize(failBatteryB.getPreferredSize());
		deployB.setPreferredSize(new Dimension((int)buttonWidth,deployB.getHeight()));
		reDeployB.setPreferredSize(new Dimension((int)buttonWidth,reDeployB.getHeight()));
		failBatteryB.setPreferredSize(new Dimension((int)buttonWidth,failBatteryB.getHeight()));
		failCommsB.setPreferredSize(new Dimension((int)buttonWidth,failCommsB.getHeight()));
		failEngineB.setPreferredSize(new Dimension((int)buttonWidth,failEngineB.getHeight()));
		
		
		deployB.setEnabled(false);
		reDeployB.setEnabled(false);
		failBatteryB.setEnabled(false);
		failCommsB.setEnabled(false);
		failEngineB.setEnabled(false);
		
		deployB.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			    deployB.setEnabled(false);
			    
			    gui.requestDeploy(
			    		new Location(
			    				Double.parseDouble(selectedLatL.getText()),
			    				Double.parseDouble(selectedLonL.getText())
			    				));
			  } 
			});
		
		reDeployB.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  reDeployB.setEnabled(false);
			    
			    gui.requestRedeploy(
			    		new Location(
			    				Double.parseDouble(selectedLatL.getText()),
			    				Double.parseDouble(selectedLonL.getText())
			    				));
			  } 
			});
		
		failBatteryB.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  failBatteryB.setEnabled(false);
				  failCommsB.setEnabled(false);
				  failEngineB.setEnabled(false);
				  
			    gui.requestBatteryFailure();
			  } 
			});
		
		failCommsB.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  failBatteryB.setEnabled(false);
				  failCommsB.setEnabled(false);
				  failEngineB.setEnabled(false);
			    
			    gui.requestCommsFailure();
			  } 
			});
		
		failEngineB.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  failBatteryB.setEnabled(false);
				  failCommsB.setEnabled(false);
				  failEngineB.setEnabled(false);
			    
			    gui.requestEngineFailure();
			  } 
			});
		

		
		//butons formatting
		//deployB.setBackground(backgroundColour);
		//reDeployB.setBackground(backgroundColour);
		
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
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
		add(selectedLatL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(selectedLonLN,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 1;
		add(selectedLonL,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.EAST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(deployB,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		add(reDeployB,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.EAST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		add(failBatteryB,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		add(failCommsB,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 3;
		add(failEngineB,c);
	}
	
	public void selectLocation(Location location){
		selectedLatL.setText(""+String.format ("%.6f", location.latitude()));
		selectedLonL.setText(""+String.format ("%.6f", location.longitude()));
	}
	
	public void enableDeployment(){
		deployB.setEnabled(true);
	}
	public void enableRedeployment(){
		reDeployB.setEnabled(true);
	}
	public void disableDeployment(){
		deployB.setEnabled(false);
	}
	public void disableRedeployment(){
		reDeployB.setEnabled(false);
	}
	
	public void enableBatteryFailure(){
		failBatteryB.setEnabled(true);
	}
	public void disableBatteryFailure(){
		failBatteryB.setEnabled(false);
	}
	public void enableCommsFailure(){
		failCommsB.setEnabled(true);
	}
	public void disableCommsFailure(){
		failCommsB.setEnabled(false);
	}
	public void enableEngineFailure(){
		failEngineB.setEnabled(true);
	}
	public void disableEngineFailure(){
		failEngineB.setEnabled(false);
	}
	
	
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
