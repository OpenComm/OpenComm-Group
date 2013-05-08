package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.OCParticipantStatusListener;
import edu.cornell.opencomm.manager.UserManager;

public class Conference implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = Conference.class.getSimpleName();

	private String title = "";

	private ArrayList<User> users = new ArrayList<User>();

	private MultiUserChat chat;

	// CONSTRUCTORS

	public Conference(MultiUserChat muc) {
		chat = muc;
		Log.v(TAG, "creation " + chat.getRoom());
		users.add(UserManager.PRIMARY_USER); 
		//users = createExampleUsers(); 
		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));
	}

//	public Conference(MultiUserChat muc) {
//		this.chat = muc;
//		Log.v(TAG, "joined " + chat.getRoom());
//		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));
//	}

	//	public void createUsers(){
	//		Log.v(TAG, "there are "+users.size()+" users now");
	//		users = this.getActiveParticipants();
	//		Log.v(TAG, "there are "+users.size()+" users now");
	//	}

	/*
	 * Backend : A method that gets the participants that are currently active in this conference
	 * Active, meaning that the user has entered conference. 
	 */
	public ArrayList<User> getActiveParticipants(){
		ArrayList<User> users = new ArrayList<User>();
		Iterator<String> s = chat.getOccupants();
		while(s.hasNext()){
			String temp = s.next();
			String[] jid = temp.split("/");
			Log.v(TAG, "searching for jid: "+jid[1]);

			User u = SearchService.getUser(jid[1]);

			if(u==null) Log.v(TAG, "search service returned null");
			else{
				Log.v(TAG, "username: "+u.getUsername()+"   nickname: "+u.getNickname());
			}
			users.add(u);
		}
		return users;
	}

	/*
	 * Backend : If there are any users that have been invited- put them on waitlist
	 * 
	 */
	public boolean isWaiting(){
		if(InvitationsList.getNumberNewInvitations() > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	//get room name
	public String getRoom() {
		return chat.getRoom();
	}


	// API Functions

	public void addPresenceListener() {
		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));

	}

	public void join(String jid) {
		Log.v(TAG, "join " + chat.getRoom());
		try {
			Log.v(TAG, jid + " is trying to join the room");
			chat.join(jid);
			Log.v(TAG, "primary user successfully joined room");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void sendConfigurationForm(Form f) {
		try {
			chat.sendConfigurationForm(f);
			Log.v(TAG, "room config form successfully sent");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void invite(String JID, String reason) {
		Log.v(TAG, ""+ chat.isJoined());
		Log.v(TAG, ""+chat.getRoom());
		chat.invite(JID, reason);
	}

	public void leave() {
		chat.leave();
	}
	
	public void destroy(){
		chat = null;
	}

	public Collection<Occupant> getParticipants() {
		try {
			return chat.getParticipants();
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Helper Functions

	// Getters and Setters

	public void setTitle(String newTitle) {
		title = newTitle;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void addUser(User u) {
		users.add(u);
	}

	public void removeUser(User u) {
		users.remove(u);
	}

	public ArrayList<User> updateLocations(Point center, int radius) {
		ArrayList<Point> pointList = getPoints(users.size(), radius, center);
		for (int i = 0; i < pointList.size(); i++) {
			// confUserList.get(i).LOCATION = pointList.get(i);
			users.get(i).setLocation(pointList.get(i));
		}
		return users;
	}

	private ArrayList<Point> getPoints(int users, double radius, Point center) {
		ArrayList<Point> pointList = new ArrayList<Point>();
		Log.d("ConferenceRoom", "Radius :" + radius);
		Log.d("ConferenceRoom", "Center :" + center.toString());
		Log.d("ConferenceRoom", "Users :" + users);
		double startAngle = Math.toRadians(90);
		double endAngle = Math.toRadians(360);
		double slice = 2 * Math.PI / users;
		center.x = center.x - 65;
		center.y = center.y - 65;
		for (int i = 0; i < users; i++) {
			double angle = (startAngle + slice * i) % endAngle;
			int newX = (int) (center.x + radius * Math.cos(angle));
			int newY = (int) (center.y + radius * Math.sin(angle));
			Point p = new Point(newX, newY);
			Log.d("ConferenceRoom", "Angle :" + angle);
			Log.d("ConferenceRoom", "Point :" + p);
			pointList.add(p);
		}

		return pointList;

	}

	private ArrayList<User> createExampleUsers() {
		UserManager.userColorTable.put("oc1testorg", Color.YELLOW);
		UserManager.userColorTable.put("oc2testorg", Color.GREEN);
		UserManager.userColorTable.put("oc3testorg", Color.BLUE);
		UserManager.userColorTable.put("oc4testorg", Color.YELLOW);
		ArrayList<User> users = new ArrayList<User>();
		users.add(UserManager.PRIMARY_USER);
		users.add(new User("oc1testorg", "Nora Ng-Quinn",
				R.drawable.example_picture_1));
		users.add(new User("oc2testorg", "Risa Naka",
				R.drawable.example_picture_2));
		users.add(new User("oc3testorg", "Kris Kooi",
				R.drawable.example_picture_3));
		users.add(new User("oc4testorg", "Ankit Singh",
				R.drawable.example_picture_4));
		return users;
	}

	public void hasLeft(User pRIMARY_USER) {
		// TODO Auto-generated method stub

	}

}
