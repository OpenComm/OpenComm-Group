package edu.cornell.opencomm.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceCardView;
import edu.cornell.opencomm.view.ConferenceView;

@SuppressWarnings("unused")
public class ConferenceController {

	private ConferenceView view;

	private Context context;

	private ConferenceDataModel conferenceModel; // the conference that is being
												// controlled

	private static final String TAG = "ConferenceController";
	private static final boolean D = true;

	private HashMap<User, String> invitedUsersToConference;

	// users who have been invited who have not been added to the model
	// information

	// TODO: add a listener for invitations and invitation responses
	
	public ConferenceController(ConferenceView view,ConferenceDataModel model){
		//TODO: do we really need this 
		this.context = view; 
		this.view = view; 
		this.conferenceModel = model;
	}

	// Constructor - initialize required fields
	public ConferenceController(ConferenceDataModel conf) {
		this.conferenceModel = conf;
		invitedUsersToConference = new HashMap<User, String>();
	}
	
	public void init(boolean isMod) {
//		if(isMod){
//			this._conference.initialize(true);
//		}
//		else{
//			this._conference.initialize(false);
//		}
	}

	public void inviteUser(User u, String sChat) {
		User primaryUser = UserManager.PRIMARY_USER;
		ConferenceRoom chatRoom = findChat(sChat);
		// check if user is moderator in the chat sChat
		User moderator = chatRoom.getModerator();
		if (!primaryUser.equals(moderator)) {
			if (D)
				Log.v(TAG, "User " + primaryUser.getUsername()
						+ " is not the moderator of chat " + sChat
						+ ".  Invitation not sent");
			return;
		}
		if (D)
			Log.v(TAG, "Confirmed that the user " + primaryUser.getUsername()
					+ "is the moderator");
		// assumes that if user is a moderator, then user is also an occupant in
		// the MUC
		chatRoom.addParticipantListener(new PacketListener() {
			public void processPacket(Packet packet) {
				boolean userFound = false;
				for (Entry<User, String> entry : invitedUsersToConference
						.entrySet()) {
					if (entry.getKey().getUsername().equals(packet.getFrom())) {
						// if user has been invited to a chat, call the addUser
						// method to update model information
						// and remove the user from the invitedUsersToConference
						// method.
						if (D)
							Log.v(TAG, "User " + entry.getKey()
									+ " has accepted an invitation");
						addUser(entry.getKey(), entry.getValue());
						if (D)
							Log.v(TAG,
									"Updated model information to reflect User "
											+ entry.getKey()
											+ " joining the MUC");
						invitedUsersToConference.remove(entry.getKey());
						break;
					}
				}
			}
		});
		chatRoom.invite(u.getUsername(), null);
		invitedUsersToConference.put(u, sChat);

		// TODO - Consider implementing sending a message (like in the old code)
		// to Moderator
		// requesting an invitation if the current user is not a moderator
	}

	public void addUser(User u, String sChat) {
		ConferenceRoom chatRoom = findChat(sChat);
		//_conference.getIDMap().get(chatRoom.getRoomID()).addUser(u); TODO 
	}

	private ConferenceRoom findChat(String sChat) {
		// find roomID of the chat (may be replaced with
		// _conference.getActiveChat();
		// if we can be confident that the active chat is the same as sChat
		String roomID = sChat;

		// roomId = _conference.getActiveChat();
		HashMap<String, ConferenceRoom> chatSpaceIDMap = conferenceModel.getIDMap();
		ConferenceRoom chatRoom = chatSpaceIDMap.get(roomID);
		return chatRoom;
	}

