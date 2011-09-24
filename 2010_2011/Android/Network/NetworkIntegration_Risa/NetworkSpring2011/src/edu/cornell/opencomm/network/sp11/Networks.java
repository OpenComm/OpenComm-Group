package edu.cornell.opencomm.network.sp11;

public class Networks {
	private Networks() {}
	
	public static final String packageName = "edu.cornell.opencomm.network.sp11.";
	
	/** Establishing XMPP Connections and logging in:
	 * When calling XMPP Service to connect to a specific host through a specific 
	 * port and logging in using a specific username and password, store these 
	 * specifications under KEY_HOST, KEY_PORT, KEY_USERNAME, and KEY_CONNECT respectively,
	 * into the intent's extras bundle. Set the intent's action to ACTION_LOGIN 
	 * before starting the service. Store the created connection under KEY_CONNECT.
	 * To notify the user that the connection has been established, send NOTIFY_CONNECT 
	 * through the notification managers.
	 */
	public static final String KEY_HOST = packageName + "KEY_HOST";
	public static final String KEY_PORT = packageName + "KEY_PORT";
	public static final String KEY_USERNAME = packageName + "KEY_USERNAME";
	public static final String KEY_PASSWORD = packageName + "KEY_PASSWORD";
	public static final String KEY_CONNECT = packageName + "KEY_CONNECT";
	public static final String ACTION_LOGIN = packageName + "ACTION_LOGIN";
	
	/** Creating/removing MUC rooms and users from said rooms:
	 * After the connection is established, call the GUI activity that shows the 
	 * MUC creation/destruction and User addition/removal using ACTION_START_MUC
	 */
	public static final String ACTION_START_MUC = packageName + "ACTION_START_MUC";
	
	
	
	public static final String ACTION_CREATEMUC = packageName + "ACTION_CREATEMUC";
	public static final String ACTION_RECEIVEINVITE = packageName + "ACTION_RECEIVEINVITE";
	public static final String ERROR_LOGIN_XMPPCONN = packageName + "errorLoginXMPPConn";
	public static final String LOGGED_IN = packageName + "loginStatus";
	public static final String CONN = packageName + "xmppConn";
	public static final String ROOMNAME = packageName + "ROOMNAME";
	public static final String ROOMPASSWORD = packageName + "ROOMPASSWORD";
	public static final String NICKNAME = packageName + "NICKNAME";
	public static final String INVITELIST = packageName + "INVITELIST";
	
}
