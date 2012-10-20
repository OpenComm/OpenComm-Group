package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.os.AsyncTask;

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
		
		
		@Override
		protected ReturnState doInBackground(String... strings) {
			Iterator<RosterEntry> entries = roster.getEntries().iterator();
			while (entries.hasNext())	
			{
				RosterEntry entry = entries.next();
				// FIXIT: pass in image or change to use VCard
				User buddy = new User(entry.getUser(), entry.getName(), 0);
				buddies.add(buddy);
				
			}
			return ReturnState.SUCEEDED;
		} 
		
		@Override
		protected void onPostExecute(ReturnState state) {
			
		}

	}
	
}
