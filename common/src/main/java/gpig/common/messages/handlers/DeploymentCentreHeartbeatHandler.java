package gpig.common.messages.handlers;

import gpig.common.messages.DeploymentCentreHeartbeat;

@FunctionalInterface
public interface DeploymentCentreHeartbeatHandler {
    public void handle(DeploymentCentreHeartbeat message);
}
