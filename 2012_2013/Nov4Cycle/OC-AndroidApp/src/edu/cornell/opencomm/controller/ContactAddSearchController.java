package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.ContactAddSearchView;
import edu.cornell.opencomm.view.MyAccountView;

/**
 * Controller for contact add/search page (ContactAddSearchView). Functionality:<br>
 * When corresponding buttons are clicked in the action bar, different app features are launched:
 * <ul>
 * <li>Back: returns to contact list view</li>
 * <li>Overflow: go to conferences or account page</li>
 * </ul>
 * When a contact name is clicked, the contact card for the user is launched
 * 
 * Issues [TODO] 
 * - [frontend] Implement functionality for action bar and conf
 * - [backend] Generate full info of contacts
 * info
 * 
 * @author Risa Naka [frontend]
 * */
public class ContactAddSearchController {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = ContactAddSearchController.class.getSimpleName();
	
	private ContactAddSearchView contactAddSearchView;

	private boolean isAdd = false;
	
	public ContactAddSearchController(
			ContactAddSearchView view, boolean isAdd) {
		this.contactAddSearchView = view;
		this.isAdd = isAdd;
	}
	/** Back button clicked: launches overriden back press which takes it back to ContactListView */
	public void handleBackButtonClicked() {
		this.contactAddSearchView.onBackPressed();
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.contactAddSearchView.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.contactAddSearchView.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.contactAddSearchView.getOverflowList().setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Overflow option clicked:
	 * <ul>
	 * <li>conferences: launches conferences page</li>
	 * <li>account: launches profile page</li>
	 * </ul>
	 * */
	public void handleOptionClick(View view) {
		String option = ((TextView) view.findViewById(R.id.overflow_itemtext))
				.getText().toString().trim();
		// if the user selects conferences
		if (option.equals("conferences")) {
			// launch conferences page
			Intent i = new Intent(this.contactAddSearchView, ConferenceSchedulerView.class);
			this.contactAddSearchView.startActivity(i);
		}
		// if the user selects account
		else if (option.equals("account")) {
			// launch my profile page
			Intent i = new Intent(this.contactAddSearchView, MyAccountView.class);
			this.contactAddSearchView.startActivity(i);
		}
	}
	
	/** Contact clicked: launch corresponding ContactView using its username */
	public void handleContactClick(User user) {
		if (isAdd) {
			// TODO launch contact add page
		}
		else {
			Intent i = new Intent(this.contactAddSearchView, ContactCardView.class);
			i.putExtra(ContactCardView.contactCardKey, user.getUsername());
			this.contactAddSearchView.startActivity(i);
		}

	}
	
	
	/** Obtain all of the possible contacts (email addresses and names) from the OpenComm users and 
	 * phone contacts with email addresses */
	private class GetContactSuggestionTask extends AsyncTask<Void, Void, ArrayList<User>> {

		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			ArrayList<User> allContacts = new ArrayList<User>();			
			// TODO [backend] add all emails and names from OpenComm users to the arraylist
			// TODO [backend] add all emails and names from the phonebook with emails
			return allContacts;
		}
	}
	
	/** Get all possible contacts (email addresses and names) from the OpenComm users and 
	 * phone contacts with email addresses */
	public ArrayList<User> getSuggestions() {
		try {
			return new GetContactSuggestionTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ArrayList<User>();
	}

}
