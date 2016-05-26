package gpig.common.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import gpig.common.util.Log;

/**
 * An incoming network connection
 *
 */
public class IncomingConnection{
	private final String channelName;
	private Connection connection;
	private Channel channel;
	
	/**
	 * A new incoming network connection
	 * 
	 * @param incomingChannelName the name of the incoming connection
	 * @param address the address of the incoming connection
	 * @param receiver A {@link ChannelReceiver} which will receive messages from this conneciton
	 * @throws IOException 
	 * @throws TimeoutException
	 */
	public IncomingConnection(String incomingChannelName, InetAddress address, ChannelReceiver receiver) throws IOException, TimeoutException {
		channelName = incomingChannelName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(address.getHostAddress());
		factory.setUsername("gpig");
		factory.setPassword("gpig");
		connection = factory.newConnection();
		channel = connection.createChannel();
		
		channel.exchangeDeclare(channelName, "fanout");
		String queueName = channel.queueDeclare().getQueue(); //Anonymous queue
		channel.queueBind(queueName, channelName, "");
		
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				receiver.messageReceived(message);
			};
		};
		
		Log.info("Ready to receive messages on channel %s", channelName);
		try {
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			Log.error("Unable to read channel %s", channelName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the incoming network connection
	 */
	public void closeChannel(){
		try {
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		Log.info("Closed incoming channel %s", channelName);
	}
}
