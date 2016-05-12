package gpig.common.commands;

import gpig.common.data.Detection;

/**
 * A command representing the detection of a single person by a detection drone
 */
public class DetectionNotification {
    public final Detection detection;

    public DetectionNotification(Detection detection) {
        this.detection = detection;
    }
}
