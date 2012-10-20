package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.ContactsDbAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
//import android.graphics.PorterDuff.Mode;
import android.widget.Toast;

public class ContactsView extends Activity{
	private ContactsDbAdapter dbHelper;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		adjustLayoutLookAndFunctionality();
		dbHelper = new ContactsDbAdapter();
		dbHelper.open();
		retrieveAndDisplayContacts();
	}
	
	/** Adjust the displayed layout and button functionality */
	public void adjustLayoutLookAndFunctionality(){
		// Apply Delicious Font
		FontSetter.applySanSerifFont(ContactsView.this,
		findViewById(R.layout.contacts_layout));
		
		// Ensure the actionbar is present
		// ActionBar actionBar = getActionBar();
		// actionBar.show();
		
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
	            	// TODO Start the Dashboard Activity - Dashboard Activity has not been made yet however
	            	//Intent intent = new Intent(ContactsView.this, DashboardView.class);
	        		//startActivity(intent);
	            	
	            	// For now make a toast
	            	Context context = getApplicationContext();
	            	CharSequence text = "Go to Dashboard Page!";
	            	int duration = Toast.LENGTH_SHORT;
	            	Toast toast = Toast.makeText(context, text, duration);
	            	toast.show();
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
	// TODO Needs to be tested and altered
	public void retrieveAndDisplayContacts(){
		// Retrieve all contacts 
		Cursor cursor = dbHelper.getAllContacts();
		
		// The desired contact data to display
		String[] desiredContactData = new String[] { ContactsDbAdapter.KEY_NAME, ContactsDbAdapter.KEY_PICTURE };
		
		// The xml defined views which the data will be bound to
		int[] toXMLViews = new int[] {R.id.contact_name, R.id.contact_picture };
		
		// Create the adapter using the cursor pointing to the desired data as well as the layout information
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry_layout, cursor, desiredContactData, toXMLViews);
		
		// Set this adapter as your listview's adapter
		ListView listView = (ListView) findViewById(R.id.contacts_list); 
		listView.setAdapter(adapter);
	}
}
