package edu.cornell.opencomm.model;

import android.graphics.Point;


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
		return 10;
	}
	public int getY(){
		return LOCATION.y;
	}
	
	public Point getLocation(){
		return LOCATION;
	}
	
	public void setLocation(Point location){
		LOCATION = location;
	}
	
}
