package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

import android.os.AsyncTask;
import android.util.Log;

public class ContactListController {

	private static final String TAG = "Controller.ContactListController";
	
	private static ContactListController _instance = null;

	private Roster roster;
	NetworkService xmppService;
	XMPPConnection xmppConn;

	/**
	 * The enum defining the opcodes for PopulateListTask
	 * 
	 */
	private enum ReturnState {
		FAILED, SUCEEDED
	};

	public static ContactListController getInstance() {
		if (_instance == null) {
			_instance = new ContactListController();
		}
		return _instance;
	}
	
	public ContactListController() {
		this.xmppService = NetworkService.getInstance();
		this.xmppConn = this.xmppService.getConnection();
		roster = this.xmppConn.getRoster();
	}

	public ReturnState updateContacts() {
		roster = this.xmppConn.getRoster();
		AsyncTask<Void, Void, ReturnState> populate = new PopulateContactsTask()
				.execute();
		try {
			return (ReturnState) populate.get();
		} catch (InterruptedException e) {
			Log.v(TAG, e.getMessage());
		} catch (ExecutionException e) {
			Log.v(TAG, e.getMessage());
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
			//Ankit :No need to clear, we can create an new list and replace the old with it
			//by doing so, the list is still available to other threads 
			//during the new list creation
			//TODO: Delete this line ->User.primaryUser.getContactList().clear();
			Iterator<RosterEntry> entries = roster.getEntries().iterator();
			ArrayList<User> updatedList = new ArrayList<User>();
			while (entries.hasNext()) {
				RosterEntry entry = entries.next();
				// FIXIT: pass in image or change to use VCard
				Log.v(TAG, "adding " + entry.getUser() + " to contact list");
				User buddy = new User(entry.getUser(), entry.getName(), 0);
				updatedList.add(buddy);

			}
			UserManager.updateContactList(updatedList);
			return ReturnState.SUCEEDED;
		}

		@Override
		protected void onPostExecute(ReturnState state) {

		}

	}

}
