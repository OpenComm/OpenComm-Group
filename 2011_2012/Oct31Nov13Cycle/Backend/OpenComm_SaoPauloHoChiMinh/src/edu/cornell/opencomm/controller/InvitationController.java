package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

/**
 * Controller class for MultiUserChat invitations
 * @author jonathanpullano, risanaka, kriskooi
 */
public class InvitationController implements InvitationListener, InvitationRejectionListener {
	private edu.cornell.opencomm.model.Invitation invitation;

	// Model variables
	private Space mSpace;

	private static final String TAG = "InvitationController";
	private static final boolean D = true;

	/**
	 * Constructor
	 * @param mSpace - The space associated with this controller
	 */
	public InvitationController(Space mSpace) {
		this.mSpace = mSpace;
		this.mSpace.getMUC();
		this.mSpace.getMUC().addInvitationRejectionListener(this);
		
		//TODO: Move this somewhere where it will only get called once (NetworkService?)
		MultiUserChat.addInvitationListener(
				Login.xmppService.getXMPPConnection(), this);
	}

	/** =============================================================================================
	 * =============================================================================================
	 *
	 * CUSTOM LISTENERS:
	 * receiveInvitationRequest
	 * confirmInvitationRequest
	 * rejectInvitationRequest
	 * receiveInvitationRequestRejection
	 * ===========================================
	 * ===========================================
	 */

