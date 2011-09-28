package edu.cornell.opencomm.network.sp11;

/** An instance of this class is a user with a pre-created username
 * and password
 * @author Risa Naka (rn96)
 *
 */
public class XMPPUser {
	private String username;
	private String password;
	
	/** Constructor: a new XMPP user with specified username and password
	 * 
	 * @param un - the user's username.
	 * @param pwd - the password associated with this particular username.
	 */
	public XMPPUser(String un, String pwd) {
		this.username = un;
		this.password = pwd;
	} // end XMPPUser method
	
	/** = this user in String format:<br />
	 * XMPP User: (username)<br />
	 * password: (password)
	 */
	public String toString() {
		String temp = "";
		temp += "XMPP User: " + this.username;
		temp += "\tpassword: " + this.password;
		return temp;
	} // end toString method

	/**= this username */
	public String getUsername() {
		return this.username;
	} // end getUsername method

	/** set this username to un */
	public void setUsername(String un) {
		this.username = un;
	} // end setUsername method

	/** = this password */
	public String getPassword() {
		return this.password;
	} // end getPassword method

	/** set this password to pwd */
	public void setPassword(String pwd) {
		this.password = pwd;
	} // end setPassword method
} // end Class XMPPUser
