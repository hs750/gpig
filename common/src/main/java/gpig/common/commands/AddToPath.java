package gpig.common.commands;

import gpig.common.data.Path;

/**
 * A command representing the desire to add a single waypoint to the end of a
 * vehicle's path.
 */
public class AddToPath {
    public final Path.Waypoint waypoint;

    public AddToPath(Path.Waypoint waypoint) {
        this.waypoint = waypoint;
    }
}
