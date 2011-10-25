package edu.cornell.opencomm.model;

import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.controller.Login;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.UserView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import android.util.Log;
import android.content.Context;

/** A Space is a chat room that holds a group of people who can talk to 
 * one another. This is just the room object holding the data, its contents are displayed in a SpaceView 
 * 
 * A "mainspace" is a space where you can drag in anyone you like. You have sole control over this room
 * TODO should we change this?  
 */

public class Space{
	// Debugging
	private static String LOG_TAG="OC_Space"; // for error checking
	private static boolean D = true;
	
	Context context;
	LinkedList<User> allPeople= new LinkedList<User>(); // The people who are in this Space
	LinkedList<UserView> allIcons= new LinkedList<UserView>(); // The icons of all the people in this space
    String spaceID; // the ID # that the network will use to identify this space
    boolean isMainSpace; // Is true if this is a mainspace and NOT a privateSpace
    User moderator; // the person who has the power to manage the PrivateSpace, this person has special priveleges
	boolean screen_on; // The SpaceView object(UI screen) that shows this Space (room) on the UI screen
    PrivateSpaceIconView bottomIcon; // The icon at the scrollable bottom that represents this Space
    public static LinkedList<Space> allSpaces= new LinkedList<Space>(); // All the rooms that have been created ever
    
    // Network variables
    private MultiUserChat muc;
    private String roomID;
	
	/* Constructor: A completely NEW space
     * isMainSpace is true if this is a mainspace, mainspaces should be 
     * initiated in the beginning and should initialize allSpaces
	 * 1) Initialize all variables - create a new 
       2) Add this space to the list of all spaces
       3) Set up a PrivateSpaceIconView */
	public Space(Context context, boolean isMainSpace, String spaceID, User moderator){
		//Log.v(LOG_TAG, "Creating a self-created SPACE");
		this.context = context;
        // (1)
		//allPeople = new LinkedList<User>();
        this.spaceID = spaceID;
        this.roomID = Network.ROOM_NAME + this.spaceID + "@conference.jabber.org";
        this.isMainSpace = isMainSpace;
        this.moderator = moderator;
        if (moderator == null) {
        	if (D) Log.d(LOG_TAG, "Moderator is null");
        	this.moderator = MainApplication.user_you;
        }
        this.muc = new MultiUserChat(Login.xmppService.getXMPPConnection(), this.roomID);
        try {
        	if (D) Log.d(LOG_TAG, "Is MUC null? " + (this.muc == null));
        	if (D) Log.d(LOG_TAG, "Is moderator's nickname null?" + (moderator == null));
			this.muc.join(this.moderator.getNickname());
			this.muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			this.muc.addInvitationRejectionListener(new InvitationRejectionListener() {
				public void invitationDeclined(String arg0, String arg1) {
					if (D) Log.d(LOG_TAG, "onCreate: invitation declined by " + arg0);		
				}			
			});
		} catch (XMPPException e) {
			Log.e(LOG_TAG, "Room could not be created");
			e.printStackTrace();
		}
        // (2)
        allSpaces.add(this);
        // (3) 
        bottomIcon = new PrivateSpaceIconView(context, this);
	}
    
