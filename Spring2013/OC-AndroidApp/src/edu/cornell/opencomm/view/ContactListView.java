package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactAddSearchController;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ContactAddSearchAdapter;
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
	private boolean isAdd = false;
	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = ContactListView.class.getSimpleName();

	private ContactListController controller;
	private ContactListAdapter clAdapter;

	/** Overflow variables: list and its options */
	private ListView contactList; 
	private ListView overflowList;
	private String[] options;
	public static final String AddSearchKey = "ADDSEARCHKEY";
	private ContactAddSearchController searchController;

	/** Search suggestion variables: list */
	private AutoCompleteTextView suggestion;
	private ContactAddSearchAdapter casAdapter;

	/** data buffer for the contact list */
	private ArrayList<User> users;
	
	
	/** put the users data in to the buffers */
	private void setUsersBuffer(ArrayList<User> usersArray)
	{
		this.users = usersArray;
	}
	
	/** show the users in buffer on the contact list */
	private void showUsers()
	{
		if(clAdapter == null){
			clAdapter = new ContactListAdapter(this,
					R.layout.contactlist_item_layout, users);
			contactList = (ListView) findViewById(R.id.contacts_contactlist);
			contactList.setAdapter(clAdapter);
			contactList.setFooterDividersEnabled(true);
			contactList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					controller.handleContactClick(clAdapter.getItem(position));
				}
			});
		}
		else
		{
			clAdapter.notifyDataSetChanged();
		}
	}
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist_layout);
		this.controller = new ContactListController(this);
		this.initializeOverflow();
		this.initializeContactList();
		
		//Done - Change font
		FontSetter.applyRobotoFont(ContactListView.this,
				findViewById(R.id.contacts_layout));
		this.isAdd = this.getIntent().getBooleanExtra(
				ContactListView.AddSearchKey, false);
		this.suggestion = (AutoCompleteTextView) this
				.findViewById(R.id.contact_addsearch_search_input);
		if (this.isAdd) {
			((TextView) this.findViewById(R.id.contact_addsearch_title))
			.setText("add");
		}
		this.searchController = new ContactAddSearchController(this, this.isAdd);
	}

	/**
	 * Initializes the content of contact list. When an item is clicked, user
	 * feedback is generated and an appropriate action is launched
	 */
	private void initializeContactList() {
		
		//TODO: the current initial contacts are dummy users, change the way we get initial contacts
		ArrayList<User> initialContacts = createExampleUsers();
		
		System.out.println(initialContacts.size()); 
		setUsersBuffer(initialContacts);
		showUsers();
	}

	/**
	 * Initializes the content of overflow. When an item is clicked, user
	 * feedback is generated and an appropriate action is launched
	 */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(
				R.array.overflow_contacts);
		//OverflowAdapter oAdapter = new OverflowAdapter(this,
		//		R.layout.overflow_item_layout, this.options);
		if(D)
			Log.d(TAG, "  1:"+options[0]+"  2:"+options[1]+"  3:"+options[2]);
		OverflowAdapter oAdapter = new OverflowAdapter(this,
						R.layout.contactlist_overflow_entry_layout, this.options);
				
		overflowList = (ListView) this.findViewById(R.id.contacts_overflowList);
		overflowList.setAdapter(oAdapter);
		overflowList.setVisibility(View.INVISIBLE);

		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleOptionClick(view);
			}
		});
	}

	/** = this dashboard's overflow list */
	public ListView getOverflowList() {
		return this.overflowList;
	}

	/** Back button clicked: go back to Dashboard */
	public void back(View v) {
		this.controller.handleBackButtonClicked();
	}
	
	/** Back button clicked: search the user input*/
	public void search(View v) {
		String userInput = this.suggestion.getText().toString().trim();
		this.controller.handleSearchButtonClicked(userInput);
	}

	/** Overflow button clicked: flip visibility of overflow list */
	public void overflow(View v) {
		this.controller.handleOverflowButtonClicked();
	}

	/** Add button clicked: add contact to roster */
	public void add(View v) {
		//TODO: make it fade away
		contactList.setVisibility(View.INVISIBLE); 
		String userInput = this.suggestion.getText().toString().trim();
		if(D)
			Log.d(TAG, this.suggestion.getText().toString());
		setUsersBuffer(this.controller.handleAddButtonClicked(userInput));
	}

	public void searching(View v){
		this.initializeSuggestionList();
	}

	private void initializeSuggestionList() {
		ArrayList<String> data = this.controller.getSuggestions();
		this.casAdapter = new ContactAddSearchAdapter(this,
				R.layout.contact_addsearch_item_layout, data);
		this.suggestion = (AutoCompleteTextView) this
				.findViewById(R.id.contact_addsearch_search_input);
		this.suggestion.setDropDownAnchor(R.id.contact_addsearch_list);
		this.suggestion.setDropDownBackgroundResource(R.color.grey_eleven);
		this.suggestion.setAdapter(casAdapter);
		// Click event for single list row
		this.suggestion.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleContactClick(casAdapter.getItem(position));
			}
		});
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
	
	//TODO: DELETE THIS AFTER INTERGRATION
	//Create dummy users for test
	private ArrayList<User> createExampleUsers() {
		UserManager.userColorTable.put("oc1testorg", Color.YELLOW);
		UserManager.userColorTable.put("oc2testorg", Color.GREEN);
		UserManager.userColorTable.put("oc3testorg", Color.BLUE);
		UserManager.userColorTable.put("oc4testorg", Color.YELLOW);
		ArrayList<User> users = new ArrayList<User>();
		//users.add(UserManager.PRIMARY_USER);
		users.add(new User("oc1testorg", "Nora Ng-Quinn",
				R.drawable.example_picture_1));
		users.add(new User("oc2testorg", "Risa Naka",
				R.drawable.example_picture_2));
		users.add(new User("oc3testorg", "Kris Kooi",
				R.drawable.example_picture_3));
		users.add(new User("oc4testorg", "Ankit Singh",
				R.drawable.example_picture_4));
		return users;
	}
	
}



