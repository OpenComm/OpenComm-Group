package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.util.Log;

import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.DashboardView;


public class ContactCardController {

	private static final String TAG = "Controller.ContactCardController";

	private User contact;

	private ContactCardView view;
	
	public ContactCardController (ContactCardView v){
		this.view = v;
	}
	
	public void handleBackButtonClicked(){
		Intent intent = new Intent(this.view, ContactsView.class); 
		this.view.startActivity(intent);
	}
	public void handleAddButtonClicked(){
		Log.v(TAG, "Adding user " + contact.getUsername() + " to contacts");
		ArrayList<User> contactList = UserManager.getContactList();
		contactList.add(contact);
		UserManager.updateContactList(contactList);
	}
	public void handleOverflowButtonClicked(){
		
	}
	public void handleBlockButtonClicked(){
		
	}
}