    /* Constructor: An already existing privateSpace that YOU have just joined
     * 1) existingPeople - list of people who are already in the privatespace
     * 2) the ID of the privatespace so that the network can identify it with the others
     * Make sure to still make a SpaceView object and pass the existingPeople in,
     * also add this space to allSpaces. Don't forget to set up a PrivateSpaceIconView */
    public Space(Context context, LinkedList<User> existingPeople, String roomID, User moderator){
    	//Log.v(LOG_TAG, "Creating an already existing SPACE");
    	this.context = context;
        //allPeople = existingPeople;
    	addManyPeople(existingPeople);
        this.spaceID = String.valueOf(Integer.parseInt(roomID));
        this.roomID = roomID;
        this.isMainSpace = false;
        this.moderator = moderator;
        this.muc = new MultiUserChat(Login.xmppService.getXMPPConnection(), roomID);
        try {
			this.muc.join(MainApplication.user_you.getNickname());
		} catch (XMPPException e) {
			Log.e(LOG_TAG, "Could not join room " + this.roomID);
			e.printStackTrace();
		}
        allSpaces.add(this);
        bottomIcon = new PrivateSpaceIconView(context, this);
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
            try {
                allSpaces.remove(this); 
                bottomIcon.deleteThisPSView();
				this.muc.destroy(null, null);
			} catch (XMPPException e) {
				Log.e(LOG_TAG, "This space cannot be destroyed");
				e.printStackTrace();
			}
        }
    }
    
    /* Add many people to this Space, make sure to add icons to the SpaceView for all these people */
    public void addManyPeople(LinkedList<User> people){
        for(User person : people){
           /* allPeople.add(person);
            UserView icon = new UserView(context, person, person.getImage());
            allIcons.add(icon);
            if(screen_on)
            	(MainApplication.screen).addPerson(icon); */
        	addPerson(person);
        }
    }
    
	/* Add this User to this Space (room), also create a new icon (UserView) for 
     * add it to this Space's corresponding SpaceView 
	public void addPerson(User newPerson){
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
        	UserView icon = new UserView(context, newPerson, newPerson.getImage());
        	allIcons.add(icon);
        	if(screen_on)
        		(MainApplication.screen).addPerson(icon);
        }
        else{
        	Log.i( "User already present", "Check your code" );
        }
		
	}*/
    
    public void addPerson(User newPerson){
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
        	if (D) Log.d(LOG_TAG, "addPerson: inviting user " + newPerson.getUsername() + " to room " + this.roomID);
        	this.muc.invite(newPerson.getUsername(), Network.DEFAULT_INVITE);
        	UserView icon = new UserView(context, newPerson, newPerson.getImage());
        	allIcons.add(icon);
        	if(screen_on)
        		(MainApplication.screen).addPerson(icon);
        }
        else{
        	Log.i( "User already present", "Check your code" );
        }
		
	}
    
	/* Remove a User from this Space (room), make sure to delete the icon (UserView)
     * from this Space's corresponding SpaceView*/
	public void removePerson(User badPerson) throws XMPPException{
        allPeople.remove(badPerson);
        
        UserView found_icon = null;
   	    int counter = 0;
   	    while(found_icon==null && counter<allIcons.size()){
   		    if(allIcons.get(counter).getPerson()==badPerson)
   			    found_icon = allIcons.get(counter);
   		    counter++;
   	    }
   	    if(found_icon!=null) {
   		    allIcons.remove(found_icon);
   		    if (D) Log.d(LOG_TAG, "Attempt to kick out " + badPerson.getUsername());
   		    this.muc.kickParticipant(badPerson.getNickname(), Network.DEFAULT_KICKOUT);
   		    if (D) Log.d(LOG_TAG, "Kicked out " + badPerson.getUsername());
   	    }
        
        if(screen_on){
        	(MainApplication.screen).deletePerson(badPerson);
        }
        
	}
	
	/* Set the moderator of this space. Cannot change the moderator from YOU if 
	 * this space is a mainspace */
	public void setModerator(User moderator){
		if(!isMainSpace())
			this.moderator = moderator;
			try {
				this.muc.grantAdmin(moderator.getUsername());
			} catch (XMPPException e) {
				e.printStackTrace();
				Log.e(LOG_TAG, "Could not grant administrator privilege to " + moderator.getUsername());
			}
	}
    
	/* Set whether the space is shown on screen or not */
	public void setScreenOn(boolean isIt){
		screen_on= isIt;
	}
	
    //GETTERS
    
    /* Return the list of all the people who are in this space */
    public LinkedList<User> getAllPeople(){
        return allPeople;
    }
    /* Return the list of all the icons that are being drawn in this space */
    public LinkedList<UserView> getAllIcons(){
    	return allIcons;
    }
    /* Return true if this Space is a mainspace, false otherwise */
    public boolean isMainSpace(){
        return isMainSpace;
    }
    /* Return the id of this space */
    public String getSpaceID(){
    	return spaceID;
    }
    /* Return the moderator of this space, (will be YOU if this is a mainspace) */
    public User getModerator(){
        return moderator;
    }
    /* Return true if this space is being shown on the screen */
    public boolean isScreenOn(){
        return screen_on;
    }
    /* Return the bottomIcon (PrivateSpaceIconView) for this Space */
    public PrivateSpaceIconView getPSView(){
        return bottomIcon;
    }

}


