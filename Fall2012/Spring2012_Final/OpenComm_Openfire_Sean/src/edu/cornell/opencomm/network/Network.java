package edu.cornell.opencomm.network;

/** A class of constants for package edu.cornell.opencomm.network */
public class Network {
	private Network() {}

	/** = package name of this class */
	public static final String packageName = "edu.cornell.opencomm.network.";

	/** = name of the cycle during which this app is created */
	public static final String cycleName = "openfire";

	// ====================================================================== //
	// ====================================================================== //

	// CONNECTION CONSTANTS
	/** = default host server */
	public static final String DEFAULT_HOST = "cuopencomm.no-ip.org";
	
	/** = default host name */
	public static final String DEFAULT_HOSTNAME = "localhost.localdomain";

	/** = default port */
	public static final int DEFAULT_PORT = 5222;

	/** = debug username */
	public static final String DEBUG_USERNAME = "debug";

	/** = debug password */
	public static final String DEBUG_PASSWORD = "debug";
	
	/** = conference server */ 
	public static final String DEFAULT_CONFERENCE = "conference.localhost.localdomain";
	
	// ====================================================================== //
	// ====================================================================== //

    	// LOGIN INTENT CONSTANTS
	/** = action that calls the MainApplication from Login */
	public static final String ACTION_LOGIN = packageName + "ACTION_LOGIN";

	/** = key of the username that is passed through intent from Login */
	public static final String KEY_USERNAME = packageName + "KEY_USERNAME";

	// ====================================================================== //
	// ====================================================================== //

	// ROOM NAME CONSTANT
	/** = prefix of all opencomm rooms created during this cycle */
	public static final String ROOM_NAME = "opencomm_" + cycleName + "_" + Math.random() * 100 + "_";

	// ====================================================================== //
	// ====================================================================== //

	// INVITATION, KICKOUT AND DELETE REQUEST MESSAGES
	public static final String REQUEST_INVITE = packageName + cycleName + ".REQUEST_INVITE";
	public static final String REQUEST_KICKOUT = packageName + cycleName + ".REQUEST_KICKOUT";
	public static final String REQUEST_DELETE = packageName + cycleName + ".REQUEST_DELETE";

	// INVITATION AND KICKOUT REQUEST REJECTION MESSAGES
	public static final String REJECT_INVITE = packageName + cycleName + ".REJECT_INVITE";
	public static final String REJECT_KICKOUT = packageName + cycleName + ".REJECT_KICKOUT";

	// ====================================================================== //
	// ====================================================================== //

	// DEFAULT REASONS
	/** = default reason for inviting a user */
	public static final String DEFAULT_INVITE = "You have been invited";

	/** = default reason for kicking out a user */
	public static final String DEFAULT_KICKOUT = "You have been kicked out";

	/** = default reason for banning a user */
	public static final String DEFAULT_BAN = "You have been banned";

	/** = default reason for rejecting a user */
	public static final String DEFAULT_REJECT = "Your request has been rejected";

	/** = default reason for deleting a space */
	public static final String DEFAULT_DELETE = "The space has been deleted";
	// ====================================================================== //
	// ====================================================================== //

	// ROLE AFFILIATION CONSTANT
	/** = owner role */
	public static final String ROLE_OWNER = "owner";

	/** = admin role */
	public static final String ROLE_ADMIN = "admin";

	/** = member role */
	public static final String ROLE_MEMBER = "member";

	/** = outcast role */
	public static final String ROLE_OUTCAST = "outcast";


}
