package edu.cornell.opencomm.util;

import java.util.regex.Pattern;

public class Util {
	// TODO create the values class
	@SuppressWarnings("unused")
	private final static boolean D = true;
	/**
	 * Pattern for validating email address
	 */
	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");

	/**
	 * 
	 */
	public final static Pattern NAME_PATTERN_SAVE = Pattern
			.compile("^[a-zA-Z-][a-z]+");

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
		return pattern.matcher(str).matches();
	}
}