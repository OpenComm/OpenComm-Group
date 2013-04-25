package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;

public class ContactListController {


	private ContactListView view; 

	public ContactListController(ContactListView view){
		this.view = view; 
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.view.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.view.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.view.getOverflowList()
			.setVisibility(View.INVISIBLE);
		}
	}

	public void handleContactClick(User item) {
		// TODO Auto-generated method stub
	}

	public void handleBackButtonClicked() {
		Intent i = new Intent(this.view, DashboardView.class); 
		this.view.startActivity(i); 
	}

	public void updateContactList() {
		// TODO Auto-generated method stub
		
	}

	public void handleOptionClick(View view2) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * When user click add button get all the contacts in the roster
	 */
	public ArrayList<User> handleAddButtonClicked() {
		// TODO Auto-generated method stub
		return null; 
	}


}
