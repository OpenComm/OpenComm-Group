package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.InvitationsList;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceController {
	static ConferenceView view;
	Conference room;

	private static final String TAG = "ConferenceController";
	private static final boolean D = true;
	private static ConferenceController _instance;
	private ArrayList<User> confUserList = this.createExampleUsers();

	public static ConferenceController getInstance(ConferenceView view, MultiUserChat muc) {
		if (_instance == null) {
			_instance = new ConferenceController(view, muc);
		}
		return _instance;
	}

	public Conference getRoom() {
//		room = new Conference("Dummy"); 
		return room;
	}

	private ConferenceController(ConferenceView view, MultiUserChat muc) {
		this.view = view;
		try {
			this.room = new Conference(muc);
//			this.room.join(UserManager.PRIMARY_USER.getUsername());
//			this.room.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			Log.v(TAG, "successfully created room " + this.room.toString());
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
		}
		
		//this.room.createUsers();
	}

	/**
	 * Change the title of the conference (conference name)
	 * 
	 * @param title
	 *            the new title
	 * @return void
	 */
//	public void setTitle(String title) {
//		room.setTitle(title);
//		view.conference_title.setText(title);
//	}

	/**
	 * handle the action when add person button was clicked
	 */
	public void HandleAddPerson(String username) {
		room.invite(username, "let's chat");
	}


	
	/**
	 * handle the action when leave button was clicked
	 */
	public void HandleLeave() {
		if (D)
			Log.d(TAG, "Leave button clicked");
		HandleLeaveButton();
	}
	
	/**
	 * handle the action when setting button was clicked
	 */
	public void HandleSetting() {
		if (D)
			Log.d(TAG, "Setting button clicked");
		// TODO: switch activity to the My Profile view
	}

	/**
	 * handle the action when back button was clicked
	 */
	public void HandleBackButton() {
		if (D) {
			Log.d(TAG, "back button clicked");
			Log.d(TAG, this.view.toString());
		}
		// start dashboard view
		Log.v(TAG, "is this.view null? "+(this.view==null));
		Log.v(TAG, "is DashboardView.class null? "+(DashboardView.class==null));
		Intent account = new Intent(this.view, DashboardView.class);
		this.view.startActivity(account);
	}

	public ArrayList<User> getUsers() {
		return room.getUsers();
	}

	public void HandleLeaveButton() {
		if (D)
			Log.d(TAG, "leave button clicked");

		InvitationsList.getInvitations().remove(InvitationsList.getPosition(room.getRoom()));
		room.leave();

		room.hasLeft(UserManager.PRIMARY_USER);
		if (room.getActiveParticipants().isEmpty()) {
			Log.v(TAG, "setting room to null");
			room.destroy();
			room = null;
			_instance = null;
		}
		
		Log.v(TAG, "switching activities");
		Intent i = new Intent(this.view, DashboardView.class); 
		this.view.startActivity(i); 
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

	// TODO
	public ArrayList<User> updateLocations(Point center, int radius) {
		int noOfusers = confUserList.size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for (int i = 0; i < pointList.size(); i++) {

			confUserList.get(i).setLocation(pointList.get(i));
		}
		return confUserList;
	}

	private ArrayList<Point> getPoints(int users, double radius, Point center) {
		Log.d("ConferenceRoom", "Radius :" + radius);
		Log.d("ConferenceRoom", "Center :" + center.toString());
		Log.d("ConferenceRoom", "Users :" + users);
		double startAngle = Math.toRadians(90);
		double endAngle = Math.toRadians(360);
		double slice = 2 * Math.PI / users;
		center.x = center.x - 65;
		center.y = center.y - 65;

		ArrayList<Point> pointList = new ArrayList<Point>();
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
