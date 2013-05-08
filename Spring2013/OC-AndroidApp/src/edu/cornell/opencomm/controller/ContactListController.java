package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactInfoView;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.ContactListView.state;
import edu.cornell.opencomm.view.DashboardView;

public class ContactListController {

	private static final String TAG = ContactListController.class.getSimpleName();
	private ContactListView view; 


	//Both ArrayLists of users
	private ArrayList<User> serverResults = new ArrayList<User>();
	private ArrayList<User> filteredResults = new ArrayList<User>();
	private ArrayList<User> displayedResults = new ArrayList<User>();


	public ContactListController(ContactListView view){
		this.view = view; 
	}

	//TODO: add the searching or adding functions here
	public void handleTextChanged()
	{
		String userInput = view.suggestion.getText().toString().trim();
		Log.d(TAG, userInput);

		if(view.mode == state.search)
		{
			//add searching functions here
			Log.d(TAG,"Searching: " + userInput);
			new FilterBuddies().execute(userInput);

		}
		else if(view.mode == state.add)
		{
			//add adding functions here
			Log.d(TAG,"Adding: " + userInput);
			if (userInput.length() == 3) {
				new SearchForUsers().execute(userInput);
			} else if (userInput.length() > 3) {
				new FilterUsers().execute(userInput);
			} else {
				view.showUsers(new ArrayList<User>());
			}
		}
	}


	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		Log.d(TAG, "overflow button clicked");
		if (this.view.getOverflowList().getVisibility() == View.INVISIBLE) {
			Log.d(TAG, "its invisible now, set it visible");
			this.view.getOverflowList().setVisibility(View.VISIBLE);
			this.view.getOverflowList().bringToFront();
		} else {
			Log.d(TAG, "its visible now, set it invisible");
			this.view.getOverflowList().setVisibility(View.INVISIBLE);
		}
	}

	public void handleContactClick(User item) {
		//TODO: how to pass argument to ContactInfoView?
		//Intent i = new Intent(this.view, ContactInfoView.class); 
		//this.view.startActivity(i);
		String s = item.getNickname();
		Log.v(TAG, s+"clicked");
		Intent i = new Intent(this.view, ContactInfoView.class);
		i.putExtra(ContactInfoView.contactCardKey, item.getUsername());
		this.view.startActivity(i);

	}


	public void handleBackButtonClicked() {
		Intent i = new Intent(this.view, DashboardView.class); 
		this.view.startActivity(i); 
	}

	public void updateContactList() {

	}
	
	public class FirstNameComparator implements Comparator<User> {
	    @Override
	    public int compare(User u1, User u2) {
	        return u1.getVCard().getFirstName().compareToIgnoreCase(u2.getVCard().getFirstName());
	    }
	}
	
	public class LastNameComparator implements Comparator<User> {
	    @Override
	    public int compare(User u1, User u2) {
	        return u1.getVCard().getLastName().compareToIgnoreCase(u2.getVCard().getLastName());
	    }
	}
	
	public class OnlineStatusComparator implements Comparator<User> {
	    @Override
	    public int compare(User u1, User u2) {
	        if (u1.getPresence().isAvailable() && u2.getPresence().isAvailable()) {
	        	return u1.getVCard().getLastName().compareToIgnoreCase(u2.getVCard().getLastName());
	        } else if (u1.getPresence().isAvailable() && !u2.getPresence().isAvailable()) {
	        	return 1;
	        } else if (!u1.getPresence().isAvailable() && u2.getPresence().isAvailable()) {
	        	return -1;
	        } else {
	        	return u1.getVCard().getLastName().compareToIgnoreCase(u2.getVCard().getLastName());
	        }
	    }
	}
	
	public ArrayList<User> orderByName(ArrayList<User> array) {
		
		return array;
	}

	public void handleOptionClick(View view) {
		// TODO add functions to handle overflow here
		String option = ((TextView) view.findViewById(R.id.contactlist_overflow_itemtext))
				.getText().toString().trim();
		if (option.equals("Sort by Online Status"))
		{
			Log.d(TAG, "Sort by Online Status");
			Collections.sort(displayedResults, new OnlineStatusComparator());
			this.view.showUsers(displayedResults);
			this.view.getOverflowList().setVisibility(View.INVISIBLE);
		}
		else if(option.equals("Sort by First Name"))
		{
			Log.d(TAG, "Sort by First Name");
			Collections.sort(displayedResults, new FirstNameComparator());
			this.view.showUsers(displayedResults);
			this.view.getOverflowList().setVisibility(View.INVISIBLE);
		}
		else if(option.equals("Sort by Last Name"))
		{
			Log.d(TAG, "Sort by Last Name");
			Collections.sort(displayedResults, new LastNameComparator());
			this.view.showUsers(displayedResults);
			this.view.getOverflowList().setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * When user click add button get all the contacts in the roster
	 */
	public void handleAddButtonClicked(String name) {
		//TODO: add the right functions here to get the user array

		if(view.mode == state.search)
		{
			//TODO: CHANGE THE BUTTON IMAGE HERE?
			view.mode = state.add;
			view.suggestion.setHint(R.string.add_hint);
			view.suggestion.setText("");
		}
		else if(view.mode == state.add)
		{
			//TODO: CHANGE THE BUTTON IMAGE HERE?
			view.mode = state.search;
			view.suggestion.setText("");
			view.suggestion.setHint(R.string.search_hint);	
			view.showUsers(NetworkService.getInstance().getBuddies());
		}

		view.clAdapter.clear();
		view.clAdapter.notifyDataSetChanged();
	}

	public void handleContactClick(String item) {
		// TODO Auto-generated method stub
	}




	private class FilterBuddies extends AsyncTask<String, Void, ArrayList<User>> {

		@Override
		protected ArrayList<User> doInBackground(String... params) {		
			ArrayList<User >results = new ArrayList<User>();
			for (User user : NetworkService.getInstance().getBuddies()) {
				if (user.getNickname().toLowerCase().contains(params[0].toLowerCase())) {
					results.add(user);
				}
			}
			return results;
		}

		@Override
		protected void onPostExecute(ArrayList<User> results) {
			filteredResults = results;
			displayedResults = results;
			view.showUsers(filteredResults);
		}
	}

	class FilterUsers extends AsyncTask<String, Void, ArrayList<User>> {

		@Override
		protected ArrayList<User> doInBackground(String... params) {		
			ArrayList<User >results = new ArrayList<User>();
			for (User user : serverResults) {
				if (user.getNickname().toLowerCase().contains(params[0].toLowerCase())) {
					results.add(user);
				}
			}
			return results;
		}

		@Override
		protected void onPostExecute(ArrayList<User> results) {
			synchronized(filteredResults) {
				filteredResults = results;
				displayedResults = results;
			}
			view.showUsers(filteredResults);
		}
	}


	private class SearchForUsers extends AsyncTask<String, Void, ArrayList<User>> {

		@Override
		protected ArrayList<User> doInBackground(String... params) {
			return SearchService.searchByName(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<User> results) {
			synchronized(serverResults) {
				serverResults = results;
				displayedResults = results;
			}
			view.showUsers(results);
		}
	}

}

