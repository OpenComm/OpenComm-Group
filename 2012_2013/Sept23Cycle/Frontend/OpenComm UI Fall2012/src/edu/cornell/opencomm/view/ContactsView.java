package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ContactsDbAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactsView extends Activity{
	private ContactsDbAdapter dbHelper;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		dbHelper = new ContactsDbAdapter();
		dbHelper.open();
		retrieveAndDsiplayContacts();
	}
	
	/** Retrieve and display this user's contacts */
	public void retrieveAndDsiplayContacts(){
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
