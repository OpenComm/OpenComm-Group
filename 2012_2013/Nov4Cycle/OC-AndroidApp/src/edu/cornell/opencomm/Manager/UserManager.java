package edu.cornell.opencomm.Manager;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;

/**Manager class to hold state if the primary user 
 * 
 * @author Ankit Singh
 *
 */
public class UserManager {

	/**
	 * The primary user of the application
	 */
	public static User PRIMARY_USER ;
	
	/**
	 * The contact list for the primary user
	 */
	private static ArrayList<User> contactList = new ArrayList<User>();
	
	/**
	 * The default color for the user 
	 */
	public static int DEFAULT_USER_COLOR = R.color.blue;
	
	private static int[] colors = { R.color.blue, R.color.green, R.color.orange };
	
	
	private static Hashtable<String, Integer> userColorTable = 
			new Hashtable<String, Integer>();
	
	/**
	 * @param userJID
	 * @return
	 */
	public static int getUserColor(String userJID){
		int colorPointer = 0;
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
	/**
	 * @return
	 */
	public static ArrayList<User> getContactList() {
		synchronized (contactList) {
				return contactList;
		}
	}
	/**
	 * @param list
	 */
	public static void updateContactList(ArrayList<User> list){
		synchronized (contactList) {
			contactList = list;
		}
	}
}
