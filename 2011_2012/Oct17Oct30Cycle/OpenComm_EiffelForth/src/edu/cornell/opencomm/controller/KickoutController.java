package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

/**
 * Class to handle kickouts/kickout requests
 * @author jonathanpullano
 *
 */
public class KickoutController {

	private static final String TAG = "KickoutController";
	private static final boolean D = true;

	private Space space;


	/** ==========================================
	 * ===========================================
	 * CUSTOM LISTENERS:
	 * receiveKickoutRequest
	 * confirmKickoutRequest
	 * rejectKickoutRequest
	 * receiveKickoutRequestRejection
	 * ===========================================
	 * ===========================================
	 */

	/**
	 * Constructor
	 * @param space - The space associated with this control
	 */
	public KickoutController(Space space) {
		this.space = space;
	} // end KickoutController constructor

	/**
	 * If you are the owner, kick the user from the chat
	 * Otherwise, send out an invitation request
	 * @param kickMe - The user to be kicked
	 * @param reason - The reason string associated with the kick
	 * @throws XMPPException
	 */
	public void kickoutUser(User kickMe, String reason) throws XMPPException {
		Occupant userOcc = this.space.getMUC().getOccupant(MainApplication.user_primary.getUsername()
				+ "/" + MainApplication.user_primary.getNickname());
		// if the primary user is the room's owner
		if (userOcc.getAffiliation().equals(Network.ROLE_OWNER)) {
			this.space.getMUC().kickParticipant(kickMe.getNickname(), reason);
		} else {
			// message containing kickout request tag, the username of the kicker,
			// the username of the kickee, and the reason
			Message msg = new Message(Network.REQUEST_KICKOUT + "@requester" +
					MainApplication.user_primary.getUsername() + "@kickee" +
					kickMe.getUsername() + "@reason" +
					((reason == null) ? Network.DEFAULT_KICKOUT : reason),
					Message.Type.groupchat);
			try {
				this.space.getMUC().sendMessage(msg);
			} catch (XMPPException e) {
				if (D) Log.d(TAG, "inviteUser - message not sent: "
						+ e.getXMPPError().getCode() + " - " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
		}
	} // end kickOutUser method

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
}
