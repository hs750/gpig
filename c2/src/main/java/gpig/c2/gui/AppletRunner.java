package gpig.c2.gui;

import gpig.c2.gui.unfolding.MapApp;
import processing.core.PApplet;

/**
 *	Runs the map applet in a separate thread.
 */
public class AppletRunner extends Thread{

	private GUI gui;
	private MapApp mapApp;

	public AppletRunner(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void run() {
        mapApp = new MapApp(gui);
        PApplet.runSketch(new String[] { "Group 4 (Vendor Lock-in): Flood Victim Detection and Supply Delivery" }, mapApp);
        
    }
}
