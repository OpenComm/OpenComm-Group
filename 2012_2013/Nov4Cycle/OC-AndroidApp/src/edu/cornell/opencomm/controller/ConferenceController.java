
package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

@SuppressWarnings("unused")
public class ConferenceController {
	
	private Context context; 
	
	private ConferenceDataModel _conference; // the conference that is being controlled
	
	//TODO: add a listener for invitations and invitation responses
	
	//Constructor - initialize required fields
	public ConferenceController(ConferenceDataModel conf){
		this._conference = conf;
	}
	
	public void inviteUser(User u, String sChat){
		//TODO: only moderators can do this.  Send invitation to user u for chat sChat
	}
	
	public void addUser(User u, String sChat){
		//TODO: update model when a user accepts an invitation
	}
	
	// sChat is the indicator that it is the left side chat or the right sidechat 
	// it may be better to use an int or constant ot indicate left/right side chat
	public void leaveChat(String sChat){
		//TODO: leave the active chat/conference
		// if the user that is leaving is the moderator, then this should call transferPrivileges
	}
	
	public void endConference(String conf){
		//TODO: only the moderator can do this and only on the main conference
	}
	
	public void transferPrivileges(User u){
		//TODO: transfer privileges to u
	}
	
	public void kickoutUser(User u){
		//TODO: only the moderator can do this.  Remove user from conference/chat
	}
	
	public void switchChat(String chat){
		//TODO: change active chat
	}
	
	
	//TODO: the following methods will not be implemented for the Nov4 cycle
	
	public void moveUser(User u){
		//TODO: modify audio manipulation based on new location (future work)
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



