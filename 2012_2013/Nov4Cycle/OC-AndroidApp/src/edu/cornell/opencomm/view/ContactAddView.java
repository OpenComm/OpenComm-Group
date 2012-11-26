package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactAddSearchController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.OverflowAdapter;

/**
 * View for contact add page. Functionality (handled by ContactAddController).<br>
 * When corresponding buttons are clicked in the action bar, different app features are launched:
 * <ul>
 * <li>Back: returns to contact list view</li>
 * <li>Overflow: go to conferences or account page</li>
 * </ul>
 * When a contact name is clicked, the contact card for the user is added to the contact list
 * 
 * Issues [TODO] 
 * - [frontend] Implement functionality for action bar and conf
 * - [backend] Generate full info of contacts
 * info
 * 
 * @author Risa Naka [frontend]
 * */
public class ContactAddView extends Activity {
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
	private static final String TAG = ContactAddView.class.getSimpleName();

	private ContactAddSearchController controller;  

	/** Overflow variables: list and options */
	private ListView overflowList;
	private String[] options;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_search_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.contact_search_layout));
		((TextView) findViewById(R.id.contact_search_title)).setText("add");
		this.controller = new ContactAddSearchController(this);
		this.initializeOverflow();
	}
	
	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(R.array.overflow_contacts);
		OverflowAdapter oAdapter = new OverflowAdapter(this, R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this.findViewById(R.id.contact_search_overflowList);
		overflowList.setAdapter(oAdapter);
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
	
	/** Overflow button clicked: flips visibility of overflow list */
	public void overflow(View v) {
		this.controller.handleOverflowButtonClicked();
	}
	
	/** Back button clicked: goes back to ContactListView */
	public void back(View v) {
		this.controller.handleBackButtonClicked();
	}
	
	@Override
	/** Overrides back pressed: go back to ContactListView*/
	public void onBackPressed() {
		Intent i = new Intent(this, ContactListView.class);
		this.startActivity(i);
	}
/*
	//TODO
	//1.Method for getting the current text and searching database - cursor adapter

	//TODO/ISSUES -
	//1. The cursor goes back to start 
	//2. This only happens when the editText box is clicked - must make it happen when its not clicked as well
	//3. Basically when any changes have been made to the editText box. 

	//As the user enters text- this method gets the text and sends
	//queries to the database to retrieve contacts
	public void textEntered(){
		this.query.setText("");
		if (D){Log.v(TAG,query.getText().toString());}
		query.addTextChangedListener(new TextWatcher(){
			//these methods can cause StackOverflow Error- must be very careful!
			public void afterTextChanged(Editable arg0) {
				if (D){Log.v(TAG, "afterTextChanged"+ arg0.toString());}
				String changedText = arg0.toString(); 
				query.removeTextChangedListener(this);
				query.setText(changedText);			
				searchQuery(changedText);
				query.addTextChangedListener(this);
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

		}); 
	}

	public void searchQuery(CharSequence query){
		this.controller.updateContacts(); 
		if (query != null || query != ""){
			ArrayList<User> contacts = this.controller.filterContacts(query);
			if (contacts != null){
				ContactSearchAdapter adapter = new ContactSearchAdapter(this, R.layout.contact_search_entry_layout, contacts);
				results.setAdapter(adapter);
				results.setVisibility(View.VISIBLE);
				results.setOnItemClickListener(new OnItemClickListener(){

					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						controller.contactSearchContactClicked(arg1);			
					}			
				});
			}
			//no contacts that match the query 
			//TODO - ask the design team what to do in this case?
			//For now throwing a toast
			else{
				results.setVisibility(View.INVISIBLE);
				CharSequence text = "No contacts found matching the query";
				int duration = Toast.LENGTH_SHORT;
				Toast send = Toast.makeText(getApplicationContext(),text,duration);
				send.show();
			}
		}
	}*/

}


