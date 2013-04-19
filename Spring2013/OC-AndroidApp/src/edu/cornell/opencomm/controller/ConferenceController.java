package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.Conference_Dummy;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceController {
	ConferenceView view;
	Conference room;

	private static final String TAG = "ConferenceController_v2";
	private static final boolean D = true;
	private static ConferenceController _instance;
	private ArrayList<User> confUserList = this.createExampleUsers(); 

	public static ConferenceController getInstance() {
		if (_instance == null) {
			_instance = new ConferenceController();
		}
		return _instance;
	}

	private ConferenceController() {
		this.view = ConferenceView.getInstance();
		String roomID = null;

		// TODO: Move constructors to more sensible place
		while (this.room == null) {
			roomID = NetworkService.generateRoomID();
			try {
				this.room = new Conference(roomID);
				Log.v(TAG, "created new room");
			} catch (Exception e) {
				Log.v(TAG, e.getMessage());
				continue;
			}
		}
		this.room.join("oc1testorg@opencomm");
		
		this.room.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	}

	/**
	 * Change the title of the conference (conference name)
	 * 
	 * @param title
	 *            the new title
	 * @return void
	 */
	public void setTitle(String title) {
		room.setTitle(title);
		view.txtv_ConfTitle.setText(title);
	}

	/**
	 * handle the action when add person button was clicked
	 */
	public void HandleAddPerson(String username) {
		if (D)
			Log.d(TAG, "addPerson button clicked");
		room.invite(username, "lets chat");
	}

	/**
	 * handle the action when overflow button was clicked
	 */
	public void HandleOverflow() {
		if (D)
			Log.d(TAG, "Overflow button clicked");
		// TODO: add "Handle overflow" functions here
	}

	/**
	 * handle the action when back button was clicked
	 */
	public void HandleBackButton() {
		if (D)
			Log.d(TAG, "back button clicked");

		// start dashboard view
		Intent account = new Intent(this.view, DashboardView.class);
		this.view.startActivity(account);
	}

	public ArrayList<User> getUsers(){
		return room.getUsers();
	}

	public void HandleLeaveButton() {
		if (D)
			Log.d(TAG, "leave button clicked");

		room.leave();

		if (room.getParticipants().isEmpty()) {
			room = null;
		}
	}

//	public ArrayList<User> getCUserList() {
//		confUserList = createExampleUsers();
//		return confUserList;
//	}

	private ArrayList<User> createExampleUsers(){
		UserManager.userColorTable.put("oc1testorg", Color.YELLOW);
		UserManager.userColorTable.put("oc2testorg", Color.GREEN);
		UserManager.userColorTable.put("oc3testorg", Color.BLUE);
		UserManager.userColorTable.put("oc4testorg", Color.YELLOW);
		ArrayList<User> users = new ArrayList<User>();
		users.add(UserManager.PRIMARY_USER);
		users.add(new User("oc1testorg", "Nora Ng-Quinn", R.drawable.example_picture_1));
		users.add(new User("oc2testorg", "Risa Naka", R.drawable.example_picture_2));
		users.add(new User("oc3testorg", "Kris Kooi", R.drawable.example_picture_3));
		users.add(new User("oc4testorg", "Ankit Singh", R.drawable.example_picture_4));
		return users;
	}

	//TODO
	public ArrayList<User> updateLocations(Point center, int radius) {
		int noOfusers = confUserList.size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for (int i = 0; i < pointList.size(); i++) {

			// confUserList.get(i).LOCATION = pointList.get(i);
			confUserList.get(i).setLocation(pointList.get(i));
		}
		return confUserList;
	}

//	public ArrayList<User> getCUserList() {
//		return room.getUsers();
//	}

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

	public Conference_Dummy getCurrentConference() {
		Conference_Dummy dummy = new Conference_Dummy("Happening Now", confUserList);
		System.out.println("Why is dummy dm?" + dummy==null); 
		return dummy; 
	}

}
