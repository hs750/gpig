package gpig.c2;


import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI {

	    protected static void createAndShowGUI() {

	        JFrame frame = new JFrame("HelloWorldSwing");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(false);
	        
	 
	        ImagePanel panel = new ImagePanel(new ImageIcon("src/main/resources/YorkMap.png").getImage());

	        frame.getContentPane().add(panel);
	        
	        frame.pack();
	        frame.setVisible(true);
	        
	        frame.setLocationRelativeTo(null);
	    }
}
