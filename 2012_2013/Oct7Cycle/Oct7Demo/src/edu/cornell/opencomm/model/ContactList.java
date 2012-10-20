package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.os.AsyncTask;

//import edu.cornell.opencomm.controller.LoginController.ReturnState;
import edu.cornell.opencomm.view.DashboardView;

public class ContactList {
	
	private Roster roster;
	private ArrayList<User> buddies;
	
	public ContactList(Connection connection) {
        roster = connection.getRoster();
        buddies = new ArrayList<User>();
    }
	
	private class LoginTask extends AsyncTask<String, Void, ReturnState> {
		
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		/*
		@Override
		protected ReturnState doInBackground(String... strings) {
			Iterator<RosterEntry> entries = roster.getEntries().iterator();
			while (entries.hasNext())	
			{
				RosterEntry entry = entries.next();
				// FIXIT: pass in image or change to use VCard
				User buddy = new User(entry.getUser(), entry.getName(), 0);
				
			}
		} */
		
		@Override
		protected void onPostExecute(ReturnState state) {
			
		}

		@Override
		protected ReturnState doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
}
