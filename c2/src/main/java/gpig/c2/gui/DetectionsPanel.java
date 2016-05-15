package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DetectionsPanel extends JPanel {

	  private Image img;
	  private ArrayList<Point> detections;

	  public DetectionsPanel(Image img, ArrayList<Point> detections) {
	    this.img = img;
	    this.detections = detections;
	    this.setOpaque(true);
	    this.setSize(589, 528);
	  }

	  public void paintComponent(Graphics g) {
	    
		  for(Point detection : detections)
		  {
			  System.out.println("detection x=" + (int)detection.getX());
			  System.out.println("detection y=" + (int)detection.getY());
			  
			  g.drawImage(img,
			  (int)detection.getX(),
			  (int)detection.getY(),
			  null);
			  
			  /*g.drawImage(img,
					  (int)detection.getX() - 3,
					  (int)detection.getY() - 3,
					  (int)detection.getX() + 3,
					  (int)detection.getY() + 3,
					  0,
					  0,
					  img.getWidth(null),
					  img.getHeight(null),
					  null);*/
		  }
			  
		  
		  
	  }

}
