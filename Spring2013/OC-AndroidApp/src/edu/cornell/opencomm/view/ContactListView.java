package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ContactListAdapter;
import edu.cornell.opencomm.model.OverflowAdapter;
import edu.cornell.opencomm.model.User;

/**
 * View for contact list page. Functionality (handled by ContactListController).<br>
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
public class ContactListView extends Activity {
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
	private static final String TAG = ContactListView.class.getSimpleName();

	private ContactListController controller;
	private ContactListAdapter clAdapter;

	/** Overflow variables: list and its options */
	private ListView overflowList;
	private String[] options;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.contactlist_layout);
		this.controller = new ContactListController(this);
		this.initializeContactList();
//		FontSetter.applySanSerifFont(ContactListView.this,
//				findViewById(R.id.contacts_layout));

	}

	/**
	 * Initializes the content of contact list. When an item is clicked, user
	 * feedback is generated and an appropriate action is launched
	 */
	private void initializeContactList() {
		ArrayList<User> allContacts = UserManager.getContactList();
		clAdapter = new ContactListAdapter(this,
				R.layout.contactlist_item_layout, allContacts);
//		ListView contactList = (ListView) findViewById(R.id.contacts_contactlist);
//		contactList.setAdapter(clAdapter);
//		contactList.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				controller.handleContactClick(clAdapter.getItem(position));
//			}
//
//		});
	}

	/** Back button clicked: go back to Dashboard */
	public void back(View v) {
		this.controller.handleBackButtonClicked();
	}

	/** Search button clicked: search for a contact */
	public void search(View v) {
		this.controller.handleSearchButtonClicked();
	}

	/** Add button clicked: add contact to roster */
	public void add(View v) {
		this.controller.handleAddButtonClicked();
	}

	/** Overriding back button: go back to Dashboard */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(this, DashboardView.class);
		this.startActivity(i);
		this.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	@Override
	/** Overriding onResume: update contact list */
	public void onResume() {
		super.onResume();
		this.controller.updateContactList();
		this.initializeContactList();
	}
}
