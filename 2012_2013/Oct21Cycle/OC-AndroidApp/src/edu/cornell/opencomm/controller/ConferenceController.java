
package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceCardView;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceController {
	
	private ConferenceSchedulerView conferenceSchedulerView;
	private Context context; 
	
	//TODO: add a listener for invitations and invitation responses
	
	//Constructor - initialize required fields
	public ConferenceController(Context context, ConferenceSchedulerView view){
		this.conferenceSchedulerView = view;
		this.context = context; 
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
	
	public void addConferencePressed(){
		//		Intent intent = new Intent(this.conferenceView, CreateConferenceView.class);
	}
	
	public void notificationsPressed(){
		//Intent intent = new Intent(this.conferenceView, NotficiationsView.class);
	}
	
	public void blockButtonPressed(){
		//Intent intent = new Intent(this.conferenceView, DashboardView.class);
	}
	
	public void backButtonPressed(){
		Intent intent = new Intent(this.conferenceSchedulerView, DashboardView.class); 
		this.conferenceSchedulerView.startActivity(intent);
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



