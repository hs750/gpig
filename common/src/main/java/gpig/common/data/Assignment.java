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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deploymentCentreID == null) ? 0 : deploymentCentreID.hashCode());
        result = prime * result + ((detection == null) ? 0 : detection.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Assignment other = (Assignment) obj;
        if (deploymentCentreID == null) {
            if (other.deploymentCentreID != null)
                return false;
        } else if (!deploymentCentreID.equals(other.deploymentCentreID))
            return false;
        if (detection == null) {
            if (other.detection != null)
                return false;
        } else if (!detection.equals(other.detection))
            return false;
        if (status != other.status)
            return false;
        return true;
    }

    public enum AssignmentStatus {
        ASSIGNED,
        DELIVERED
    }
}
