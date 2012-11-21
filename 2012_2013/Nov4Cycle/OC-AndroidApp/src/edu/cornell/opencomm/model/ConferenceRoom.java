package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.graphics.Point;
import android.util.Log;
import edu.cornell.opencomm.network.NetworkService;

public class ConferenceRoom extends MultiUserChat{
	
	private Point center ;
	
	private double radius; 
	/**
	 * 
	 */
	
	private ArrayList<ConferenceUser> confUserList = new ArrayList<ConferenceUser>();
	
	private String roomID;
	private User moderator;
	
	public ConferenceRoom(String roomName){
		this(NetworkService.getInstance().getConnection(),roomName);
		
	}
	private static String formatRoomName(String roomName){
		//Kris: format the string as per the server expectation
		return roomName;
	}
	public ConferenceRoom(Connection c, String s) {
		super(c, formatRoomName(s));
		retriveOccupants();
	}
	private void retriveOccupants(){
		//Kris/BE: get the list of occupants from the super/muc and populate the participant maps
	}
	public String getRoomID(){
		return roomID;
	}
	public void setList(ArrayList<User> users){
		for(User user : users){
			ConferenceUser cu = new ConferenceUser(user);
			this.confUserList.add(cu);
		}
	}
	public User getModerator(){
		return moderator;
	}
	
	public void setModerator(User u){
		moderator = u;
	}
	
//	public void addConferenceUser(ConferenceUser confUser){
//		confUserList.add(confUser);
//	}
	public ArrayList<ConferenceUser> getCUserList(){
		return confUserList;
	}
	public  ArrayList<ConferenceUser> updateLocations(Point center,int radius){
		int noOfusers = confUserList.size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for(int i=0;i<pointList.size();i++){
			
			//confUserList.get(i).LOCATION = pointList.get(i);
			confUserList.get(i).setLocation(pointList.get(i));
			Log.d("ConfUser-xy - point", "x = " + pointList.get(i).x + ", y = " + pointList.get(i).y);
			Log.d("ConfUser-xy", "x = " + confUserList.get(i).getX() + ", y = " + confUserList.get(i).getY());
		}
		return confUserList;
	}
	private ArrayList<Point> getPoints(int users,double radius,Point center){
		double slice = 2 * Math.PI / users;
		ArrayList<Point> pointList  = new ArrayList<Point>();
		for (int i = 0; i < users; i++)
		{
			double angle = slice * i;
			int newX = (int)(center.x + radius * Math.cos(angle));
			int newY = (int)(center.y + radius * Math.sin(angle));
			Point p = new Point(newX, newY);
			pointList.add(p);
		}
		return pointList;
	}
} 