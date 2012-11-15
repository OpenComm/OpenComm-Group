package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.ContactSearchAdapter;
import edu.cornell.opencomm.model.User;

public class ContactsSearchView extends Activity {


	private ContactListController controller; 
	private ListView results; 
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * For debugging purposes
	 *
	 */
	private final boolean D = true; 
	private final String TAG= ContactsSearchView.class.getSimpleName(); 

	private EditText query; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_search_layout);
		this.query = (EditText)findViewById(R.id.search_contact_bar); 
		this.results = (ListView)findViewById(R.id.contact_search_list); 
		FontSetter.applySanSerifFont(this, findViewById(R.layout.contact_search_layout));
		FontSetter.applySanSerifFont(this, findViewById(R.layout.contact_entry_layout));
		controller = new ContactListController(this);
		textEntered();
	}

	//TODO
	//1.Method for getting the current text and searching database - cursor adapter

	//when a contact is clicked
	public void goToContact(View v){
		this.controller.contactSearchContactClicked(v);
	}

	public void backButtonPressed(View v){
		this.controller.handleBackButtonClicked();
	}

	public void overflow(View v){
		this.controller.handleOverflowButtonClicked();
	}

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
	}

}


