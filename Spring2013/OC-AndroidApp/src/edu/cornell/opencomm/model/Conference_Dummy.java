package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

public class Conference_Dummy {

	private String title; 
	
	private ArrayList<User>confUsersList = new ArrayList<User>(); 

	public Conference_Dummy(String title, ArrayList<User>attendees){
		this.title = title; 
		this.confUsersList = attendees; 
	}

	public String getTitle(){
		return title;
	}

	public ArrayList<User> getUsers(){
		return confUsersList;
	}

	public ArrayList<User> getCUserList() {
		confUsersList = createExampleUsers();
		return confUsersList;
	}

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
		int noOfusers = this.getCUserList().size();
		ArrayList<Point> pointList = getPoints(noOfusers, radius, center);
		for (int i = 0; i < pointList.size(); i++) {

			// confUserList.get(i).LOCATION = pointList.get(i);
			confUsersList.get(i).setLocation(pointList.get(i));
		}
		return confUsersList;
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

	public Conference_Dummy getCurrentConference() {
		Conference_Dummy dummy = new Conference_Dummy("Happening Now", confUsersList); 
		return dummy; 
	}



}
