package gpig.common.networking;

@FunctionalInterface
public interface ChannelReceiver {
	void messageReceived(String message);
}
