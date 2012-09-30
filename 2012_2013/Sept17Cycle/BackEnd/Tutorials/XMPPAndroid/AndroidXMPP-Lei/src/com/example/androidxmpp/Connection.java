package com.example.androidxmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Connection {
	
	private String host;
	private int port;
	
	
	private ConnectionConfiguration config;
	private XMPPConnection connection;
	

	public Connection(String host){
		this.host=host;
		connection= new XMPPConnection(host);
	
	}
	
	
	public Connection(String host, int port){
		this.host=host; this.port=port;
		config= new ConnectionConfiguration(host,port);
		config.setCompressionEnabled(true);
		//config.setSASLAuthenticationEnabled(true);
		
	}
	
	/**Connects the user to an XMPP Server after a connection has been made with a host and port*/
	public void connectMe(String userName, String pw) throws XMPPException{
	
	//	config.setReconnectionAllowed(true);
		connection.login(userName, pw);
	}
	
	public void disconnect(){
			connection.disconnect();
	}
	
	public XMPPConnection getXMPPConnection(){
		return connection;
	}
	
	

}
