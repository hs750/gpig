package gpig.common.data;

import java.util.UUID;

/**
 * An assignment made by the C2, delegating the delivery to a detection to a
 * single deployment centre.
 */
public class Assignment {
    public final Detection detection;
    public final UUID deploymentCentreID;
    public AssignmentStatus status;

    public Assignment(Detection detection, UUID deploymentCentreID) {
        this.detection = detection;
        this.deploymentCentreID = deploymentCentreID;
        this.status = AssignmentStatus.ASSIGNED;
    }
    private Assignment() {
        detection = null;
        deploymentCentreID = null;
    }

    public enum AssignmentStatus {
        ASSIGNED,
        DELIVERED
    }
}
