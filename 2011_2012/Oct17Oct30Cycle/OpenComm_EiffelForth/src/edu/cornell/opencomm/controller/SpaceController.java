package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import android.util.Log;

import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

/** An instance of this class is a space controller for each space (main space 
 * or private space), such as adding/deleting people from a space.
 * @author OpenComm (cuopencomm@gmail.com)
 *
 */
public class SpaceController {
	// Debugging
	private static final String TAG = "SpaceController";
	private static final boolean D = true;
	
	// Model objects
	private Space space; // the space that is controlled
	
	// Network objects
	private MultiUserChat muc;
	
	// View objects
	SpaceView view;
	
	/** Constructor: a new instance of SpaceController that controls a specific 
	 * space
	 * @param space - the space to be controlled */
	public SpaceController(Space space, SpaceView view) {
		this.space = space;
		this.muc = this.space.getMUC();
		this.view = view;

	} // end SpaceController method

	
	/** ============================================================================================
	 * =============================================================================================
	 * 
	 * REQUESTS:
	 * inviteUser - send invitation to user
	 * confirmInvitationRequest - confirm invitation request from another user
	 * declineInvitationRequest - decline invitation request from another user
	 * confirmKickoutRequest - confirm kickout request from another user
	 * declineKickoutRequest - decline kickout request from another user
	 * 
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
		Occupant userOcc = this.muc.getOccupant(MainApplication.user_primary.getUsername() 
				+ "/" + MainApplication.user_primary.getNickname());
		// if the primary user is the room's owner
		if (userOcc.getAffiliation().equals(Network.ROLE_OWNER)) {
			this.muc.invite(invitee.getUsername(), ((reason == null) ? Network.DEFAULT_INVITE : reason));
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
				this.muc.sendMessage(msg);
			} catch (XMPPException e) {
				if (D) Log.d(TAG, "inviteUser - message not sent: "
						+ e.getXMPPError().getCode() + " - " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
		}
	} // end inviteUser method
	
	public void kickoutUser(User kickMe, String reason) throws XMPPException {
		if(kickMe.getUsername().equals(MainApplication.user_primary.getUsername())) {
			this.muc.kickParticipant(kickMe.getNickname(), reason);
		} else {
			// TODO Jonathan - if not, send kickout request
		}
		
	}
	
	public void deleteSpace() {
		if(MainApplication.user_primary.getUsername().equals(this.space.getOwner().getUsername())) {
			
		}
	}
	
	public void addSpace() {
		if(MainApplication.user_primary.getUsername().equals(this.space.getOwner().getUsername())) {
			
		}
	}
	
	/** END REQUESTS
	 * 
	 * =============================================================================================
	 * =============================================================================================
	 * =============================================================================================
	 * =============================================================================================
	 * 
	 * CUSTOM LISTENERS:
	 * receiveInvitationRequest
	 * confirmInvitationRequest
	 * rejectInvitationRequest
	 * receiveInvitationRequestRejection
	 * receiveKickoutRequest
	 * confirmKickoutRequest
	 * rejectKickoutRequest
	 * receiveKickoutRequestRejection
	 * 
	 * ===========================================
	 * ===========================================
	 */
	
	/** = Information of invitation request received when a non-owner tries to invite a user to a 
	 * space in String array format {requesterJID, inviteeJID, inviteReason}
	 * @param inviteRequest - message sent to the room when a non-owner tries to 
	 * invite a user to a room. Contains the InviteRequest Tag<br>
	 * (format: (Network.REQUEST_INVITE)@requester(Requester's JID)@invitee
	 * (Invitee's JID)@reason(Invitation reason)
	 */
	public String[] receiveInvitationRequest(String inviteRequest) {
		// Check the inviteRequest is accurate
		if (inviteRequest.contains(Network.REQUEST_INVITE)) {
			// extract invitation request info
			String inviteRequestInfo = inviteRequest.split(Network.REQUEST_INVITE)[0];
			String requester = (inviteRequestInfo.split("@requester")[0]).split("@invitee")[0];
			String invitee = (inviteRequestInfo.split("@requester" + requester + "@invitee")[0]).split("@reason")[0];
			String reason = (inviteRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_INVITE : inviteRequestInfo.split("@reason")[0]);
			String[] inviteInfo = {requester, invitee, reason};
			// DEBUG
			if (D) Log.d(TAG, "receiveInvitationRequest - received invitation request from " 
					+ inviteInfo[0] + " for room " + this.space.getRoomID() + " for user "
					+ inviteInfo[1] + ": reason - " + inviteInfo[2]);
			return inviteInfo;
		}
		// else
		Log.e(TAG, "receiveInvitationRequest - incorrectly called");
		return null;
	} // end receiveInvitationRequest method
	
