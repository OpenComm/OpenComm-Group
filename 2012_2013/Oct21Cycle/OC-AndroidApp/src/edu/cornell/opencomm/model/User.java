package edu.cornell.opencomm.model;

import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;

/* An object representing a user who is taking part in the conversation */

public class User implements Comparable<User> {
	/**
	 * 
	 */
	private static String LOG_TAG = "Model.User";
	/**
	 * 
	 */
	private static boolean D = true;

	/**
	 * The JID of the user
	 */
	String username;
	/**
	 * The nickname of the user
	 */
	String nickname;
	/**
	 * The VCard of the user
	 */
	VCard vCard;
	/**
	 * The image of the user
	 */
	int image;

	/**
	 * TODO Ankit: Move this out of here it is only used for conference and
	 * store it in a map in chat space model The color of the user contact card
	 */
	public int userColor;

	/**
	 * CONSTRUCTOR: = a new User
	 * 
	 * @param username
	 *            - the JID of the User
	 * @param nickname
	 *            - the User's chosen nickname
	 * @param image
	 *            - will be replaced by vCard
	 */
	public User(String username, String nickname, int image) {
		if (D) {
			Log.v(LOG_TAG, "Made a person for the user " + username);
		}
		this.username = username;
		this.nickname = nickname;
		if (image == 0) {
			this.image = R.drawable.question;
		} else {
			this.image = image;
		}
		this.userColor = UserManager.getUserColor(username);
	}

	public User(String firstname, String lastname, String email, byte[] photo,
			String title) {

	}

	/**
	 * CONSTRUCTOR: = a new User without a nickname
	 * 
	 * @param username
	 *            - the JID of the User
	 */
	public User(String username) {
		if (D) {
			Log.v(LOG_TAG, "Made a person for the user " + username);
		}
		this.username = username;
		this.image = R.drawable.question;
		this.userColor = UserManager.getUserColor(username);
	}

	/** @return - the User's JID */
	public String getUsername() {
		return this.username;
	}

	/** @return - the User's chat nickname */
	public String getNickname() {
		return this.nickname;
	}

	/** @return - the int representation of the User's image */
	public int getImage() {
		return this.image;
	}

	/** @return - the User's vCard */
	public VCard getVCard() {
		return this.vCard;
	}

	public int compareTo(User arg0) {
		return (getUsername().compareTo(arg0.getUsername()));
		// returns alphabetic comparison of usernames by using string compareTo
	}
}
// SEE BELOW FOR CODE REMOVED DUE TO REFACTORING
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Please use UserManager.getContactList() and UserManager.updateContactList()
// /** @return - the User's contact list */
// public ArrayList<User> getContactList() {
// return this.contactList;
// }
// TODO: is there a better place for this?
// ANKIT TODO: Moved to User Manager.
// public static User primaryUser;
// // Crystal
// public int userColor = R.color.blue;
// static int[] colors = { R.color.blue, R.color.green, R.color.orange };
// static int color_pointer = 0;
// private static Hashtable<String, Integer> user_color_table =

// new Hashtable<String, Integer>();
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
