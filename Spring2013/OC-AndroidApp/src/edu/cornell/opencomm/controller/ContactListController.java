package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.ContactListView.state;

public class ContactListController {

	private static final String TAG = ContactListController.class.getSimpleName();
	private ContactListView view; 


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
		}
		else if(view.mode == state.add)
		{
			//add adding functions here
			Log.d(TAG,"Adding: " + userInput);
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
		}
		
		view.clAdapter.clear();
		view.clAdapter.notifyDataSetChanged();
	}

	public void handleContactClick(String item) {
		// TODO Auto-generated method stub
	}

	/** Obtain all of the possible contacts (email addresses and names) from the OpenComm users and 
	 * phone contacts with email addresses */
	private class GetContactSuggestionTask extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			ArrayList<String> allContacts = new ArrayList<String>();
			Iterator<User> iter = UserManager.getContactList().iterator();
			while (iter.hasNext()) {
				User u = iter.next();
				allContacts.add(u.getNickname());
			}
			UserSearchManager search = new UserSearchManager(NetworkService.getInstance().getConnection());
			try {
				Object[] ss = search.getSearchServices().toArray();
				for (Object s : ss) {
					Log.d(TAG, s.toString());
				}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			//Form searchForm = search.getSearchForm();
			//Form answerForm = searchForm.createAnswerForm();
			//answerForm.setAnswer("last", "DeMoro");
			//ReportedData data = search.getSearchResults(answerForm);
			// TODO [backend] add all emails and names from OpenComm users to the arraylist
			// TODO [backend] add all emails and names from the phonebook with emails
			return allContacts;
		}
	}

	/** Get all possible contacts (email addresses and names) from the OpenComm users and 
	 * phone contacts with email addresses */
	public ArrayList<String> getSuggestions() {
		try {
			return new GetContactSuggestionTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
}

