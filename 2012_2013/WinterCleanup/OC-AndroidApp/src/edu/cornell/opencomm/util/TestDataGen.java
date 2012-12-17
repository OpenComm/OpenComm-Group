package edu.cornell.opencomm.util;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;

/**This class can be used as test data generator for front end development
 * 
 * @author Ankit Singh:as2536
 *
 */
public class TestDataGen {
	public static ArrayList<User> createExampleUsers(){
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
	
	
	public static ArrayList<Conference>  createExampleConferences(){
		ArrayList<User> users = createExampleUsers();
		Calendar currentTime = Calendar.getInstance();
		ArrayList<Conference> conferences = new ArrayList<Conference>();
		// Conference - Invited
		Calendar startTime = (Calendar)currentTime.clone();
		startTime.add(Calendar.HOUR, -1);
		Calendar endTime = (Calendar)currentTime.clone();
		endTime.add(Calendar.HOUR, 3); 
		conferences.add( new Conference( "Invited",
				"Man, we're gonna have suche a rockin' Christmas party. So cool. Did I mention it's at my place, Joseph Gordon-Levitt's?",
				startTime,
				endTime,
				"Every fuckinggg' year",
				users.get(0),
				users 
				) ); 
		// Conference - Accepted + Happening Now
		startTime = (Calendar)currentTime.clone();
		startTime.add(Calendar.HOUR, -1);
		endTime = (Calendar)currentTime.clone();
		endTime.add(Calendar.HOUR, 3); 
		ArrayList<User> conference2_attendees = new ArrayList<User>();
		conference2_attendees.add(users.get(1));
		conference2_attendees.add(users.get(2));
		conference2_attendees.add(users.get(3));
		Conference conference2 = new Conference("Happening Now",
				"OH HAAAAAAAAAAAAAAAAAAAAAAAAAI",
				startTime,
				endTime,
				"NOWWWW",
				users.get(1),
				conference2_attendees 
				);
		conference2.acceptInvite();
		conferences.add(conference2);
		// Conference - Accepted + Upcoming
		startTime = (Calendar)currentTime.clone();
		startTime.add(Calendar.HOUR, 3);
		endTime = (Calendar)currentTime.clone();
		endTime.add(Calendar.HOUR, 4); 
		ArrayList<User> conference3_attendees = new ArrayList<User>();
		conference2_attendees.add(users.get(2));
		conference2_attendees.add(users.get(4));
		Conference conference3 = new Conference("UPCOMING",
				"OMG it's Year 9000",
				startTime,
				endTime,
				"OMG IT'S OVER 9-",
				users.get(2),
				conference3_attendees 
				);
		conference3.acceptInvite();
		conferences.add(conference3);

		return conferences;
	}
	public static Conference getCurrentConference(){
		ArrayList<User> users  = createExampleUsers();
		
		Calendar currentTime = Calendar.getInstance();
		Calendar startTime = (Calendar)currentTime.clone();
		startTime.add(Calendar.HOUR, -1);
		Calendar endTime = (Calendar)currentTime.clone();
		endTime.add(Calendar.HOUR, 5); 
		Conference conf = new Conference("Demo Conference", "This is Demo", startTime, endTime, "Every Day", UserManager.PRIMARY_USER, users);
		
		
		return conf;
		
	}
	
}
