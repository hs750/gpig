package gpig.common.messages.handlers;

import gpig.common.messages.DeploymentCentreHeartbeat;

@FunctionalInterface
public interface DeploymentCentreHeatbeatHandler {
    public void handle(DeploymentCentreHeartbeat message);
}
