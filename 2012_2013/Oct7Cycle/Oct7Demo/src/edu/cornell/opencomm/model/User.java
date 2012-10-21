package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;
import edu.cornell.opencomm.R;

/* An object representing a user who is taking part in the conversation */

public class User {
	// DEBUGGING
	private static String LOG_TAG = "Model.User"; // for error checking
	private static boolean D = true;

	String username; // The User's JID
	String nickname; // The User's chat nickname
	VCard vCard; // The User's vCard, used to pass image
	int image; // icon - will be replaced by vCard
	ArrayList<User> contactList;
	
	// TODO: is there a better place for this?
	public static User primaryUser;

	// Crystal
	public int user_color = R.color.blue;
	static int[] colors = { R.color.blue, R.color.green, R.color.orange };
	static int color_pointer = 0;
	private static Hashtable<String, Integer> user_color_table = 
			new Hashtable<String, Integer>();

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

		if (user_color_table.containsKey(username)) {
			user_color = user_color_table.get(username);
		} else {
			user_color = colors[color_pointer%colors.length];
			color_pointer = (color_pointer >= 9 ? 0 : ++color_pointer);
			user_color_table.put(username, user_color);
		}
		this.contactList = new ArrayList<User>();
	}

	// SETTERS AND GETTERS

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

	/** @return - the User's contact list */
	public ArrayList<User> getContactList() {
		return this.contactList;
	}
}