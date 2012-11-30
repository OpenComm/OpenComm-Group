package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactAddSearchController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.ContactAddSearchAdapter;
import edu.cornell.opencomm.model.OverflowAdapter;
/**
 * View for contact add/search page.
 * When launching this activity, add to the intent the following boolean extra:<br>
 * key: ContactAddSearchView.AddSearchKey, value: true if AddView, false if SearchView<br>
 * Functionality (handled by ContactSearchController).<br>
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
public class ContactAddSearchView extends Activity {
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
	private static final String TAG = ContactAddSearchView.class.getSimpleName();
	
	/** Key for determining if this is an addview or searchview*/
	public static final String AddSearchKey = "ADDSEARCHKEY";
	
	/** boolean value retrieved from the intent that started this activity that defines whether 
	 * this activity is a search page or an add page*/
	private boolean isAdd = false;

	private ContactAddSearchController controller;  

	/** Overflow variables: list and options */
	private ListView overflowList;
	private String[] options;
	
	/** Search suggestion variables: list */
	private AutoCompleteTextView suggestion;
	private ContactAddSearchAdapter casAdapter;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_addsearch_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.contact_addsearch_layout));
		// determine if this is an add view or search view: if false, it's a search view
		this.isAdd = this.getIntent().getBooleanExtra(ContactAddSearchView.AddSearchKey, false);
		if (this.isAdd) {
			((TextView) this.findViewById(R.id.contact_addsearch_title)).setText("add");
		}
		this.controller = new ContactAddSearchController(this, this.isAdd);
		this.initializeOverflow();
		this.initializeSuggestionList();
	}
	
	/** Initialize the content of suggestion list. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeSuggestionList() {
		ArrayList<String> data = this.controller.getSuggestions();
		this.casAdapter = new ContactAddSearchAdapter(this, R.layout.contact_addsearch_item_layout, data);
		this.suggestion = (AutoCompleteTextView) this.findViewById(R.id.contact_addsearch_search_input);
		this.suggestion.setDropDownAnchor(R.id.contact_addsearch_list);
		this.suggestion.setDropDownBackgroundResource(R.color.grey_eleven);
		this.suggestion.setAdapter(casAdapter);
		// Click event for single list row
		this.suggestion.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleContactClick(casAdapter.getItem(position));
			}
		});
	}

	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(R.array.overflow_contacts);
		OverflowAdapter oAdapter = new OverflowAdapter(this, R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this.findViewById(R.id.contact_addsearch_overflowList);
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
