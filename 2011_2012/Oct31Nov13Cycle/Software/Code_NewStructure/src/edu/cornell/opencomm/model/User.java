package edu.cornell.opencomm.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cornell.opencomm.R;
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
	
	//Crystal
	public int user_color=R.color.blue;
	static int[] colors={R.color.blue,R.color.green,R.color.orange,R.color.teal};
	static int color_pointer=0;
	
	// Maps JID to User
	private static HashMap<String, User> allUsers = new HashMap<String, User>();
	
	// Maps nickname to User -- TODO: delete if not needed by UI
	public static HashMap<String,User> nickname_to_user = 
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
        if (image == 0){
        	this.image = R.drawable.question;
        } else {
            this.image = image;	
        }
        allUsers.put(username, this);
        nickname_to_user.put(nickname, this);
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
	
	/** @return - the int representation of the User's image */
	public int getImage(){
		return image;
	}
	
	/** @return - the User's vCard */
	public File getVCard(){
		return vCard;
	}

	/** Change the user's nickname 
	 * @param new_nickname - the User's new nickname
	 */
	// TODO: ask Design if we want this/if a User can have different nicks for each Space
	public void setNickname(String new_nickname){
	   nickname = new_nickname;
	}
	
	/** @return - all users with their JID as the key */
	public static HashMap<String, User> getAllUsers() {
		return allUsers;
	} // end getAllUsers method
}
