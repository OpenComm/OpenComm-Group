package edu.cornell.opencomm.model;

import java.util.ArrayList;

import android.graphics.Point;
import edu.cornell.opencomm.interfaces.OCUpdateListener;


public class ConferenceUser {
	/**
	 * 
	 */
	private Point LOCATION;
	public User user;
	ArrayList<OCUpdateListener> locationUpdateListners = new ArrayList<OCUpdateListener>();
	
	public ConferenceUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	public void addLocationUpdateListner(OCUpdateListener listner){
		locationUpdateListners.add(listner);
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
		
		for(OCUpdateListener lis : locationUpdateListners){
			lis.onUpdate(100, new String("location changed"));
		}
	}
	
}
