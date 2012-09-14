/**
 * A class of constants for package edu.cornell.opencomm.network.
 * All the values that are hardcoded and required by hcisec are also moved to this file.
 *
 * Issues [TODO]
 * - Looks into authentication used in login function once we move to openfire
 *
 * @author rahularora, risanaka, kriskooi, anneedmundson, jp
 *
 * Asmack - https://github.com/Flowdalic/asmack
 * */

package edu.cornell.opencomm.network;

public class Network {
    private Network() {}

    //Use this switch to go betewen jabber and openfire when one or the other is down
    static boolean isJabber = true;


    /** = package name of this class */
    public static final String packageName = "edu.cornell.opencomm.network.";

    /** = name of the cycle during which this app is created */
    public static final String cycleName = "saopaulo_sean";

	// ====================================================================== //
	// ====================================================================== //

	// CONNECTION CONSTANTS
	/** = default host server */
	public static final String DEFAULT_HOST = isJabber ? "jabber.org" : "cuopencomm.no-ip.org";

	/** = default host name */
	public static final String DEFAULT_HOSTNAME = isJabber ? "jabber.org" : "localhost.localdomain";

	/** = default port */
	public static final int DEFAULT_PORT = 5222;

	/** = debug username */
	public static final String DEBUG_USERNAME = isJabber ? "opencommsec" : "opencommsec";

	/** = debug password */
	public static final String DEBUG_PASSWORD = isJabber ? "secopencomm" : "secopencomm";

	/** = conference server */
	public static final String DEFAULT_CONFERENCE = isJabber ? "conference.jabber.org" : "conference.localhost.localdomain";

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

    /** = default reason for rejecting an invitation request */
    public static final String DEFAULT_REJECT_INVITATION_REQUEST = "Your invitation request has" +
            " been rejected";

    /** = default reason for rejecting a kickout request */
    public static final String DEFAULT_REJECT_KICKOUT_REQUEST_REASON = "Your kickout request has" +
            " been rejected;";

    /** = default reason for rejecting a user */
    public static final String DEFAULT_REJECT = "Your request has been rejected";

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
