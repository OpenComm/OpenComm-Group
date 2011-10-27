package edu.cornell.opencomm.network;

/** A class of constants for package edu.cornell.opencomm.network */
public class Network {
	private Network() {}
	
	/** = package name of this class */
	public static final String packageName = "edu.cornell.opencomm.network.";
	
	/** = name of the cycle during which this app is created */
	public static final String cycleName = "eiffel_forth";
	
	
	// CONNECTION CONSTANTS
	/** = default host server */
	public static final String DEFAULT_HOST = "jabber.org";
	
	/** = default port */
	public static final int DEFAULT_PORT = 5222;
	
	/** = debug username */
	public static final String DEBUG_USERNAME = "opencommss@jabber.org";
	
	/** = debug password */
	public static final String DEBUG_PASSWORD = "ssopencomm";
	
	
	// LOGIN INTENT CONSTANTS
	/** = action that calls the MainApplication from Login */
	public static final String ACTION_LOGIN = packageName + "ACTION_LOGIN";
	
	/** = key of the username that is passed through intent from Login */
	public static final String KEY_USERNAME = packageName + "KEY_USERNAME";

	
	// ROOM NAME CONSTANT
	/** = prefix of all opencomm rooms created during this cycle */
	public static final String ROOM_NAME = "opencomm_" + cycleName + "_";
	
	// INVITATION AND KICKOUT REQUEST MESSAGES
	public static final String REQUEST_INVITE = packageName + cycleName + ".REQUEST_INVITE";
	
	// DEFAULT REASONS
	/** = default reason for inviting a person */
	public static final String DEFAULT_INVITE = "You have been invited";
	
	/** = default reason for kicking out a person */
	public static final String DEFAULT_KICKOUT = "You have been kicked out";
	
	/** = default reason for banning a person */
	public static final String DEFAULT_BAN = "You have been banned";
	
}
