package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

import edu.cornell.opencomm.view.ContactCardView;


public class ContactCardController {

	private static final String TAG = "Controller.ContactCardController";

	private Roster roster;
	NetworkService xmppService;
	XMPPConnection xmppConn;

	private ContactCardView view;
	
	public ContactCardController (ContactCardView v){
		this.view = v;
	}
	
	public void handleBackButtonClicked(){
		
	}
	public void handleAddButtonClicked(){
		this.xmppService = NetworkService.getInstance();
		this.xmppConn = this.xmppService.getConnection();
		roster = this.xmppConn.getRoster();
		Log.v(TAG, "adding " + user + " to contact list");
	}
	public void handleOverflowButtonClicked(){
		
	}
	public void handleBlockButtonClicked(){
		
	}
}
