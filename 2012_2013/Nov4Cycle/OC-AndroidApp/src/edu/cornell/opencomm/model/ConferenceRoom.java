package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.graphics.Point;
import android.util.Log;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class ConferenceRoom extends MultiUserChat{
	
	String TAG = "ConferenceRoom";
	
	private Point center ;
	
	private double radius; 
	/**
	 * 
	 */
	
	private ArrayList<ConferenceUser> confUserList = new ArrayList<ConferenceUser>();
	
	private String roomID;
	private static String rName;
	private User moderator;
	
	public ConferenceRoom(String roomName){
		this(NetworkService.getInstance().getConnection(),roomName);
		
	}
	private static String formatRoomName(String roomName){
		//Kris: format the string as per the server expectation
		roomName = roomName + "@conference.cuopencomm";
		rName = roomName;
		return roomName;
	}
	
	public ConferenceRoom(Connection c, String s, User u) {
		super(c, formatRoomName(s));
		roomID = s;
		moderator = u;
		retriveOccupants();
	}
	
	public ConferenceRoom(Connection c, String s){
		super(c, formatRoomName(s));
		roomID = s;
		retriveOccupants();
	}
	
	public void init(boolean isMod){
		if(isMod){
	        try {
				super.join(UserManager.PRIMARY_USER.nickname);
			} catch (XMPPException e) {
				Log.e(TAG, "could not create room", e);
			}

	        //Configure room
	        Form form;
	        Form answerForm = null;
			try {
				form = this.getConfigurationForm();
				answerForm = form.createAnswerForm();
		        for (Iterator<FormField> fields = form.getFields(); fields.hasNext();){
		            FormField field = (FormField) fields.next();
		            if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null){
		                answerForm.setDefaultAnswer(field.getVariable());
		            }
		        }
		        answerForm.setAnswer("muc#roomconfig_moderatedroom", true);
			} catch (XMPPException e1) {
				Log.e(TAG, "Could not get configuration form");
			}
	        try {
				this.sendConfigurationForm(answerForm);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not send configuration form");
			}
		}
		else{
	        try {
				this.join(rName);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not join room", e);
			}
		}
		// Create and instantiate all existing users
        Iterator<String> occItr = this.getOccupants();
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
	
	//NOTE: cannot revoke privileges, so when leaving, just grant 
		//privileges to someone else and then leave.
	public void updateMod(User currMod, User newMod){
		try {
			this.grantModerator(newMod.getNickname());
			setModerator(newMod);
		} catch (XMPPException e) {
			Log.e(TAG, "Could not transfer privileges", e);
		}
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