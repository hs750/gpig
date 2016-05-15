package gpig.common.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import gpig.common.config.CommunicationChannelConfig;
import gpig.common.util.Log;

/**
 * A bi-directional communicaiton channel
 *
 */
public class CommunicationChannel {
	IncomingConnection incomingConnection;
	OutgoingConnection outgoingConnection;
	
	/**
	 * Create a new communication channel
	 * 
	 * @param outgoingChannelName The name of the outbound communication channel
	 * @param incomingChannelName The name of the inbound communication channel
	 * @param outgoingAddress The ip address of the outbound communication channel
	 * @param incomingAddress The ip address of the outbound communication channel
	 * @param receiver A {@link ChannelReceiver} that will receive messages from this network connection
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public CommunicationChannel(String outgoingChannelName, String incomingChannelName, InetAddress outgoingAddress, InetAddress incomingAddress, ChannelReceiver receiver){
		try {
			outgoingConnection = new OutgoingConnection(outgoingChannelName, outgoingAddress);
			incomingConnection = new IncomingConnection(incomingChannelName, incomingAddress, receiver);
		} catch (IOException | TimeoutException e) {
			Log.error("Unable to establish network connection");
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a communication channel
	 * @param config channel configuration settings
	 * @param reciever A {@link ChannelReceiver} that will receive messages from this network connection
	 */
	public CommunicationChannel(CommunicationChannelConfig config, ChannelReceiver reciever){
	    this(config.out, config.in, config.outAddr, config.outAddr, reciever);
	}
	
	/**
	 * Send a message over the communication channel
	 * @param message the message
	 */
	public void sendMessage(String message){
		outgoingConnection.sendMessage(message);
	}
	
	/**
	 * Close the communication channel
	 */
	public void closeConnections(){
		outgoingConnection.closeChannel();
		incomingConnection.closeChannel();
	}
	
	
}
