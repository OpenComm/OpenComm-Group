package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import android.content.Context;
import android.widget.Toast;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConferenceView;

public class ConferenceController {

	Context context;

	private Conference conferenceModel;

	private static final String TAG = "ConferenceController";

	// users who have been invited who have not been added to the model
	// information

	public ConferenceController(ConferenceView view, Conference model) {
		this.context = view;
		this.conferenceModel = model;
	}

	// Constructor - initialize required fields
	public ConferenceController(Conference conf) {
		this.conferenceModel = conf;
	}

	public void inviteUser(User u, String sChat) {
		conferenceModel.getMUC().invite(u.getUsername(), null);
	}

	public void end(User currUser){
		
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
	public void transferPrivileges(User u1)
	throws XMPPException {
		conferenceModel.getMUC().grantModerator(u1.getUsername());
	}

	public void kickoutUserByRoomID(User userToBeKicked) {
		try {
			conferenceModel.getMUC().kickParticipant(userToBeKicked.getUsername(), "you suck");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
		Toast.makeText(this.context, "Set New Moderator", Toast.LENGTH_SHORT).show();
		//TODO
	}

	public void showProfile() {
		Toast.makeText(this.context, "Show Profile", Toast.LENGTH_SHORT).show();
	}

	public void handleEndClicked(User user) {
		// TODO won't work until backend finds a way to retrieve the moderator (as of now moderator is null)
		// end(user);
	}

	public void handleLeaveClicked(int roomIndex, User user){
		// TODO won't work until backend fins a way to retrieve the moderator (as of now moderator is null)
		//leaveChat(roomIndex, user); 
	}

	public void handleOnContextAddClicked() {
		Toast.makeText(this.context, "Add Contact clicked", Toast.LENGTH_SHORT)
				.show();
	}
}
