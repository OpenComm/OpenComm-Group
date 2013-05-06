package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.ContactListView.state;

public class ContactListController {

	private static final String TAG = ContactListController.class.getSimpleName();
	private ContactListView view; 

	
	//Both ArrayLists of users
	private ArrayList<User> serverResults = new ArrayList<User>();
	private ArrayList<User> filteredResults = new ArrayList<User>();
	

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
		
	}
	

	public void handleBackButtonClicked() {
		Intent i = new Intent(this.view, DashboardView.class); 
		this.view.startActivity(i); 
	}

	public void updateContactList() {

	}

	public void handleOptionClick(View view) {
		// TODO add functions to handle overflow here
		String option = ((TextView) view.findViewById(R.id.contactlist_overflow_itemtext))
				.getText().toString().trim();
		if (option.equals("Sort by Online Status"))
		{
			Log.d(TAG, "Sort by Online Status");
		}
		else if(option.equals("Sort by First Name"))
		{
			Log.d(TAG, "Sort by First Name");
		}
		else if(option.equals("Sort by Last Name"))
		{
			Log.d(TAG, "Sort by Last Name");
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
//			synchronized(filteredResults) {
				filteredResults = results;
//			}
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
			}
			view.showUsers(results);
		}
	}
	
}

