package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.ContactListController.ReturnState;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactsView extends Activity{
	
	private ContactListController controller;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		this.controller = new ContactListController(this);
		ReturnState updatecontact = this.controller.updateContacts();
		if (updatecontact == ReturnState.SUCCEEDED) {
			retrieveAndDisplayContacts();
			adjustLayoutLookAndFunctionality();
		}
		//dbHelper = new ContactsDbAdapter();
		//dbHelper.open();
		
	}
	
	/** Adjust the displayed layout and button functionality */
	public void adjustLayoutLookAndFunctionality(){
		// Apply Delicious Font
		FontSetter.applySanSerifFont(ContactsView.this,
		findViewById(R.id.contacts_layout));
	}
	
	public void overflow(View v) {
		this.controller.handleOverflowButtonClicked();
	}
	
	public void searchContact(View v) {
		this.controller.handleSearchButtonClicked();
	}
	
	public void addContact(View v) {
		this.controller.handleAddContactButtonClicked();
	}
	
	/**
	 * 
	 */
/*	public void initializeBackButton(){
		final ImageView backButton = (ImageView) findViewById(R.id.left_arrow_icon);
		backButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	Intent intent = new Intent(ContactsView.this, DashboardView.class);
	        		startActivity(intent);
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	} */
	
	/**
	 * 
	 */
/*	public void initializeSearchButton(){
		final ImageView searchButton = (ImageView) findViewById(R.id.search_button);
		searchButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	Intent intent = new Intent(ContactsView.this, ContactsSearchView.class);
	        		startActivity(intent);
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}*/
	/**
	 * The back arrow should takes the user to previous screen
	 * @param view
	 */
	public void onBackArrow(View view){
		super.onBackPressed();
	}
	
	/** Retrieve and display this user's contacts */
	public void retrieveAndDisplayContacts(){
		ArrayList<User> allContacts = UserManager.getContactList();
		User[] users = allContacts.toArray(new User[allContacts.size()]);
		 ContactsViewAdapter adapter = new ContactsViewAdapter(this, 
	                R.layout.contact_entry_layout, users);
	        
	        
		ListView contactList = (ListView)findViewById(R.id.contacts_list);
		contactList.setAdapter(adapter);
		contactList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				controller.contactClicked(arg1);
				
			}
			
		});
	}
}
