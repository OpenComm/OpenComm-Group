package edu.cornell.opencomm.model;

import java.util.HashMap;
import java.util.LinkedList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.drawable;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

/* An object representing a user who is taking part in the conversation */

public class User {
	private static String LOG_TAG = "OC_Person"; // for error checking
	Context context;	
	
	/** The Essentials */
	String username; // The username the user chose, will show up on screen, is the XMPP id as well
	String nickname; // The nickname the user chose, needed for the network service
	int image; // the R.drawable int for this image
	String description; // will show up when hover over this person's icon 
	//Crystal
		public int user_color=R.color.blue;
		static int[] colors={R.color.blue,R.color.green,R.color.orange,R.color.teal};
		static int color_pointer=0;
		public boolean isAdmin=false;
	
	/** Static structures */
	public static LinkedList<User> allPeople = new LinkedList<User>(); // List of all person's initiated, do not delete people from this list 
	public static HashMap<String,User> username_to_person = new HashMap<String,User>(); // Be able to access the User object through either the username 
	public static HashMap<String,User> nickname_to_person = new HashMap<String,User>(); // Be able to access the User object through either the nickname
    
	/* Constructor: int imageInt is a R.drawable int for an image
		1)Initiate all varibles
		2)Add this to linkedlist of all people created (allPeople)
		3)Add this to the MainSpace's list of people  TODO: I'm thinking maybe I should do this step AFTER I create this person
		4)Add (username, person) and (nickname, person) pairs to hashmap */
	
	public User(String username, String nickname, int imageInt, String description){
        Log.v(LOG_TAG, "Made a person for the user " + username);
		// (1)
        this.username = username;
        this.nickname = nickname;
        this.description = description;
        if(imageInt==-1) // Give them a default image if they have not already picked one
        	this.image = R.drawable.question;
        else
        	this.image = imageInt;
        // (2)
        allPeople.add(this);
        // (3)
       /* (MainApplication.mainspace).addPerson(this); */
        // (4)
        username_to_person.put(username, this);
        nickname_to_person.put(nickname, this);
        user_color=colors[color_pointer];
        Log.v(LOG_TAG, "COLOR USER"+color_pointer);
		color_pointer=(color_pointer==3?0:color_pointer+1);
	}
	
	
	// GETTERS
	
	/* Return person's username, the name that will show up on the screen under the icon
	 * Acts as the XMPP id as well */
	public String getUsername(){
        return username;
	}
	/* Return person's nickname, needed for network use */
	public String getNickname(){
        return nickname;
	}

	/* Get the String description of this person that the user can write for him/herself 
	 * Should be used when the user hovers over another person's icon, can then see description */
	public String getDescription(){
        return description;
	}
	/* Return the image int */
	public int getImage(){
		return image;
	}
	// SETTERS
	
	/* Change the user's username */
	public void setUsername(String new_username){
        username = new_username;
	}
	/* Change the user's nickname */
	public void setNickname(String new_nickname){
	   nickname = new_nickname;
	}
	/* Change the user's description */
	public void setDescription(String new_description){
        description = new_description;
    }
	
}
