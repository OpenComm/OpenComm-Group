package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.MyProfileView;


/**
 * Controller for profile page (MyProfileView). Functionality:<br>
 * When corresponding buttons are clicked in the action bar, different app features are launched:
 * <ul>
 * <li>Back: returns to dashboard</li>
 * <li>Edit: launches account edit page</li>
 * <li>Overflow: go to conferences or contacts</li>
 * </ul>
 * 
 * Issues [TODO] 
 * - [frontend] Implement functionality for action bar and conf
 * - [backend] Generate full info of primary user
 * info
 * 
 * @author Risa Naka [frontend]
 * */
public class MyProfileController {

	private MyProfileView myProfileView;
	
	public MyProfileController (MyProfileView v) {
		this.myProfileView = v;
	}
	
	/** 
	 * Launched when back button is clicked
	 * */
	public void handleBackButtonClicked() {
		this.myProfileView.onBackPressed();	
	}
	

	public void handleEditButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(myProfileView.getApplicationContext(), "Edit Button Pressed", duration);
		toast.show();	
	}
	
	/** 
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
    	if (this.myProfileView.getOverflowList().getVisibility() == View.INVISIBLE) {
    		this.myProfileView.getOverflowList().setVisibility(View.VISIBLE);
    	}
    	else {
    		this.myProfileView.getOverflowList().setVisibility(View.INVISIBLE);
    	}		
	}

	/** 
	 * Overflow option clicked:
	 * <ul>
	 * <li>conferences: launches conferences page</li>
	 * <li>contacts: launches contacts page</li>
	 * </ul>
	 * */
	public void handleOptionClick(View view) {
		String option = ((TextView) view.findViewById(R.id.overflow_itemtext))
				.getText().toString().trim();
		// if the user selects conferences
		if (option.equals("conferences")) {
			// launch conferences page
			Intent i = new Intent(this.myProfileView, ConferenceSchedulerView.class);
			this.myProfileView.startActivity(i);
		}
		// if the user selects contact
		else if (option.equals("contacts")) {
			// launch contacts page
			Intent i = new Intent(this.myProfileView, ContactListView.class);
			this.myProfileView.startActivity(i);
		}		
	}
}