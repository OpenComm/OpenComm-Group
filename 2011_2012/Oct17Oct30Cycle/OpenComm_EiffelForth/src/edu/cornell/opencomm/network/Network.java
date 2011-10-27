package edu.cornell.opencomm.network;

/** A class of constants for package edu.cornell.opencomm.network */
public class Network {
	private Network() {}
	
	/** = package name of this class */
	public static final String packageName = "edu.cornell.opencomm.network.";
	
	/** = name of the cycle during which this app is created */
	public static final String cycleName = "commonlisp_dylan";
	
	/** = default host server */
	public static final String DEFAULT_HOST = "jabber.org";
	/** = default port */
	public static final int DEFAULT_PORT = 5222;
	
	/** = action that calls the MainApplication from Login */
	public static final String ACTION_LOGIN = packageName + "ACTION_LOGIN";
	
	/** = key of the username that is passed through intent from Login */
	public static final String KEY_USERNAME = packageName + "KEY_USERNAME";

	/** = prefix of all opencomm rooms created during this cycle */
	public static final String ROOM_NAME = "opencomm_" + cycleName + "_";
	
	/** = default reason for inviting a person */
	public static final String DEFAULT_INVITE = "Come join us";

	/** = default reason for kicking out a person */
	public static final String DEFAULT_KICKOUT = "Go away";
	
	/** = default reason for banning a person */
	public static final String DEFAULT_BAN = "You have been banned";
	
	/* Establishing XMPP Connections and logging in:
	 * When calling XMPP Service to connect to a specific host through a specific 
	 * port and logging in using a specific username and password, store these 
	 * specifications under KEY_HOST, KEY_PORT, KEY_USERNAME, and KEY_CONNECT respectively,
	 * into the intent's extras bundle. Set the intent's action to ACTION_LOGIN 
	 * before starting the service. Store the created connection under KEY_CONNECT.
	 * To notify the user that the connection has been established, send NOTIFY_CONNECT 
	 * through the notification managers.
	 */
	//public static final String KEY_HOST = packageName + "KEY_HOST";
	//public static final String KEY_PORT = packageName + "KEY_PORT";
	//public static final String KEY_PASSWORD = packageName + "KEY_PASSWORD";
	//public static final String KEY_CONNECT = packageName + "KEY_CONNECT";
	//public static final String ACTION_LOGIN_RETRY = packageName + "ACTION_LOGIN_RETRY";
	//public static final int REQUEST_LOGOUT = 1;
	//public static final boolean XMPPCONFIG_COMPRESSION = true;
	//public static final boolean XMPPCONFIG_SASLAUTH = true;
	
	// Constants that indicate the current connection state
   public static final int STATE_NONE = 0;       // we're not connected
   public static final int STATE_LOGIN = 1;	// we've logged in
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	
    // Handler Constants for communication between NetworkService and activities
   // public static final int MESSAGE_STATE_CHANGE = 1;
   // public static final int MESSAGE_READ = 2;
   // public static final int MESSAGE_WRITE = 3;
   // public static final int MESSAGE_DEVICE_NAME = 4;
   // public static final int MESSAGE_TOAST = 5;
    
  
	//public static final String ACTION_START_MUC = packageName + "ACTION_START_MUC";	
	//public static final String ACTION_CREATEMUC = packageName + "ACTION_CREATEMUC";
	//public static final String ACTION_RECEIVEINVITE = packageName + "ACTION_RECEIVEINVITE";
	//public static final String ERROR_LOGIN_XMPPCONN = packageName + "errorLoginXMPPConn";
	//public static final String LOGGED_IN = packageName + "loginStatus";
	//public static final String CONN = packageName + "xmppConn";
	//public static final String ROOMNAME = packageName + "ROOMNAME";
	//public static final String ROOMPASSWORD = packageName + "ROOMPASSWORD";
	//public static final String NICKNAME = packageName + "NICKNAME";
	//public static final String INVITELIST = packageName + "INVITELIST";
}
