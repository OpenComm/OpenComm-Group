package edu.cornell.opencomm.controller;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ContactAddSearchView;
import edu.cornell.opencomm.view.ContactListView;

/**
 * Controller for contacts page (ContactListView). Functionality:<br>
 * When corresponding buttons are clicked in the action bar, different app
 * features are launched:
 * <ul>
 * <li>Back: returns to dashboard</li>
 * <li>Add: add a new user to contact</li>
 * <li>Search: search contacts</li>
 * <li>Overflow: go to conferences or account page</li>
 * </ul>
 * When a contact name is clicked, the contact card for the user is launched
 * 
 * Issues [TODO] - [frontend] Implement functionality for action bar and conf -
 * [backend] Generate full info of contacts info
 * 
 * @author Risa Naka [frontend]
 * */
public class ContactListController {
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
	private static final String TAG = ContactListController.class
			.getSimpleName();

	private ContactListView contactListView;

	/** Constructor for this ContactListController */
	public ContactListController(ContactListView view) {
		this.contactListView = view;
	}

	/**
	 * Search button clicked; launch ContactSearchView
	 * */
	public void handleSearchButtonClicked() {
//		Intent i = new Intent(this.contactListView, ContactAddSearchView.class);
//		i.putExtra(ContactAddSearchView.AddSearchKey, false);
//		this.contactListView.startActivity(i);

	}

	/** Add button clicked: launch ContactAddView */
	public void handleAddButtonClicked() {
//		Intent i = new Intent(this.contactListView, ContactAddSearchView.class);
//		i.putExtra(ContactAddSearchView.AddSearchKey, true);
//		this.contactListView.startActivity(i);
	}

	/** Contact clicked: launch corresponding ContactView using its username */
	public void handleContactClick(User user) {

	}

	/** Back button clicked: go back to Dashboard */
	public void handleBackButtonClicked() {
		this.contactListView.onBackPressed();
	}

	/** Update contact list; if it fails, show a toast to the user */
	public void updateContactList() {
		boolean updated = false;
		try {
			updated = new UserManager.UpdateContactsTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (!updated)
			System.out.println("not updated");
//			Toast.makeText(this.contactListView, "Contact List Update failed",
//					Toast.LENGTH_SHORT).show();
	}

}
