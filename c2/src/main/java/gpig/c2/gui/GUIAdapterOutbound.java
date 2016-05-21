package gpig.c2.gui;
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

	public void DeployRedeploy(Location location){
		c2.DeployRedeployDC(location);
	}
}
