package opencomm.rtp.bombaysapphire;

import java.util.HashMap;

/** An instance of this class is a user to demonstrate
 * parallel RTP sessions
 */
public class UserRTP {
	private String username; // name of user
	private String IPAdd; // user's IP address
	/** private static HashMap<String, UserRTP> allUsers; // all users
	
	/** static: = all users created
	public static HashMap<String, UserRTP> getAllUsers() {
		return UserRTP.allUsers;
	} // end getAllUsers method */
	
	/** set this user's username to un */
	public void setUsername(String un) {
		this.username = un;
	} // end setUsername method

	/** = this user's username */
	public String getUsername() {
		return this.username;
	} // end getUsername method
	
	/** set this user's IP address to ip */
	public void setIPAdd(String ip) {
		this.IPAdd = ip;
	} // end setIPAdd method

	/** = this user's IP address */
	public String getIPAdd() {
		return this.IPAdd;
	} // end getIPAdd method

	/** Constructor: Creates a User with username un and ipAdd ip */
	public UserRTP(String un, String ip) {
		this.username = un;
		this.IPAdd = ip;
		// UserRTP.allUsers.put(un, this);
	} // end UserRTP method
} // end class UserRTP