	/** Confirm an invitation request. Invite the invitee with the reaosn given by the requester
	 * @param inviteInfo - String array: {requesterJID, inviteeJID, inviteReason}
	 */
	public void confirmInvitationRequest(String[] inviteInfo) {
		// Check the inviteInfo is not null and length 3
		if (inviteInfo != null && inviteInfo.length == 3) {
			// invite the invitee
			this.muc.invite(inviteInfo[1], inviteInfo[2]);
			// DEBUG
			if (D) Log.d(TAG, "confirmInvitationRequest - confirmed invitation request from " 
					+ inviteInfo[0] + " for room " + this.space.getRoomID() + " for user "
					+ inviteInfo[1] + " (reason - " + inviteInfo[2] + ")");
		}
	} // end confirmInvitationRequest method
	
	/** Reject invitation request. Sends a message to the room rejecting the invitation request 
	 * along with the reason for the rejection
	 * @param inviteInfo - inviteInfo - String array: {requesterJID, inviteeJID, inviteReason}
	 * @param reason - reason for the rejection
	 */
	public void rejectInvitationRequest(String[] inviteInfo, String reason) {
		// Check that the inviteInfo is valid
		if (inviteInfo != null && inviteInfo.length == 3) {
			// send the room the rejection notification
			Message msg = new Message(Network.REJECT_INVITE + "@requester" + 
					inviteInfo[0] + "@invitee" + inviteInfo[1] + "@reason" + 
					inviteInfo[2] + "@rejectreason" +
					(reason == null ? Network.DEFAULT_REJECT : reason),
					Message.Type.groupchat);
		}
		// DEBUG
		if (D) Log.d(TAG, "rejectInvitationRequest - rejected invitation request from " 
				+ inviteInfo[0] + " for room " + this.space.getRoomID() + " for user "
				+ inviteInfo[1] + "(reason - " + inviteInfo[2] + ") : rejection reason - " + reason);
	} // end rejectInvitationRequest method
	
	public void receiveInvitationRejectionRequest(){
		// TODO Risa - receive rejection of invitation request
	}
	
	public String[] receiveKickoutRequest(String kickoutRequest) {
		// Check the kickoutRequest is accurate
		if (kickoutRequest.contains(Network.REQUEST_KICKOUT)) {
			// extract kickout request info
			String kickoutRequestInfo = kickoutRequest.split(Network.REQUEST_KICKOUT)[0];
			String requester = (kickoutRequestInfo.split("@requester")[0]).split("@kickee")[0];
			String kickee = (kickoutRequestInfo.split("@requester" + requester + "@kickee")[0]).split("@reason")[0];
			String reason = (kickoutRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_KICKOUT : kickoutRequestInfo.split("@reason")[0]);
			String[] kickoutInfo = {requester, kickee, reason};
			// DEBUG
			if (D) Log.d(TAG, "receiveKickoutRequest - received kickout request from " 
					+ kickoutInfo[0] + " for room " + this.space.getRoomID() + " for user "
					+ kickoutInfo[1] + ": reason - " + kickoutInfo[2]);
			return kickoutInfo;
		}
		// else
		Log.e(TAG, "receiveInvitationRequest - incorrectly called");
		return null;
	}
	public void confirmKickoutRequest(String[] kickoutInfo) {
		// TODO Risa - confirm kickout request
	}
	public void rejectKickoutRequest(String[] kickoutInfo, String reason) {
		// TODO Risa - reject kickout request
	}
	public void receiveKickoutRequestRejection() {
		// TODO Risa - receive rejection of kickout request
	}

} // end Class SpaceController
