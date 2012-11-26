package edu.cornell.opencomm.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.ContactSearchView;
import edu.cornell.opencomm.view.MyAccountView;

/**
 * Controller for contact search page (ContactSearchView). Functionality:<br>
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
public class ContactSearchController {
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
	private static final String TAG = ContactSearchController.class.getSimpleName();
	
	private ContactSearchView contactSearchView;

	public ContactSearchController(ContactSearchView contactSearchView) {
		this.contactSearchView = contactSearchView;
	}

	/** Back button clicked: launches overriden back press which takes it back to ContactListView */
	public void handleBackButtonClicked() {
		this.contactSearchView.onBackPressed();
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.contactSearchView.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.contactSearchView.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.contactSearchView.getOverflowList()
					.setVisibility(View.INVISIBLE);
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
			Intent i = new Intent(this.contactSearchView,
					ConferenceSchedulerView.class);
			this.contactSearchView.startActivity(i);
		}
		// if the user selects account
		else if (option.equals("account")) {
			// launch my profile page
			Intent i = new Intent(this.contactSearchView, MyAccountView.class);
			this.contactSearchView.startActivity(i);
		}
	}
	
	/** Contact clicked: launch corresponding ContactView using its username */
	public void handleContactClick(User user) {
		Intent i = new Intent(this.contactSearchView, ContactCardView.class);
		i.putExtra(ContactCardView.contactCardKey, user.getUsername());
		this.contactSearchView.startActivity(i);

	}

}
