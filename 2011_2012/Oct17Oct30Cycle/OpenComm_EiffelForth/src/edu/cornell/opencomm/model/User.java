package edu.cornell.opencomm.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;

/* An object representing a user who is taking part in the conversation */

public class User {
	
	String username; // The User's JID
	String nickname; // The User's chat nickname
	File vCard; // The User's vCard, used to pass image
	
	// List of all Users initialized
	public static ArrayList<User> allUsers = new ArrayList<User>();
	
	// Maps username to User
	public static HashMap<String,User> username_to_person = 
			new HashMap<String,User>(); 
	
	// Maps nickname to User
	public static HashMap<String,User> nickname_to_person = 
			new HashMap<String,User>(); 
	
	private static String LOG_TAG = "Model.User"; // for error checking
	private static boolean D = true;
    
	/** CONSTRUCTOR: = a new User
	 * 
	 * @param username - the JID of the User
	 * @param nickname - the User's chosen nickname
	 */
	
	public User(String username, String nickname){
        if (D){
        	Log.v(LOG_TAG, "Made a person for the user " + username);
        }
        this.username = username;
        this.nickname = nickname;
        allUsers.add(this);
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

	/* Change the user's nickname */
	public void setNickname(String new_nickname){
	   nickname = new_nickname;
	}
}
