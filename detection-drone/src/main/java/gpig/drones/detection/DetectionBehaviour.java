package gpig.drones.detection;

import gpig.common.data.Detection;
import gpig.common.data.Location;

import java.util.List;

public interface DetectionBehaviour {
    List<Detection> getDetections(Location location);
}
