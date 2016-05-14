package gpig.common.networking;

@FunctionalInterface
public interface ChannelReceiver {
	
	public void messageRecieved(String message);

}
