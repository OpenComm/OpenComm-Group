package edu.cornell.opencomm.util;

import java.util.regex.Pattern;

public class Util {
	// TODO create the values class
	private final static boolean DEBUG = false;
	/**
	 * Pattern for validating email address
	 */
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");

	/**
	 * 
	 */
	public final static Pattern NAME_PATTERN_SAVE = Pattern
			.compile("[a-zA-Z-]+");

	public final static Pattern NAME_PATTERN_FOCUS_CHANGE = Pattern
			.compile("[ a-zA-Z-]+");

	public static boolean validateString(String str, Pattern pattern) {
		// TODO debug to logger ("string to be validated and the patter used)
		return pattern.matcher(str).matches();
	}

	// Use this switch to go betewen jabber and openfire when one or the other
	// is down
	static boolean isJabber = false;

	// CONNECTION CONSTANTS
	/** = default host server */
	public static final String DEFAULT_HOST = isJabber ? "jabber.org"
			: "cuopencomm.no-ip.org";

	/** = default host name */
	public static final String DEFAULT_HOSTNAME = isJabber ? "jabber.org"
			: "localhost.localdomain";

	/** = default port */
	public static final int DEFAULT_PORT = 5222;

	/** = debug username */
	public static final String DEBUG_USERNAME = isJabber ? "opencommsec"
			: "opencommsec";

	/** = debug password */
	public static final String DEBUG_PASSWORD = isJabber ? "secopencomm"
			: "secopencomm";

	/** = conference server */
	public static final String DEFAULT_CONFERENCE = isJabber ? "conference.jabber.org"
			: "conference.localhost.localdomain";
}
