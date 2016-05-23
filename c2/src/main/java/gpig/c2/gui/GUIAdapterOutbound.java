package gpig.c2.gui;
import java.util.UUID;

import gpig.c2.C2;
import gpig.common.data.Location;

/**
 * Used to get and provide data to the gui in the required format.
 */
public class GUIAdapterOutbound {
	
	private C2 c2;
	
	public GUIAdapterOutbound(C2 c2){
		this.c2 = c2;
	}

	public void requestDeployRedeploy(Location location){
		c2.deployRedeployDC(location);
	}
	
    public void requestBatteryFailure(UUID id){
    	c2.failBattery(id);
    }
    public void requestCommsFailure(UUID id){
    	c2.failComms(id);
    }
    public void requestEngineFailure(UUID id){
    	c2.failEngine(id);
    }
}
