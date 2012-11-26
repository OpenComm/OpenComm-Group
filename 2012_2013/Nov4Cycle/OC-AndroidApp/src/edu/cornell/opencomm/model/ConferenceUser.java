package edu.cornell.opencomm.model;

import android.graphics.Point;
import android.location.Location;


public class ConferenceUser {
	/**
	 * 
	 */
	public Point LOCATION;
	public User user;
	
	/*public ConferenceUser(String username, String nickname, int image) {
		super(username, nickname, image);
		// TODO Auto-generated constructor stub
		
	}*/
	
	public ConferenceUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	public int getX(){
		return LOCATION.x;
	}
	public int getY(){
		return LOCATION.y;
	}
	//TODO: delete this , location is public field
	public Point getLocation(){
		return LOCATION;
	}
	//TODO: delete this, location is public field
	public void setLocation(Point location){
		LOCATION = location;
	}
	
}
