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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	
	//checkbox filters
	protected JCheckBox showDetectionDronesCB;
	protected JCheckBox showDeliveryDronesCB;
	protected JCheckBox showDCsCB;
	protected JCheckBox showDeliveredDetectionsCB;
	protected JCheckBox showUndeliveredDetectionsCB;
	protected JCheckBox showExternalDetectionsCB;
	
	
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
		
		//checkbox filters
		
		showDetectionDronesCB = new JCheckBox("Detection Drones");
		showDeliveryDronesCB = new JCheckBox("Delivery Drones");
		showDCsCB = new JCheckBox("Deployment Centres");
		showDeliveredDetectionsCB = new JCheckBox("Delivered Detections");
		showUndeliveredDetectionsCB = new JCheckBox("Undelivered Detections");
		showExternalDetectionsCB = new JCheckBox("External Detections");
		
		//check box backgrounds
		
		showDetectionDronesCB.setOpaque(false);
		showDeliveryDronesCB.setOpaque(false);
		showDCsCB.setOpaque(false);
		showDeliveredDetectionsCB.setOpaque(false);
		showUndeliveredDetectionsCB.setOpaque(false);
		showExternalDetectionsCB.setOpaque(false);
		
		//initial states
		deployB.setEnabled(false);
		reDeployB.setEnabled(false);
		failBatteryB.setEnabled(false);
		failCommsB.setEnabled(false);
		failEngineB.setEnabled(false);
		
		showDetectionDronesCB.setSelected(true);
		showDeliveryDronesCB.setSelected(true);
		showDCsCB.setSelected(true);
		showDeliveredDetectionsCB.setSelected(true);
		showUndeliveredDetectionsCB.setSelected(true);
		showExternalDetectionsCB.setSelected(true);
		
		//listeners
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
		
		showDetectionDronesCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowDetectionDrones(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowDetectionDrones(true);
	             }
	          }           
	       });
		
		showDeliveryDronesCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowDeliveryDrones(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowDeliveryDrones(true);
	             }
	          }           
	       });
		
		showDCsCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowDCs(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowDCs(true);
	             }
	          }           
	       });
		
		showDeliveredDetectionsCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowDeliveredDetections(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowDeliveredDetections(true);
	             }
	          }           
	       });
		
		showUndeliveredDetectionsCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowUndeliveredDetections(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowUndeliveredDetections(true);
	             }
	          }           
	       });
		
		showExternalDetectionsCB.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {         
	             if(e.getStateChange() == ItemEvent.DESELECTED){
	            	 gui.setShowExternalDetections(false);
	             }else if(e.getStateChange() == ItemEvent.SELECTED){
	            	 gui.setShowExternalDetections(true);
	             }
	          }           
	       });
			
		//fonts
		selectedLatLN.setFont(fieldNameFont);
		selectedLonLN.setFont(fieldNameFont);
		
		selectedLatL.setFont(fieldContentsFont);
		selectedLonL.setFont(fieldContentsFont);	

		
		//add labels
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
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
		
		//add buttons
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(deployB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		add(reDeployB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		add(failBatteryB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		add(failCommsB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 3;
		add(failEngineB,c);
		
		//add checkboxes
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		add(showDeliveredDetectionsCB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 4;
		add(showUndeliveredDetectionsCB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 4;
		add(showExternalDetectionsCB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		add(showDCsCB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 5;
		add(showDetectionDronesCB,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.weighty = 0.5;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 5;
		add(showDeliveryDronesCB,c);
		
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
