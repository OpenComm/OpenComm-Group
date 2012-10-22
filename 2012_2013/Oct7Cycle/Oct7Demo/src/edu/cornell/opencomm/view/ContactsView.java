package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;

public class ContactsView extends Activity{
	//private ContactsDbAdapter dbHelper;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		retrieveAndDisplayContacts();
		adjustLayoutLookAndFunctionality();
		//dbHelper = new ContactsDbAdapter();
		//dbHelper.open();
		
	}
	
	/** Adjust the displayed layout and button functionality */
	public void adjustLayoutLookAndFunctionality(){
		// Apply Delicious Font
		FontSetter.applySanSerifFont(ContactsView.this,
		findViewById(R.layout.contacts_layout));
		
		// Initialize images to act as buttons
		initializeBackButton();
		initializeSearchButton();
	}
	
	public void initializeBackButton(){
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
	} 
	
	public void initializeSearchButton(){
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
	}
	/**
	 * The back arrow should takes the user to previous screen
	 * @param view
	 */
	public void onBackArrow(View  view){
		super.onBackPressed();
	}
	
	/** Retrieve and display this user's contacts */
	public void retrieveAndDisplayContacts(){
		ArrayList<User> all_contacts = User.primaryUser.getContactList();
		User[] users = all_contacts.toArray(new User[all_contacts.size()]);
		 ContactsViewAdapter adapter = new ContactsViewAdapter(this, 
	                R.layout.contact_entry_layout, users);
	        
	        
		ListView contactList = (ListView)findViewById(R.id.contacts_list);
		contactList.setAdapter(adapter);
	}
	
	public User[] getContacts(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("risa", "Risa Naka", R.drawable.example_picture_1));
		users.add(new User("nora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		users.add(new User("makoto", "Makoto Bentz", R.drawable.example_picture_3));
		users.add(new User("graeme", "Graeme Bailey", R.drawable.example_picture_4));
		return users.toArray(new User[users.size()]);
	}

}
