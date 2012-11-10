package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.graphics.Point;

public class ChatSpaceModel extends MultiUserChat{
	
	//Ask Risa and Kris
	//How does MUC get list of occupants
	/**
	 * 
	 */
	HashMap<User, Point> userLocationMap = new HashMap<User, Point>();
	
	// The users who are in this Space, <JID, User>
    private HashMap<String, User> allParticipants = new HashMap<String, User>();
    
    private HashMap<String, User> allNicks = new HashMap<String, User>();
	
	String roomID;
	User moderator;
	
	public ChatSpaceModel(Connection c, String s) {
		super(c, s);
	}
	
	public User getModerator(){
		return moderator;
	}
	
	public void setModerator(User u){
		moderator = u;
	}
	
	/**
     * @return allNicknames
     */
    public HashMap<String, User> getAllNicknames() {
        return allNicks;
    }
	
	/**
     * @return - all participants in Space, maps JID to User
     *  */
    public HashMap<String, User> getAllParticipants() {
        return allParticipants;
    }
	
	//TODO: don't worry about these for Nov4 cycle
	public void updateUserLocation(User u, Point location){
		
	}
	
	public void getUserLocation(User u){
		
	}
} 