package edu.cornell.opencomm.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;

/* An object representing a user who is taking part in the conversation */

public class User {
	// DEBUGGING
	private static String LOG_TAG = "Model.User"; // for error checking
	private static boolean D = true;
	
	String username; // The User's JID
	String nickname; // The User's chat nickname
	File vCard; // The User's vCard, used to pass image
	int image; // icon - will be replaced by vCard
	
	// List of all Users initialized
	private static HashMap<String, User> allUsers = new HashMap<String, User>();
	
	// Maps username to User
	public static HashMap<String,User> username_to_person = 
			new HashMap<String,User>(); 
	
	// Maps nickname to User
	public static HashMap<String,User> nickname_to_person = 
			new HashMap<String,User>();
    
	/** CONSTRUCTOR: = a new User
	 * 
	 * @param username - the JID of the User
	 * @param nickname - the User's chosen nickname
	 * @param image - will be replaced by vCard
	 */
	
	public User(String username, String nickname, int image){
        if (D){
        	Log.v(LOG_TAG, "Made a person for the user " + username);
        }
        this.username = username;
        this.nickname = nickname;
        this.image = image;
        allUsers.put(username, this);
        username_to_person.put(username, this);
        nickname_to_person.put(nickname, this);
	}
	
	// SETTERS AND GETTERS
	
	/** @return - the User's JID */
	public String getUsername(){
        return username;
	}
	
	/** @return - the User's chat nickname */
	public String getNickname(){
        return nickname;
	}

	/** Change the user's nickname */
	public void setNickname(String new_nickname){
	   nickname = new_nickname;
	}
	
	/** All users with their JID as the key */
	public static HashMap<String, User> getAllUsers() {
		return allUsers;
	} // end getAllUsers method
}
