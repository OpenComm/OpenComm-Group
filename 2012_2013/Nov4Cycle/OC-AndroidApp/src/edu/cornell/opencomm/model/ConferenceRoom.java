package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.graphics.Point;

public class ConferenceRoom extends MultiUserChat{
	
	//Ask Risa and Kris
	//How does MUC get list of occupants
	/**
	 * 
	 */
	HashMap<User, Point> userLocationMap = new HashMap<User, Point>();
	
	// The users who are in this Space, <JID, User>
    private HashMap<String, ConferenceUser> allParticipants = new HashMap<String, ConferenceUser>();
    
    private HashMap<String, User> allNicks = new HashMap<String, User>();
	
	String roomID;
	User moderator;
	
	public ConferenceRoom(Connection c, String s) {
		super(c, s);
	}
	
	public String getRoomID(){
		return roomID;
	}
	
	public User getModerator(){
		return moderator;
	}
	
	public void setModerator(User u){
		moderator = u;
	}
	
	public void updateForNewUser(User u){
		allParticipants.put(u.getUsername(), (ConferenceUser) u);
		allNicks.put(u.getNickname(), u);
		userLocationMap.put(u, getUserLocation(u));
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
    public HashMap<String, ConferenceUser> getAllParticipants() {
        return allParticipants;
    }
	
	//TODO: don't worry about these for Nov4 cycle
	public void updateUserLocation(User u, Point location){
		
	}
	
	//TODO - used in updateForNewUser() method above to temporarily
	//indicate method of finding the location to map with the new user in userLocationMap
	public Point getUserLocation(User u){
		return null;
	}
} 