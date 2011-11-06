package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.view.InvitationView;

/**
 * Controller class for MultiUserChat invitations
 * @author jonathan
 *
 */
public class InvitationController implements InvitationListener, InvitationRejectionListener {
	private edu.cornell.opencomm.model.Invitation invitation;
	
	private InvitationView invitationView = null;
	public InvitationController(InvitationView invitationView) {
		this.invitationView = invitationView;
	}
	public void handlePopupWindowClicked() {
		invitationView.getWindow().dismiss();		
	}
	public void handleAcceptButtonHover() {
		invitationView.getAcceptButton().setBackgroundColor(R.color.light_grey);
		
	}
	public void handleCancelButtonHover() {
		invitationView.getCancelButton().setBackgroundColor(R.color.light_grey);
	}

	
	/**
	 * Decline the invitation
	 * @param inviter - Person who invited you to the chat
	 */
	public void decline(String inviter) {
		MultiUserChat.decline(this.invitation.getConnection(), this.invitation.getRoom(), inviter, null);
	}
	
	/**
	 * Accept the invitation
	 * @param username - Your username
	 * @return The chat room you just joined
	 * @throws XMPPException
	 */
	MultiUserChat accept(String username) throws XMPPException {
		MultiUserChat chat = new MultiUserChat(this.invitation.getConnection(), this.invitation.getRoom());
		chat.join(username);
		return chat;
	}

	/**
	 * Automagically called when this client receives an invitation to join a MUC
	 */
	@Override
	public void invitationReceived(Connection connection, String room, String inviter, String reason, String password, Message message) {
		this.invitation = new edu.cornell.opencomm.model.Invitation(connection, room, inviter, reason, password, message);
		
		//TODO: Trigger update to the view!
	}
	
	/**
	 * Returns the most recent invitation received
	 * @return InvitationController
	 */
	public edu.cornell.opencomm.model.Invitation getInvitation() {
		return this.invitation;
	}

	/**
	 * Automagically called when an invitation this client sent was rejected (oh snap!)
	 */
	@Override
	public void invitationDeclined(String invitee, String reason) {
		//TODO: Trigger update to view (if any)
	}
}