	// sChat is the indicator that it is the left side chat or the right
	// sidechat
	// it may be better to use an int or constant ot indicate left/right side
	// chat
	public void leaveChat(String sChat, User currentUser) throws XMPPException {

		HashMap<String, ConferenceRoom> conferencemap = conferenceModel.getIDMap();
		ConferenceRoom spacechat = conferencemap.get(sChat);
		User moderator = spacechat.getModerator();

		if (moderator.getUsername().equals(currentUser.getUsername())) { // if
																			// moderator
			User newmoderator = null; // ask for new moderator if current
										// moderator is leaving
			User new_sidemoderator = null; // find the new_sidemoderator for
											// this side chat
			if (conferenceModel.getIsmain()) { // if in mainchat
				String[] keys = (String[]) conferencemap.keySet().toArray();
				for (int i = 0; i <= 2; i++) {
					if (keys[i] != sChat) { // if not mainchat i.e. sidechats,
											// transfer privileges and then
											// leave
						ConferenceRoom spacechat1 = conferencemap.get(keys[i]);
						String name = spacechat1.getRoomID();
						transferPrivileges(currentUser, newmoderator,
								name);
						spacechat1.leave();
						conferenceModel.setIsmain(true); // not sure if this is
														// needed, please check
					}
					String name = spacechat.getRoomID();
					transferPrivileges(newmoderator, currentUser, name); // finally
																				// leave
																				// the
																				// mainchat
					spacechat.leave();
				}
			} else { // if this is a side chat than only for that
				String name = spacechat.getRoomID();
				transferPrivileges(newmoderator, currentUser, name);
				spacechat.leave();
			}
		}

		else { // if only a user
			if ((conferenceModel.getIsmain())) { // if main then leave side chat
												// also
				String[] keys = (String[]) conferencemap.keySet().toArray();
				for (int i = 0; i <= 2; i++) {
					if (keys[i] != sChat) { // if not mainchat i.e. sidechats
											// leave
						ConferenceRoom spacechat1 = conferencemap.get(keys[i]);
						spacechat1.leave();
					}
					spacechat.leave(); // leave main
				}
			} else {
				spacechat.leave(); // leave the side chat
			}
		}
	}

	public void endConference(String conf, User u) {
		ConferenceRoom chat = this.conferenceModel.getIDMap().get(conf);
		if (chat.getModerator().equals(u)) {
			HashMap<String, ConferenceRoom> allChats = this.conferenceModel
					.getIDMap();
			for (String s : allChats.keySet()) {
				try {
					allChats.get(s).destroy(null, null);
				} catch (XMPPException e) {
					Log.e("ConferenceController",
							"XMPPError when destroying a MUC");
				}
			}
		}
	}

	/**
	 * 
	 * @param u1
	 *            current moderator leaving the chat
	 * @param u2
	 *            new moderator
	 * @param room
	 *            ChatSpaceModel in which the privilege has to be transferred
	 * @throws XMPPException
	 */
	public void transferPrivileges(User u1, User u2, String chat)
			throws XMPPException {
		ConferenceRoom room = this.conferenceModel.getIDMap().get(chat);
		room.updateMod(u1, u2);
	}

	public void kickoutUser(String chat, User userToBeKicked, User currUser) {
		if (this.conferenceModel.getIDMap().get(chat).getModerator()
				.equals(currUser)) {
			//AnKit
//			this._conference.getIDMap().get(chat).getAllNicknames()
//					.remove(userToBeKicked.getNickname());
//			this._conference.getIDMap().get(chat).getAllParticipants()
//					.remove(userToBeKicked.getUsername());
			try {
				this.conferenceModel.getIDMap().get(chat)
						.kickParticipant(userToBeKicked.getNickname(), null);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}



	// TODO: the following methods will not be implemented for the Nov4 cycle

	public void moveUser(User u) {
		// TODO: modify audio manipulation based on new location (future work)
	}

	public void init() {
		// TODO Back End
		// 1. Open a channel to listen to incoming messages / register this
		// class with the connection

	}

	public void handleIncomingPackets() {
		// Handle
		// 1. Invitation response
		// 2. Priv transfer
	}

	public void sendPackets() {
		// Send
		// 1.invitation
		// 2.trans_priv
	}

	public void handleOverflow() {
		Toast.makeText(this.context, "Overflow Clicked", Toast.LENGTH_SHORT).show(); 
	}
	
	public void addPersonClicked() {
		Toast.makeText(this.context, "Add Contact Clicked", Toast.LENGTH_SHORT).show();
		// TODO- Should go to add contact to conference page
	}

	public void muteClicked() {
		Toast.makeText(this.context, "Mute Clicked", Toast.LENGTH_SHORT).show();
	}

	public void leaveConference() {
		Toast.makeText(this.context, "Leave Conference Clicked", Toast.LENGTH_SHORT).show();
	}

	public void removeUser() {
		Toast.makeText(this.context, "Remove User", Toast.LENGTH_SHORT).show();
	}

	public void setNewModerator() {
		Toast.makeText(this.context, "Set New Moderator", Toast.LENGTH_SHORT).show();

	}
	
	public void showProfile() {
		Toast.makeText(this.context, "Show Profile", Toast.LENGTH_SHORT).show();
	}

	public void handleEndClicked() {
		Toast.makeText(this.context, "End conference", Toast.LENGTH_SHORT).show();		
	}

	public void handleOnContextAddClicked() {
		Toast.makeText(this.context, "Add Contact clicked", Toast.LENGTH_SHORT).show();		
	}
	
	

}
