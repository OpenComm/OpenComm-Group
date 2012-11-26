package edu.cornell.opencomm.util;

import java.util.regex.Pattern;

/**
 * Utility class Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Ankit Singh
 * 
 */
public class Util {
	// TODO create the values class
	private final static boolean D = false;
	/**
	 * Pattern for validating email address
	 */
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");

	/**
	 * 
	 */
	public final static Pattern NAME_PATTERN_SAVE = Pattern
			.compile("^[A-Z-][a-z]+");

	/**
	 * Password can be minimum 10 maximum 30
	 */
	public final static Pattern PASSWORD = Pattern.compile(".{10,30}$");

	/**
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean validateString(String str, Pattern pattern) {
		// TODO debug to logger ("string to be validated and the patter used)
		if (D)
			return true;
		str = str.trim();
		return pattern.matcher(str).matches();
	}

	public static String generateUniqueRoomId(String conferenceTitle,
			String ConferenceTime,String owner) {
		return new StringBuffer(conferenceTitle).append(":")
				.append(ConferenceTime).append(":").append(owner).toString();
	}
}