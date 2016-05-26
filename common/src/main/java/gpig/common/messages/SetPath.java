package gpig.common.messages;

import java.util.UUID;

import gpig.common.data.Path;

/**
 * A command representing an override of a drone's current path with a new path.
 * To be used (for example) for recalling.
 */
public class SetPath {
    public final Path path;
    public final UUID assignee;
    
    public SetPath(Path path, UUID assignee) {
        this.path = path;
        this.assignee = assignee;
    }

    private SetPath() {
        path = null;
        assignee = null;
    }

    public boolean isFor(UUID uuid) {
        return assignee.equals(uuid);
    }
}
