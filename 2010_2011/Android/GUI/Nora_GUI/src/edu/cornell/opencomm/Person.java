package edu.cornell.opencomm;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

/* An object representing a user who is taking part in the conversation */

public class Person{
	private static String LOG_TAG = "NORA_Person";
	Context context;
	/** The Essentials */
	String username; // The username the user chose, will show up on screen, is the XMPP id as well
	String nickname; // The nickname the user chose, needed for the network service
	int image; // the R.drawable int for this image
	PersonView icon; // The graphical icon that will show up on the UI representing this person
	
	/** Static structures */
	public static LinkedList<Person> allPeople = new LinkedList<Person>(); // List of all person's initiated, do not delete people from this list 
	public static HashMap<String,Person> username_to_person = new HashMap<String,Person>(); // Be able to access the Person object through either the username 
	public static HashMap<String,Person> nickname_to_person = new HashMap<String,Person>(); // Be able to access the Person object through either the nickname
    
	/** Not so important */
	// Short description of the person that will 
	// show up when you hover on this person's icon
	String description; 
	
	/* Constructor: int imageInt is a R.drawable int for an image
		1)Initiate all varibles
		2)Add this to linkedlist of all people created (allPeople)
		3)Add this to the MainSpace's list of people  TODO: I'm thinking maybe I should do this step AFTER I create this person
		4)Add (username, person) and (nickname, person) pairs to hashmap
		5)Create an icon (PersonView) for this person and pass in this and imageInt,
          pass in -1 for imageInt if you do not want to input an imageInt yet*/
	public Person(/*Context context,*/ String username, String nickname, int imageInt, String description){
        Log.v(LOG_TAG, "Made a person for the user " + username);
       // this.context = context;
		// (1)
        this.username = username;
        this.nickname = nickname;
        this.description = description;
        if(imageInt!=-1)
        	this.image = R.drawable.question;
        else
        	this.image = image;
        // (2)
        allPeople.add(this);
        // (3)
       /* Log.v(LOG_TAG, "GOING to add this person to the space");
        (MainApplication.mainspace).addPerson(this);
        Log.v(LOG_TAG, "FINISHED adding this person to the space"); */
        // (4)
        username_to_person.put(username, this);
        nickname_to_person.put(nickname, this);
        // (5)
        // TODO actually might not need this step, for now I made another separate method createIcon(Context context) so we can control when to make this.
	}
	
	/** Create an icon for this person to be viewed in the given context */
	public void createIcon(Context context){
			icon = new PersonView(context, this, image);
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
	/* Return the PersonView Object (graphical icon) for this person */
	public PersonView getPersonView(){
        return icon;
	}
	/* Get the String description of this person that the user can write for him/herself 
	 * Should be used when the user hovers over another person's icon, can then see description */
	public String getDescription(){
        return description;
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