	/** Sends an invitation user to a room. If the primary user is the owner of the room,
	 * it sends the invitee an invitation to the space. If the primary user is not,
	 * the user sends a request to the room requesting an invitation request
	 * @param invitee - user to invite
	 * @param reason - reason for inviting the user to the space
	 * <p>TODO when receiving messages, check all messages for the InviteRequest tag
	 * (Network.REQUEST_INVITE); when it's shown, do not show the message;
	 * rather, call the method confirmInvitationRequest method</p> */
	public void inviteUser(User invitee, String reason) {
		Occupant userOcc = mSpace.getAllOccupants().get(MainApplication.user_primary.getUsername());
		//DEBUG
		Log.v(TAG, "Is userOcc valid for " + MainApplication.user_primary.getUsername()
				+ (userOcc != null));
		// if the primary user is the room's owner
		if (userOcc.getAffiliation().equals(Network.ROLE_OWNER)) {
			this.mSpace.getMUC().invite(invitee.getUsername(), ((reason == null)
					? Network.DEFAULT_INVITE : reason));
		}
		// send message to owner invitation request
		else {
			// message containing invite request tag, the username of the inviter,
			// the username of the invitee, and the reason
			Message msg = new Message(Network.REQUEST_INVITE + "@inviter" +
					MainApplication.user_primary.getUsername() + "@invitee" +
					invitee.getUsername() + "@reason" +
					((reason == null) ? Network.DEFAULT_INVITE : reason),
					Message.Type.groupchat);
			try {
				this.mSpace.getMUC().sendMessage(msg);
			} catch (XMPPException e) {
				if (D) Log.d(TAG, "inviteUser - message not sent: "
						+ e.getXMPPError().getCode() + " - " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
		}
	} // end inviteUser method

	/**
	 * = Information of invitation request received when a non-owner tries to
	 * invite a user to a space in String array format {requesterJID,
	 * inviteeJID, inviteReason}
	 *
	 * @param inviteRequest
	 *            - message sent to the room when a non-owner tries to invite a
	 *            user to a room. Contains the InviteRequest Tag<br>
	 *            (format: (Network.REQUEST_INVITE)@requester(Requester's JID)@invitee
	 *            (Invitee's JID)@reason(Invitation reason)
	 */
	public String[] receiveInvitationRequest(String inviteRequest) {
		// Check the inviteRequest is accurate
		if (inviteRequest.contains(Network.REQUEST_INVITE)) {
			// extract invitation request info
			String inviteRequestInfo = inviteRequest
					.split(Network.REQUEST_INVITE)[0];
			String requester = (inviteRequestInfo.split("@requester")[0])
					.split("@invitee")[0];
			String invitee = (inviteRequestInfo.split("@requester" + requester
					+ "@invitee")[0]).split("@reason")[0];
			String reason = (inviteRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_INVITE
					: inviteRequestInfo.split("@reason")[0]);
			String[] inviteInfo = { requester, invitee, reason };
			// DEBUG
			if (D)
				Log.d(TAG,
						"receiveInvitationRequest - received invitation request from "
								+ inviteInfo[0] + " for room "
								+ this.mSpace.getRoomID() + " for user "
								+ inviteInfo[1] + ": reason - " + inviteInfo[2]);
			return inviteInfo;
		}
		// else
		Log.e(TAG, "receiveInvitationRequest - incorrectly called");
		return null;
	} // end receiveInvitationRequest method

	/**
	 * Confirm an invitation request. Invite the invitee with the reaosn given
	 * by the requester
	 *
	 * @param inviteInfo
	 *            - String array: {requesterJID, inviteeJID, inviteReason}
	 */
	public void confirmInvitationRequest(String[] inviteInfo) {
		// Check the inviteInfo is not null and length 3
		if (inviteInfo != null && inviteInfo.length == 3) {
			// invite the invitee
			// TODO Risa - enter Jonathan's invite code
			// DEBUG
			if (D)
				Log.d(TAG,
						"confirmInvitationRequest - confirmed invitation request from "
								+ inviteInfo[0] + " for room "
								+ this.mSpace.getRoomID() + " for user "
								+ inviteInfo[1] + " (reason - " + inviteInfo[2]
								+ ")");
		}
	} // end confirmInvitationRequest method

	/**
	 * Reject invitation request. Sends a message to the room rejecting the
	 * invitation request along with the reason for the rejection
	 *
	 * @param inviteInfo
	 *            - inviteInfo - String array: {requesterJID, inviteeJID,
	 *            inviteReason}
	 * @param reason
	 *            - reason for the rejection
	 */
	public void rejectInvitationRequest(String[] inviteInfo, String reason) {
		// Check that the inviteInfo is valid
		if (inviteInfo != null && inviteInfo.length == 3) {
			// send the room the rejection notification
			Message msg = new Message(Network.REJECT_INVITE + "@requester"
					+ inviteInfo[0] + "@invitee" + inviteInfo[1] + "@reason"
					+ inviteInfo[2] + "@rejectreason"
					+ (reason == null ? Network.DEFAULT_REJECT : reason),
					Message.Type.groupchat);
			try {
				this.mSpace.getMUC().sendMessage(msg);
			} catch (XMPPException e) {
				if (D)
					Log.d(TAG, "rejectInvitationRequest - message not sent: "
							+ e.getXMPPError().getCode() + " - "
							+ e.getXMPPError().getMessage());
				e.printStackTrace();
			}
		}
		// DEBUG
		if (D)
			Log.d(TAG,
					"rejectInvitationRequest - rejected invitation request from "
							+ inviteInfo[0] + " for room "
							+ this.mSpace.getRoomID() + " for user "
							+ inviteInfo[1] + "(reason - " + inviteInfo[2]
							+ ") : rejection reason - " + reason);
	} // end rejectInvitationRequest method

	/**
	 * Called when a user who is not the owner of the room receives a rejection
	 * for its invitation requests.
	 *
	 * @param inviteRequest
	 *            - message sent to the room when a owner rejects an invitation request
	 *            user to a room. Contains the Reject_Invite Tag<br>
	 *            (format: (Network.REJECTT_INVITE)@requester(Requester's JID)@invitee
	 *            (Invitee's JID)@reason(Invitation reason)@rejectionreason(Rejection
	 *            reason)
	 *
	 * @return Information of rejection of invitation request received from the
	 * owner of the room in String array format {requesterJID,
	 * inviteeJID, inviteReason, rejectionReason}
	 */
	public String[] receiveInvitationRequestRejection(String requestReject) {
		// Check that the requestReject is valid
		if (requestReject.contains(Network.REJECT_INVITE)) {
			String requestRejectInfo = requestReject
					.split(Network.REJECT_INVITE)[0];
			String requester = (requestRejectInfo.split("@requester")[0])
					.split("@invitee")[0];
			// Check that the requester is the primary user
			if (MainApplication.user_primary.getUsername().contains(requester)) {
				String invitee = (requestRejectInfo.split("@requester"
						+ requester + "@invitee")[0]).split("@reason")[0];
				String reason = (requestRejectInfo.split("@reason")[1])
						.split("@rejectreason")[0];
				String rejectReason = requestRejectInfo.split("@rejectreason")[1];
				String[] rejectInfo = { requester, invitee, reason,
						rejectReason };
				// TODO UI - send information around request reject to screen?
				// DEBUG
				if (D)
					Log.d(TAG, "receiveInvitationRequestRejection - received "
							+ "rejection of invitation request " + " for room "
							+ this.mSpace.getRoomID() + " for user "
							+ rejectInfo[1] + "(reason - " + rejectInfo[2]
							+ ": reason - " + rejectInfo[3]);
				return rejectInfo;
			}
			if (D)
				Log.d(TAG, "receiveInvitationRequestRejection - primary user "
						+ "did not submit this request");
		} else {
			Log.e(TAG, "receiveInvitationRequestRejection - wrong tag");
		}
		return null;
	} // end receiveInvitationRequestRejection

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
		
		//DEBUG
		Log.v(TAG, "invitationReceived - Invitation received from: " + inviter 
				+ " to join room: " + room);
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
		
		//DEBUG
		Log.v(TAG, "invitationDeclined - Invitee: " + invitee + 
				" declined invitation because: " + reason);
	}
}
