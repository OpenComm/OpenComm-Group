package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ContactListAdapter;
import edu.cornell.opencomm.model.User;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


/**
 * View/Controller for conference creation page. Functionality:<br>
 * When corresponding buttons are clicked in the action bar, different app
 * features are launched:
 * <ul>
 * <li>Back: returns to conference list page</li>
 * </ul>
 * When corresponding buttons are clicked in the conference configuration, 
 * different features are activated:
 * <ul>
 * <li>name: name the conference</li>
 * <li>date: launch default date selector</li>
 * <li>start/end/recurring: launchg default time selector</li>
 * <li>add: add an attendee</li>
 * </ul>
 * When a contact name is clicked, the contact card for the user is launched
 * 
 * Issues [TODO] - [frontend] Implement functionality for all buttons
 * 
 * @author Risa Naka [frontend]
 * */
public class ConferenceCreationController extends Activity {
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
	private static final String TAG = ConferenceCreationController.class.getSimpleName();

	private ContactListAdapter clAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_creation_layout);
		this.initializeContactList();
		FontSetter.applySanSerifFont(ConferenceCreationController.this,
				findViewById(R.id.conference_creation_layout));

	}

	/**
	 * Initializes the content of contact list. When an item is clicked, user
	 * feedback is generated and an appropriate action is launched
	 */
	private void initializeContactList() {
		ArrayList<User> allContacts = UserManager.getContactList();
		clAdapter = new ContactListAdapter(this,
				R.layout.contactlist_item_layout, allContacts);
		ListView contactList = (ListView) findViewById(R.id.conference_creation_attendeelist);
		if (D) Log.d(this.TAG, "contact list is null" + (contactList == null));
		if (D) Log.d(this.TAG, "contact list adapter is null" + (clAdapter == null));
		contactList.setAdapter(clAdapter);
		contactList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO add contact to list
			}

		});
	}
	
	/** Back button clicked: go back to the ConferenceList page*/
	public void back(View v) {
		this.onBackPressed();
	}
	
	/** Set date clicked: launch date selector */
	public void setDate(View v) {
		// TODO [frontend] launch date selector
		Toast.makeText(this, "date clicked", Toast.LENGTH_SHORT).show();
	}
	/** Set start time clicked: launch time selector */
	public void setTimeStart(View v) {
		// TODO [frontend] launch time selector
		Toast.makeText(this, "start time clicked", Toast.LENGTH_SHORT).show();
	}
	/** Set end time clicked: launch time selector */
	public void setTimeEnd(View v) {
		// TODO [frontend] launch time selector
		Toast.makeText(this, "end time clicked", Toast.LENGTH_SHORT).show();
	}
	/** Set recurring time clicked: launch time selector */
	public void setTimeRecurring(View v) {
		// TODO [frontend] launch time selector
		Toast.makeText(this, "recurring time clicked", Toast.LENGTH_SHORT).show();
	}
	
	/** add attendees clicked: launch search contacts page */
	public void addAttendees(View v) {
		// TODO [fronend] launch search contacts page
		Toast.makeText(this, "add Attendees clicked", Toast.LENGTH_SHORT).show();
	}
	
	/** create conference clicked: validate all inputs, and if valid, create conference in database */
	public void create(View v) {
		// TODO [frontend] validate inputs
		// TODO [backend] send creation of conference to databaes
		Toast.makeText(this, "create clicked", Toast.LENGTH_SHORT).show();
	}

}
