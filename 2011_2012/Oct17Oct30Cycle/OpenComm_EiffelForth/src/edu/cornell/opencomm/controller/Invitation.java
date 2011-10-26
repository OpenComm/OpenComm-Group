package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Controller class for MultiUserChat invitations
 * @author jonathan
 *
 */
public class Invitation implements InvitationListener {
	private edu.cornell.opencomm.model.Invitation invitation;
	
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
	 * @return Invitation
	 */
	public edu.cornell.opencomm.model.Invitation getInvitation() {
		return this.invitation;
	}
}
