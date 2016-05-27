package gpig.c2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import java.util.UUID;

import gpig.common.data.ActorType;
import gpig.common.data.DroneState;
import gpig.common.data.Location;

/**
 * Displays information about a DC
 */
public class DetectionDroneInfoPanel extends InfoPanel {

    private Location location;
    private DroneState state;

    public DetectionDroneInfoPanel(UUID id, Location location, DroneState state, ActorType actorType, URL imageURL,
            Dimension size) {
        super(actorType, imageURL, size);
        this.location = location;
        this.state = state;

        // Set parent field contents
        actorIdL.setText("" + id);
        actorTypeL.setText("Detection Drone");
        actorStateL.setText("" + state);
        if (location != null) {
            actorLatL.setText("" + String.format("%.6f", location.latitude()));
            actorLonL.setText("" + String.format("%.6f", location.longitude()));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
