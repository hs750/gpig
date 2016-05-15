package gpig.common.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import gpig.common.util.Log;

/**
 * An outgoing network connection.
 *
 */
public class OutgoingConnection {
	private final String channelName;
	private Connection connection;
	private Channel channel;

	/**
	 * A new outgoing network connection
	 * 
	 * @param outgoingChannelName the name of the outgoing connection
	 * @param address the address of the outgoing connection
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public OutgoingConnection(String outgoingChannelName, InetAddress address) throws IOException, TimeoutException {
		channelName = outgoingChannelName;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(address.getHostAddress());
		connection = factory.newConnection();
		channel = connection.createChannel();

		channel.exchangeDeclare(channelName, "fanout");
	}
	
	/**
	 * Send a message over the network connection
	 * 
	 * @param message the message
	 */
	public void sendMessage(String message){
		try {
			channel.basicPublish(channelName, "", null, message.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the network connection
	 */
	public void closeChannel(){
		try {
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		Log.info("Closed outgoing channel {}", channelName);
	}
}
