package gpig.common.messages;

import gpig.common.data.Path;

/**
 * A command representing an override of a drone's current path with a new path.
 * To be used (for example) for recalling.
 */
public class SetPath {
    public final Path path;

    public SetPath(Path path) {
        this.path = path;
    }

    private SetPath() {
        path = null;
    }
}
