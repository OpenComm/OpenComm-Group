package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.graphics.Point;
import android.util.Log;

import edu.cornell.opencomm.controller.OCParticipantStatusListener;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class Conference implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = Conference.class.getSimpleName();

	private String title = "";

	private ArrayList<User> users = new ArrayList<User>();

	private MultiUserChat chat;

	// CONSTRUCTORS

	public Conference(String roomName) {
		chat = new MultiUserChat(NetworkService.getInstance().getConnection(),
				roomName);
		Log.v(TAG, "creation " + chat.getRoom());
		users.add(UserManager.PRIMARY_USER); 
		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));
	}

	public Conference(MultiUserChat muc) {
		this.chat = muc;
		Log.v(TAG, "joined " + chat.getRoom());
		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));
	}
	
	/*
	 * Backend TODO: A method that gets the participants that are currently active in this conference
	 * Active, meaning that the user has entered conference. 
	 */
	public ArrayList<User> getActiveParticipants(){
		return null; 
	}
	
	/*
	 * Backend TODO: If there are any users that have been invited- put them on waitlist
	 * 
	 */
	public boolean isWaiting(){
		return true; 
	}
	
	/*
	 * Backend TODO: Any time a user leaves the conference, the users in conference has to updated
	 * This function returns a user who has left the conference
	 */
	public User hasLeft(){
		return null; 
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
		chat.invite(JID, reason);
	}

	public void leave() {
		chat.leave();
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

	// TODO
	public ArrayList<User> updateLocations(Point center, int radius) {
		int noOfusers = this.getUsers().size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for (int i = 0; i < pointList.size(); i++) {
			// confUserList.get(i).LOCATION = pointList.get(i);
			users.get(i).setLocation(pointList.get(i));
		}
		return users;
	}

	private ArrayList<Point> getPoints(int users, double radius, Point center) {
		ArrayList<Point> pointList = new ArrayList<Point>();
		if (users == 1){
			int x = center.x - 65; 
			int y = center.y -65;
			Point a = new Point(x, y); 
			pointList.add(a); 
			return pointList; 
		}else{
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
	}

}
