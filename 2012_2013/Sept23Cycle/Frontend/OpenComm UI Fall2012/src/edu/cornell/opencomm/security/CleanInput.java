package edu.cornell.opencomm.security;

/**
 * 
 * This should be used to validate and clean all user input (for account creation, login, 
 * 		forgetting login, creating a new conference, editing a conference, edit account info).  
 *
 */
public class CleanInput {
	
	/**
	 * TODO: check input string for invalid syntax, code injection attacks, email format, 
	 * 		and allowed characters
	 * @param s
	 * @param isEmail
	 * @return true for success and false for failure/error in validation/cleaning
	 */
	public boolean clean(String s, boolean isEmail){
		return false;
	}
	
}
