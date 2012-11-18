package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.graphics.Point;

public class ChatSpaceModel extends MultiUserChat{
	
	//TODO:Is it a good idea to extend MUC?
	//Ask Risa and Kris
	//How does MUC get list of occupants
	/**
	 * 
	 */
	HashMap<String, User> userMap = new HashMap<String, User>();
	HashMap<User, Point> userLocationMap = new HashMap<User, Point>();
	
	String roomID;
	User moderator;
	
	public ChatSpaceModel(Connection c, String s) {
		super(c, s);
	}
	
	
	//TODO: don't worry about these for Nov4 cycle
	public void updateUserLocation(User u, Point location){
		
	}
	public void getUserLocation(User u){
		
	}
public User getModerator(){
	return moderator;
}
 
	
} 