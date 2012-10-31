package edu.cornell.opencomm.Manager;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;

public class UserManager {

	public static User PRIMARY_USER ;
	
	private static ArrayList<User> contactList = null;
	
	public static int DEFAULT_USER_COLOR = R.color.blue;
	
	private static int[] colors = { R.color.blue, R.color.green, R.color.orange };
	
	private static int colorPointer = 0;
	
	private static Hashtable<String, Integer> userColorTable = 
			new Hashtable<String, Integer>();
	
	public static int getUserColor(String userJID){
		int userColor = DEFAULT_USER_COLOR;
		if (userColorTable.containsKey(userJID)) {
			userColor = userColorTable.get(userJID);
		} else {
			userColor = colors[colorPointer%colors.length];
			colorPointer = (colorPointer >= 9 ? 0 : ++colorPointer);
			userColorTable.put(userJID, userColor);
		}
		return  userColor;
	}
	public static ArrayList<User> getContactList(){
		synchronized (contactList) {
			return contactList;
		}
	}
	public static void updateContactList(ArrayList<User> list){
		synchronized (contactList) {
			contactList = list;
		}
	}
}
