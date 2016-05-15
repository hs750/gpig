package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DetectionsPanel extends JPanel {

	  private Image detectionImg;
	  private ArrayList<Point> detections;

	  public DetectionsPanel(Dimension size, Image detectionImg, ArrayList<Point> detections) {
	    this.detectionImg = detectionImg;
	    this.detections = detections;
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    setBounds(0, 0, size.width, size.width);
	    this.setOpaque(false);
	  }

	  public void paintComponent(Graphics g) {
	    
		  for(Point detection : detections)
		  {
			  System.out.println("detection x=" + (int)detection.getX());
			  System.out.println("detection y=" + (int)detection.getY());
			  
			  
			  g.drawImage(detectionImg,
					  (int)detection.getX() - 10,
					  (int)detection.getY() - 10,
					  (int)detection.getX() + 10,
					  (int)detection.getY() + 10,
					  0,
					  0,
					  detectionImg.getWidth(null),
					  detectionImg.getHeight(null),
					  null);
			  
			  //g.drawRect((int)detection.getX(), (int)detection.getY(), 10, 10);
		  }
			  
		  
		  
	  }

}

