package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

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
	}

	public void kickoutUser(User kickMe, String reason) throws XMPPException {
		if(kickMe.getUsername().equals(MainApplication.user_primary.getUsername())) {
			this.space.getMUC().kickParticipant(kickMe.getNickname(), reason);
		} else {
			// TODO Jonathan - if not, send kickout request
		}

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
}
