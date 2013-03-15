package network;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.jivesoftware.smack.*;

import android.os.Parcelable;

public class NetConnection implements Serializable {

	public static final String DEFAULT_HOST = "cuopencomm.no-ip.org";
	public static final String DEFAULT_HOSTNAME = "@cuopencomm";
	public static final int DEFAULT_PORT = 5222;
	public static final String DEFAULT_RESOURCE = "OpenComm";
	public static final String DEFAULT_ID = "oc1testorg";
	public static final String DEFAULT_PWD = "opencomm2012";
	
	FileWriter fw; 
	public XMPPConnection conn;
	ConnectionConfiguration config;
	
	public enum ConnState
	{
		CONNECTED, DISCONNECTED, LOGIN_ERROR
	};
	
	public NetConnection() 
	{
		config = new ConnectionConfiguration(DEFAULT_HOST, DEFAULT_PORT);
	}
	public NetConnection(String host)
	{
		this(host, DEFAULT_PORT);
	}
	public NetConnection(String host, int port)
	{
		config = new ConnectionConfiguration(host, port);		
	}
	public synchronized ConnState connect()
	{
		// Create a connection to the jabber.org server on a specific port.
		try
		{
			conn = new XMPPConnection(config);
			config.setSASLAuthenticationEnabled(true);
			conn.connect();				
			return ConnState.CONNECTED;
		}
		catch(XMPPException e)
		{
			System.out.println("connection failure");
			return ConnState.DISCONNECTED;
		}
	}
	public synchronized boolean isConnected() 
	{
		return conn.isConnected();
	}
	public synchronized XMPPConnection getConnection() 
	{
		if(!conn.isConnected())
		{
			connect();
		}
		return conn;
	}
	public synchronized ConnState login()
	{
		if(!conn.isConnected())
		{
			connect();
		}
		try
		{
			//we may check invalid login format using regex - not necessary at this point
			conn.login(DEFAULT_ID, DEFAULT_PWD, DEFAULT_RESOURCE);	
			
			//we may check invalid login account - not necessary at this point
			//if(invalid) return ConnState.LOGIN_ERROR;
		}
		catch (XMPPException e) 
		{
			e.printStackTrace();
			return ConnState.LOGIN_ERROR;
		}

		return ConnState.DISCONNECTED;
	}
	public synchronized ConnState logout()
	{
		conn.disconnect();
		return ConnState.DISCONNECTED;		
	}
}
