package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.ContactsView;

public class ContactListController {

	private static final String LOG_TAG = "Controller.ContactListController";
	private static final boolean D = true;

	private Roster roster;
	NetworkService xmppService;
	XMPPConnection xmppConn;

	/**
	 * The enum defining the opcodes for PopulateListTask
	 * 
	 */
	public enum ReturnState {
		FAILED, SUCCEEDED
	};

	private Activity view;


	public ContactListController (Activity v) {
		this.view = v;
		this.xmppService = NetworkService.getInstance();
		this.xmppConn = this.xmppService.getConnection();
		roster = this.xmppConn.getRoster();
	}

	public void handleBackButtonClicked(){
		Intent intent = new Intent(this.view, ContactsView.class);
		this.view.startActivity(intent);
	}


	public void handleBlockButtonClicked(){
		//TODO:
		//This should bring a drop down menu from which a user can select to either
		//go to contacts page or conferences page
	}

	public void contactClicked(View v){
		Intent i = new Intent(this.view, ContactCardView.class);
		TextView user = (TextView) v.findViewById(R.id.contact_name);
		i.putExtra("Contact", user.getText().toString());
		this.view.startActivity(i);
		//TODO: Needs to find the contact and open that contact page
	}

	public void contactSearchContactClicked(View v){
		Intent i = new Intent(this.view, ContactCardView.class);
		Button user = (Button) v.findViewById(R.id.contact_search_name);
		i.putExtra("Contact", user.getText().toString());
		this.view.startActivity(i);
		//TODO: Needs to find the contact and open that contact page
	}

	public ReturnState updateContacts() {
		roster = this.xmppConn.getRoster();
		AsyncTask<Void, Void, ReturnState> populate = new PopulateContactsTask()
		.execute();
		try {
			return (ReturnState) populate.get();
		} catch (InterruptedException e) {
			Log.v(LOG_TAG, e.getMessage());
		} catch (ExecutionException e) {
			Log.v(LOG_TAG, e.getMessage());
		}
		return ReturnState.FAILED;
	}

	private class PopulateContactsTask extends
	AsyncTask<Void, Void, ReturnState> {
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected ReturnState doInBackground(Void... arg0) {
			// Ankit :No need to clear, we can create an new list and replace
			// the old with it
			// by doing so, the list is still available to other threads
			// during the new list creation
			Iterator<RosterEntry> entries = roster.getEntries().iterator();
			ArrayList<User> updatedList = new ArrayList<User>();
			while (entries.hasNext()) {
				RosterEntry entry = entries.next();
				// FIXIT: pass in image or change to use VCard
				Log.v(LOG_TAG, "adding " + entry.getUser() + " to contact list");
				User buddy = new User(entry.getUser(), entry.getName(), 0);
				updatedList.add(buddy);

			}
			UserManager.updateContactList(updatedList);
			return ReturnState.SUCCEEDED;
		}

		@Override
		protected void onPostExecute(ReturnState state) {

		}

	}

	public void sortContacts() {
		ArrayList<User> users = UserManager.getContactList();
		User[] model = new User[0];
		User[] userArray = users.toArray(model);
		//cannot cast toArray() to User[], must call it with a parameter
		//of what type T[] the arrayList will be going to
		Arrays.sort(userArray);
		// implement comparable on user class
		ArrayList<User> sorted = new ArrayList<User>();
		for (int i = 0; i < userArray.length; i++) {
			sorted.add(userArray[i]);
		}
		UserManager.updateContactList(sorted);
	}

	/**
	 * ISSUES - Not sure if this algorithm is actually consistent
	 * Gave different results each time- weird O.o
	 * @param charQuery
	 *            - A sequence of characters representing a user's query for his
	 *            contacts
	 * @return An arrayList of users whose usernames begin with the original
	 *         query. Returns null if none found or there are no contacts
	 */
	public ArrayList<User> filterContacts(CharSequence charQuery) {
		try{
			ArrayList<User> users = UserManager.getContactList();
			String query = charQuery.toString();
			String queryStartsWith = query.substring(0, 1);
			if (D) {
				Log.v(LOG_TAG, "query results in string: " + query);
				Log.v(LOG_TAG, "retreived contact list of length " + users.size());
			}
			if (users == null || users.size() == 0) {
				return null;
			}
			ArrayList<User> filtered = new ArrayList<User>();
			int start = 0;
			int end = users.size();
			int prevMiddle = -1;
			int middle = end / 2;
			while (middle != prevMiddle) {
				if (users.get(middle).getUsername().substring(0,1).equalsIgnoreCase(queryStartsWith)) {
					// finds block of users that start with the first letter
					// finds the index of the first user in the block with that
					// first letter
					Log.v(LOG_TAG,
							"found user(s) with a username that starts with the first character of the query");
					int blockFirstIndex = middle;
					for (int i = middle; i > 0; i--) {
						if (users.get(i - 1).getUsername()
								.substring(0,1).equalsIgnoreCase(queryStartsWith)) {
							blockFirstIndex = i - 1;
						} else {
							break;
							// do not continue to iterate once the first letter does
							// not match
						}
					}

					// iterates through block to find users w/ names matching query
					for (int i = blockFirstIndex; i < users.size(); i++) {
						User userI = users.get(i);
						if (!userI.getUsername().substring(0,1).equalsIgnoreCase(queryStartsWith)) {
							// do not continue to iterate once the first letter does
							// not match
							break;
						}
						if (userI.getUsername().substring(0,query.length()).equalsIgnoreCase(query)) {
							filtered.add(users.get(i));
						}
					}
					if (filtered.size() == 0) {
						// makes sure that null is returned instead of empty
						// arraylist if no matches to query were found
						Log.v(LOG_TAG, "found no user(s) with a username that starts with the query");
						return null;
					}
					Log.v(LOG_TAG, "found user(s) with a username that starts with the query");
					return filtered;
				} else {
					prevMiddle = middle;
					if (users.get(middle).getUsername().substring(0, 1)
							.compareTo(queryStartsWith) < 0) {
						// compares the first letter of the username with the first
						// letter of the query, if the letter of the username is
						// before the letter of the query, we should look in the
						// upper half of the arraylist (we can set our firstIndex to
						// our value of middle)
						start = middle;
					} else {
						// safe to do else since == case is handled by very first
						// check in the greater loop
						end = middle;
					}
					middle = (start + end) / 2;
				}
			}
			// if middle = PrevMiddle, then we have reached a point where the letter
			// is not being found and there are no more divisions to make, nothing
			// matches query, so return null
			Log.v(LOG_TAG, "found no user(s) with a username that starts with the first character of the query");
			return null;
		}catch(StringIndexOutOfBoundsException e){
			//Do nothing
			//TODO- actually has to wait for the user and not throw a toast
			return null; 
		}
	}
}
