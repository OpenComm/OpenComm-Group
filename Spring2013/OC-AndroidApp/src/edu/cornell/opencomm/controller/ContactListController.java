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
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;

public class ContactListController {

	private static final String TAG = ContactListController.class.getSimpleName();
	private ContactListView view; 


	public ContactListController(ContactListView view){
		this.view = view; 
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.view.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.view.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.view.getOverflowList()
			.setVisibility(View.INVISIBLE);
		}
	}

	public void handleContactClick(User item) {
		// TODO Auto-generated method stub
	}

	public void handleBackButtonClicked() {
		Intent i = new Intent(this.view, DashboardView.class); 
		this.view.startActivity(i); 
	}

	public void updateContactList() {
		// TODO Auto-generated method stub

	}

	public void handleOptionClick(View view2) {
		// TODO Auto-generated method stub

	}

	/*
	 * When user click add button get all the contacts in the roster
	 */
	public ArrayList<User> handleAddButtonClicked(String name) {
		return SearchService.searchByName(name); 
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

