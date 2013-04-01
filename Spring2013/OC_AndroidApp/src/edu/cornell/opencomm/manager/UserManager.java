package edu.cornell.opencomm.manager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.RosterEntry;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

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
	
	
	public static Hashtable<String, Integer> userColorTable = 
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
	
	/** Updates the contact list, returning true if the update is successful */
	public static class UpdateContactsTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			Iterator<RosterEntry> entries = NetworkService.getInstance()
					.getConnection().getRoster().getEntries().iterator();
			ArrayList<User> updatedList = new ArrayList<User>();
			while (entries.hasNext()) {
				RosterEntry entry = entries.next();
				// TODO [backend] obtain complete info on user
				User buddy = new User(entry.getUser(), entry.getName(), 0);
				updatedList.add(buddy);
			}
			UserManager.updateContactList(updatedList);
			return true;
		}
	}
}
