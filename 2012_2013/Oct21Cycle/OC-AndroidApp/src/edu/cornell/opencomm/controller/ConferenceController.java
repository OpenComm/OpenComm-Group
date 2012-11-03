
package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceController {
	
	private ConferenceSchedulerView conferenceSchedulerView;
	@SuppressWarnings("unused")
	private Context context; 
	
	private ConferenceDataModel _conference; // the conference that is being controlled
	
	//TODO: add a listener for invitations and invitation responses
	
	//Constructor - initialize required fields
	public ConferenceController(ConferenceDataModel conf){
		this._conference = conf;
	}
	
	// u is the user that is dragged to create a new side chat
	// sChat is the indicator that it is the left side chat or the right sidechat 
	// it may be better to use an int or constant ot indicate left/right side chat
	public void addSideChat(User u, String sChat){
		//TODO: send invitation to user u (using sendInvitation method below)
		//TODO: the listener will handle creation of side chat by checking if it 
		// exists or not when it receives a response from user u
	}
	
	// sChat is the indicator that it is the left side chat or the right sidechat 
	// it may be better to use an int or constant ot indicate left/right side chat
	public void deleteSideChat(String sChat){
		//TODO: leave the side chat
		//TODO: empty the current user's view of the side chat (reset the model's side chat to empty)
	}
	
	public void inviteUser(User u){
		//TODO: send invitation to user
	}
	
	//TODO: the following methods will not be implemented for the Oct21 cycle
	
	public void moveUser(User u){
		//TODO: modify audio manipulation based on new location (future work)
	}
	
	public void kickoutUser(User u){
		//TODO: remove user from conference (not in current specs)
	}
	
	public void init(){
		//TODO Back End
		//1. Open a channel to listen to incoming messages / register this class with the connection
		
	}
	public void handleIncomingPackets(){
		//Handle
		//1. Invitation response
		//2. Priv transfer
	}
	public void sendPackets(){
		//Send
		//1.invitation
		//2.trans_priv
	}
}



