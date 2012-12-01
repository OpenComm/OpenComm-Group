package edu.cornell.opencomm.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
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

	// users who have been invited who have not been added to the model
	// information

	// TODO: add a listener for invitations and invitation responses

	public ConferenceController(ConferenceView view, ConferenceDataModel model) {
		// TODO: do we really need this
		this.context = view;
		this.view = view;
		this.conferenceModel = model;
	}

	// Constructor - initialize required fields
	public ConferenceController(ConferenceDataModel conf) {
		this.conferenceModel = conf;
	}

	public void inviteUser(User u, String sChat) {
		ConferenceRoom chatRoom = findChat(sChat);
		chatRoom.invite(u.getUsername(), null);
	}

	public void addUser(String u, String sChat) {
		ConferenceRoom chatRoom = findChat(sChat);
		Occupant o = chatRoom.getOccupant(u);
		VCard user_data = new VCard();
		try {
			user_data.load(NetworkService.getInstance().getConnection(), u);
		} catch (XMPPException e) {
			Log.v(TAG, "couldn't get new user's info from VCard");
			Log.v(TAG, "User is :" + u);
		}
		User added = new User(user_data.getFirstName(),
				user_data.getLastName(), user_data.getEmailHome(), null, null,
				u, user_data.getNickName());
		chatRoom.addUser(added);
	}

	private ConferenceRoom findChat(String sChat) {
		String roomID = sChat;
		HashMap<String, ConferenceRoom> chatSpaceIDMap = conferenceModel
				.getIDMap();
		ConferenceRoom chatRoom = chatSpaceIDMap.get(roomID);
		return chatRoom;
	}

	// sChat is the indicator that it is the left side chat or the right
	// sidechat
	// it may be better to use an int or constant ot indicate left/right side
	// chat
	public void leaveChat(String sChat, User currentUser) {
		ConferenceRoom room = conferenceModel.getIDMap().get(sChat);
		if (room.getModerator().equals(UserManager.PRIMARY_USER)) {
			// TODO : choose user to make moderator
			room.setModerator(null);
		}
		room.leave();
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
		Toast.makeText(this.context, "Overflow Clicked", Toast.LENGTH_SHORT)
				.show();
	}

	public void addPersonClicked() {
		Toast.makeText(this.context, "Add Contact Clicked", Toast.LENGTH_SHORT)
				.show();
		// TODO- Should go to add contact to conference page
	}

	public void muteClicked() {
		Toast.makeText(this.context, "Mute Clicked", Toast.LENGTH_SHORT).show();
	}

	public void leaveConference() {
		Toast.makeText(this.context, "Leave Conference Clicked",
				Toast.LENGTH_SHORT).show();
	}

	public void removeUser() {
		Toast.makeText(this.context, "Remove User", Toast.LENGTH_SHORT).show();
	}

	public void setNewModerator() {
		Toast.makeText(this.context, "Set New Moderator", Toast.LENGTH_SHORT)
				.show();

	}

	public void showProfile() {
		Toast.makeText(this.context, "Show Profile", Toast.LENGTH_SHORT).show();
	}

	public void handleEndClicked() {
		Toast.makeText(this.context, "End conference", Toast.LENGTH_SHORT)
				.show();
	}

	public void handleOnContextAddClicked() {
		Toast.makeText(this.context, "Add Contact clicked", Toast.LENGTH_SHORT)
				.show();
	}

}
