package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.network.NetworkService;

import android.graphics.Point;

public class ConferenceRoom extends MultiUserChat{
	
	
	/**
	 * 
	 */
	HashMap<User, Point> userLocationMap = new HashMap<User, Point>();
	
	// The users who are in this Space, <JID, User>
    private HashMap<String, User> allParticipants = new HashMap<String, User>();
    
    private HashMap<String, User> allNicks = new HashMap<String, User>();
	
	String roomID;
	User moderator;
	
	public ConferenceRoom(String roomName){
		this(NetworkService.getInstance().getConnection(),roomName);
	}
	private static String formatRoomName(String roomName){
		//Kris: format the string as per the server expectation
		return roomName;
	}
	public ConferenceRoom(Connection c, String s) {
		super(c, formatRoomName(s));
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
	
	public void addUser(User user){
		allParticipants.put(user.getUsername(), user);
		allNicks.put(user.getNickname(), user);
		userLocationMap.put(user, getUserLocation(user));
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
	
	//TODO - used in updateForNewUser() method above to temporarily
	//indicate method of finding the location to map with the new user in userLocationMap
	public Point getUserLocation(User u){
		return null;
	}
} 