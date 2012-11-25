package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.MyAccountView;


public class MyAccountController {

	private MyAccountView myAccountView;
	
	public MyAccountController (MyAccountView v) {
		this.myAccountView = v;
	}
	
	/** 
	 * Launched when back button is clicked
	 * */
	public void handleBackButtonClicked() {
		this.myAccountView.onBackPressed();	
	}
	

	public void handleEditButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(myAccountView.getApplicationContext(), "Edit Button Pressed", duration);
		toast.show();	
	}
	
	/** 
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
    	if (this.myAccountView.getOverflowList().getVisibility() == View.INVISIBLE) {
    		this.myAccountView.getOverflowList().setVisibility(View.VISIBLE);
    	}
    	else {
    		this.myAccountView.getOverflowList().setVisibility(View.INVISIBLE);
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
			Intent i = new Intent(this.myAccountView, ConferenceSchedulerView.class);
			this.myAccountView.startActivity(i);
		}
		// if the user selects contact
		else if (option.equals("contacts")) {
			// launch contacts page
			Intent i = new Intent(this.myAccountView, ContactsView.class);
			this.myAccountView.startActivity(i);
		}		
	}
}