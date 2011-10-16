package edu.cornell.opencomm;

import java.util.LinkedList;
import android.util.Log;
import android.content.Context;

/** A Space is a chat room that holds a group of people who can talk to 
 * one another. This is just the room object holding the data, its contents are displayed in a SpaceView 
 * 
 * A "mainspace" is a space where you can drag in anyone you like. You have sole control over this room
 * TODO should we change this?  
 */

public class Space{
	private static String LOG_TAG="OC_Space"; // for error checking
	Context context;
	LinkedList<Person> allPeople= new LinkedList<Person>(); // The people who are in this Space
	LinkedList<PersonView> allIcons= new LinkedList<PersonView>(); // The icons of all the people in this space
    int spaceID; // the ID # that the network will use to identify this space
    boolean isMainSpace; // Is true if this is a mainspace and NOT a privateSpace
    Person moderator; // the person who has the power to manage the PrivateSpace, this person has special priveleges
	boolean screen_on; // The SpaceView object(UI screen) that shows this Space (room) on the UI screen
    PrivateSpaceView bottomIcon; // The icon at the scrollable bottom that represents this Space
    public static LinkedList<Space> allSpaces= new LinkedList<Space>(); // All the rooms that have been created ever
    
	
	/* Constructor: A completely NEW space
     * isMainSpace is true if this is a mainspace, mainspaces should be 
     * initiated in the beginning and should initialize allSpaces
	 * 1) Initialize all variables - create a new 
       2) Add this space to the list of all spaces
       3) Set up a PrivateSpaceView */
	public Space(Context context, boolean isMainSpace, int spaceID, Person moderator){
		//Log.v(LOG_TAG, "Creating a self-created SPACE");
		this.context = context;
        // (1)
		//allPeople = new LinkedList<Person>();
        this.spaceID = spaceID;
        this.isMainSpace = isMainSpace;
        this.moderator = moderator;
        // (2)
        allSpaces.add(this);
        // (3) 
        bottomIcon = new PrivateSpaceView(context, this);
	}
    
    /* Constructor: An already existing privateSpace that YOU have just joined
     * 1) existingPeople - list of people who are already in the privatespace
     * 2) the ID of the privatespace so that the network can identify it with the others
     * Make sure to still make a SpaceView object and pass the existingPeople in,
     * also add this space to allSpaces. Don't forget to set up a PrivateSpaceView */
    public Space(Context context, LinkedList<Person> existingPeople, int spaceID, Person moderator){
    	//Log.v(LOG_TAG, "Creating an already existing SPACE");
    	this.context = context;
        //allPeople = existingPeople;
    	addManyPeople(existingPeople);
        this.spaceID = spaceID;
        this.isMainSpace = false;
        this.moderator = moderator;
        allSpaces.add(this);
        bottomIcon = new PrivateSpaceView(context, this);
    }
    
    /* Create the SpaceView (screen) for this space */
  /*  public void addSpaceView(SpaceView sv){
    	screen = sv;
    	sv.invalidate();
    } */
	
    /* Delete this private space from the static list of existing Spaces (allSpaces),
     * however you cannot delete a mainspace, also delete this space's corresponding
     * PS icon */
    public void deletePrivateSpace(){
        if(!isMainSpace){
            allSpaces.remove(this); 
            bottomIcon.deleteThisPSView();
        }
    }
    
    /* Add many people to this Space, make sure to add icons to the SpaceView for all these people */
    public void addManyPeople(LinkedList<Person> people){
        for(Person person : people){
           /* allPeople.add(person);
            PersonView icon = new PersonView(context, person, person.getImage());
            allIcons.add(icon);
            if(screen_on)
            	(MainApplication.screen).addPerson(icon); */
        	addPerson(person);
        }
    }
    
	/* Add this Person to this Space (room), also create a new icon (PersonView) for 
     * add it to this Space's corresponding SpaceView 
	public void addPerson(Person newPerson){
        // Check to make sure this person is not already in this space
		boolean already_have = false;
        int counter=0;
        while(!already_have && counter<allPeople.size()){
        	if(allPeople.get(counter)==newPerson)
        		already_have = true;	
        	counter++;
        }
        if(!already_have){
        	allPeople.add(newPerson);
        	PersonView icon = new PersonView(context, newPerson, newPerson.getImage());
        	allIcons.add(icon);
        	if(screen_on)
        		(MainApplication.screen).addPerson(icon);
        }
        else{
        	Log.i( "Person already present", "Check your code" );
        }
		
	}*/
    
    // TODO : Add users
    public void addPerson(Person newPerson){
        // Check to make sure this person is not already in this space
		boolean already_have = false;
        int counter=0;
        while(!already_have && counter<allPeople.size()){
        	if(allPeople.get(counter).username==newPerson.username)
        		already_have = true;	
        	counter++;
        }
        
        if(!already_have){
        	allPeople.add(newPerson);
        	PersonView icon = new PersonView(context, newPerson, newPerson.getImage());
        	allIcons.add(icon);
        	if(screen_on)
        		(MainApplication.screen).addPerson(icon);
        }
        else{
        	Log.i( "Person already present", "Check your code" );
        }
		
	}
    
	/* Remove a Person from this Space (room), make sure to delete the icon (PersonView)
     * from this Space's corresponding SpaceView*/
	public void removePerson(Person badPerson){
        allPeople.remove(badPerson);
        
        PersonView found_icon = null;
   	    int counter = 0;
   	    while(found_icon==null && counter<allIcons.size()){
   		    if(allIcons.get(counter).getPerson()==badPerson)
   			    found_icon = allIcons.get(counter);
   		    counter++;
   	    }
   	    if(found_icon!=null)
   		    allIcons.remove(found_icon);
        
        if(screen_on){
        	(MainApplication.screen).deletePerson(badPerson);
        }
        
	}
	
	/* Set the moderator of this space. Cannot change the moderator from YOU if 
	 * this space is a mainspace */
	public void setModerator(Person moderator){
		if(!isMainSpace())
			this.moderator = moderator;
	}
    
	/* Set whether the space is shown on screen or not */
	public void setScreenOn(boolean isIt){
		screen_on= isIt;
	}
	
    //GETTERS
    
    /* Return the list of all the people who are in this space */
    public LinkedList<Person> getAllPeople(){
        return allPeople;
    }
    /* Return the list of all the icons that are being drawn in this space */
    public LinkedList<PersonView> getAllIcons(){
    	return allIcons;
    }
    /* Return true if this Space is a mainspace, false otherwise */
    public boolean isMainSpace(){
        return isMainSpace;
    }
    /* Return the id of this space */
    public int getSpaceID(){
    	return spaceID;
    }
    /* Return the moderator of this space, (will be YOU if this is a mainspace) */
    public Person getModerator(){
        return moderator;
    }
    /* Return true if this space is being shown on the screen */
    public boolean isScreenOn(){
        return screen_on;
    }
    /* Return the bottomIcon (PrivateSpaceView) for this Space */
    public PrivateSpaceView getPSView(){
        return bottomIcon;
    }

}


