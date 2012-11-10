
package edu.cornell.opencomm.controller;

import java.util.HashMap;
import java.util.Set;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.content.Intent;
import edu.cornell.opencomm.model.ChatSpaceModel;
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
	private User currentUser=null;    //currentUser

	//Chat space model is also needed for transfer previliges and thus I have globalised these variables


	public void leaveChat(String sChat) throws XMPPException{

		HashMap<String, ChatSpaceModel> conferencemap = _conference.getHashmap();
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


		//TODO: leave the active chat/conference
		// if the user that is leaving is the moderator, then this should call transferPrivileges
	}

	public void endConference(String conf){
		//TODO: only the moderator can do this and only on the main conference
	}
	/**
	 * 
	 * @param u1  new moderator
	 * @param u2 current moderator leaving the chat
	 * @param room MultiUserChat in which the privilege has to be transferred 
	 * @throws XMPPException 
	 */
	public void transferPrivileges(User u1, User u2,  MultiUserChat room) throws XMPPException{
		//TODO: transfer privileges to u1
		room.grantModerator(u1.getNickname());
		room.revokeModerator(u2.getNickname());
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



