package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import edu.cornell.opencomm.view.ContactsSearchView;
import edu.cornell.opencomm.view.ContactsView;

public class ContactsSearchController {

	private ContactsSearchView view;
	
	public ContactsSearchController (ContactsSearchView v){
		this.view = v;
	}
	
	public void handleBackButtonClicked(){
		Intent intent = new Intent(this.view, ContactsView.class);
		this.view.startActivity(intent);
	}
	
	
	public void handleBlockButtonClicked(){
		//TODO:
		//This should bring a drop down menu from which a user can select to either
		//go to contacts page or conferences page
	}
	
	public void contactClicked(View v){
		//TODO: Needs to find the contact and open that contact page
	}
}
