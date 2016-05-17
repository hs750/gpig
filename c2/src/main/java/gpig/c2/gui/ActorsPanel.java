package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *	Shows actors of some type
 */
public class ActorsPanel extends JPanel {

	  private Image actorImg;
	  private ArrayList<Point> actors;

	  public ActorsPanel(Dimension size, Image actorImg, ArrayList<Point> actors) {
	    this.actorImg = actorImg;
	    this.actors = actors;
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	    setBounds(0, 0, size.width, size.width);
	    this.setOpaque(false);
	  }

	  public void paintComponent(Graphics g) {
	    
		  for(Point actor : actors)
		  {
			  
			  //scale and centre the actor image
			  g.drawImage(actorImg,
					  (int)actor.getX() - 13,
					  (int)actor.getY() - 13,
					  (int)actor.getX() + 13,
					  (int)actor.getY() + 13,
					  0,
					  0,
					  actorImg.getWidth(null),
					  actorImg.getHeight(null),
					  null);
		  }
			  
		  
		  
	  }

}

