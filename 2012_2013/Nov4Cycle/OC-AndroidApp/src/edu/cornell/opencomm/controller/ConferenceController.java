
package edu.cornell.opencomm.controller;

import java.util.HashMap;

import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.cornell.opencomm.model.ChatSpaceModel;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceCardView;
import edu.cornell.opencomm.view.ConferenceView;

@SuppressWarnings("unused")
public class ConferenceController {

	private ConferenceView view; 

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
	public void leaveChat(String sChat, User currentUser) throws XMPPException{

		HashMap<String, ChatSpaceModel> conferencemap = _conference.getIDMap();
		ChatSpaceModel spacechat= conferencemap.get(sChat);
		User moderator=spacechat.getModerator();

		if (moderator.getUsername().equals(currentUser.getUsername())){    //if moderator
			User newmoderator=null;                                 //ask for new moderator if current moderator is leaving
			User new_sidemoderator=null;                            //find the new_sidemoderator for this side chat
			if (_conference.getIsmain()){                           //if in mainchat
				String[] keys= (String[]) conferencemap.keySet().toArray();
				for (int i=0;i<=2; i++){
					if (keys[i]!=sChat){	                            //if not mainchat i.e. sidechats, transfer privileges and then leave
						ChatSpaceModel spacechat1= conferencemap.get(keys[i]);
						transferPrivileges( currentUser, newmoderator, spacechat1);
						spacechat1.leave();
						_conference.setIsmain(true);                        //not sure if this is needed, please check
					}
					transferPrivileges( newmoderator, currentUser,  spacechat);  //finally leave the mainchat
					spacechat.leave();
				}
			}
			else{                                                          //if this is a side chat than only for that
				transferPrivileges(newmoderator, currentUser,  spacechat);
				spacechat.leave();	
			}
		}

		else{                                            //if only a user
			if ((_conference.getIsmain())){            //if main then leave side chat also
				String[] keys= (String[]) conferencemap.keySet().toArray();
				for (int i=0;i<=2; i++){
					if (keys[i]!=sChat){	                            //if not mainchat i.e. sidechats leave
						ChatSpaceModel spacechat1= conferencemap.get(keys[i]);
						spacechat1.leave();
					}	
					spacechat.leave();                                  //leave main
				}
			}
			else {
				spacechat.leave();                                    //leave the side chat
			}
		}
	}

	public void endConference(String conf, User u){
		ChatSpaceModel chat = this._conference.getIDMap().get(conf);
		if(chat.getModerator().equals(u)){
			HashMap<String, ChatSpaceModel> allChats = this._conference.getIDMap();
			for(String s : allChats.keySet()){
				try {
					allChats.get(s).destroy(null, null);
				} catch (XMPPException e) {
					Log.e("ConferenceController", "XMPPError when destroying a MUC");
				}
			}
		}
	}

	/**
	 * 
	 * @param u1  new moderator
	 * @param u2 current moderator leaving the chat
	 * @param room ChatSpaceModel in which the privilege has to be transferred 
	 * @throws XMPPException 
	 */
	public void transferPrivileges(User u1, User u2,  ChatSpaceModel room) throws XMPPException{
		room.grantModerator(u1.getNickname());
		room.revokeModerator(u2.getNickname());
	}

	public void kickoutUser(String chat, User userToBeKicked, User currUser){
		if(this._conference.getIDMap().get(chat).getModerator().equals(currUser)){
			this._conference.getIDMap().get(chat).getAllNicknames().remove(userToBeKicked.getNickname());
			this._conference.getIDMap().get(chat).getAllParticipants().remove(userToBeKicked.getUsername());
			try {
				this._conference.getIDMap().get(chat).kickParticipant(userToBeKicked.getNickname(), null);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	public ChatSpaceModel switchChat(String chat){
		ChatSpaceModel newChat = this._conference.getIDMap().get(chat);
		this._conference.setActiveChat(chat);
		if(this._conference.getLocationMap().get(chat).equals("MAIN")){
			this._conference.setIsmain(true);
		}
		else{
			this._conference.setIsmain(false);
		}
		return newChat;
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

	//TODO- need to store the current conference as a conference object 
	public void handleBackButtonClicked(){
		Intent click = new Intent(this.view, ConferenceCardView.class);
		//click.putExtra("com.cornell.opencomm.model.Conference", conference);
		this.view.startActivity(click); 
	}

	public void handleOverflow(){

	}

	public void pingClicked(){

	}
	
	public void addPersonClicked(){
		//TODO- Should go to add contact to conference page
	}
	
	public void muteClicked(){
		
	}
	
	public void leaveConference(){
		
	}
	
	public void removeUser(){
		
	}
	
	public void setNewModerator() {
		// TODO Auto-generated method stub
		
	}

	public void showProfile() {
		// TODO Auto-generated method stub
		
	}
	
	
}



