package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.FontSetter;

public class ContactsSearchView extends Activity {
	
	
	private ContactListController controller; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_search_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.layout.contact_search_layout));
		FontSetter.applySanSerifFont(this, findViewById(R.layout.contact_entry_layout));
		controller = new ContactListController(this);
	}
	
	//TODO
	//1.Method for getting the current text and searching database - cursor adapter
	
	//when a contact is clicked
	public void goToContact(View v){
		this.controller.contactClicked(v);
	}
	
	public void backButtonPressed(View v){
		this.controller.handleBackButtonClicked();
	}
	
	public void blockButtonPressed(View v){
		this.controller.handleBlockButtonClicked();
	}
}
