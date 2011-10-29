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

	
	/**
	 * Called when a non-owner tries to kick a user from a space
	 * 
	 * @param kickoutRequest
	 *            - message sent to the room when a non-owner tries to kick a
	 *            user from a room. Contains the KickoutRequest Tag<br>
	 *            (format: (Network.REQUEST_KICKOUT)@requester(Requester's JID)@kickee
	 *            (Kickee's JID)@reason(Kickout reason)
	 *            
	 * @return Information of kickout request received when a non-owner tries to
	 * kick a user from a space in String array format {requesterJID,
	 * kickeeJID, kickReason}
	 */
	public String[] receiveKickoutRequest(String kickoutRequest) {
		// Check the kickoutRequest is accurate
		if (kickoutRequest.contains(Network.REQUEST_KICKOUT)) {
			// extract kickout request info
			String kickoutRequestInfo = kickoutRequest
					.split(Network.REQUEST_KICKOUT)[0];
			String requester = (kickoutRequestInfo.split("@requester")[0])
					.split("@kickee")[0];
			String kickee = (kickoutRequestInfo.split("@requester" + requester
					+ "@kickee")[0]).split("@reason")[0];
			String reason = (kickoutRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_KICKOUT
					: kickoutRequestInfo.split("@reason")[0]);
			String[] kickoutInfo = { requester, kickee, reason };
			// DEBUG
			if (D)
				Log.d(TAG,
						"receiveKickoutRequest - received kickout request from "
								+ kickoutInfo[0] + " for room "
								+ this.mSpace.getRoomID() + " for user "
								+ kickoutInfo[1] + ": reason - "
								+ kickoutInfo[2]);
			return kickoutInfo;
		}
		// else
		Log.e(TAG, "receiveKickoutRequest - incorrectly called");
		return null;
	} // end receiveKickoutRequest method

	/**
	 * Confirm an kickout request. Kick out th euser with the reason given 
	 * by the requester
	 * 
	 * @param kickoutInfo
	 *            - String array: {requesterJID, kickeeJID, kickoutReason}
	 */
	public void confirmKickoutRequest(String[] kickoutInfo) {
		// Check the kickoutInfo is not null and length 3
		if (kickoutInfo != null && kickoutInfo.length == 3) {
			// kickout the user
			// TODO Risa - enter Jonathan's kickout code
			// DEBUG
			if (D)
				Log.d(TAG,
						"confirmKickoutRequest - confirmed kickout request from "
								+ kickoutInfo[0] + " for room "
								+ this.mSpace.getRoomID() + " for user "
								+ kickoutInfo[1] + " (reason - "
								+ kickoutInfo[2] + ")");
		}
	} // end confirmKickoutRequest method

	public void rejectKickoutRequest(String[] kickoutInfo, String reason) {
		// Check that the kickoutInfo is valid
		if (kickoutInfo != null && kickoutInfo.length == 3) {
			// send the room the rejection notification
			Message msg = new Message(Network.REJECT_KICKOUT + "@requester"
					+ kickoutInfo[0] + "@invitee" + kickoutInfo[1] + "@reason"
					+ kickoutInfo[2] + "@rejectreason"
					+ (reason == null ? Network.DEFAULT_REJECT : reason),
					Message.Type.groupchat);
			try {
				this.muc.sendMessage(msg);
			} catch (XMPPException e) {
				if (D)
					Log.d(TAG, "rejectKickoutRequest - message not sent: "
							+ e.getXMPPError().getCode() + " - "
							+ e.getXMPPError().getMessage());
				e.printStackTrace();
			}
		}
		// DEBUG
		if (D)
			Log.d(TAG,
					"rejectKickoutRequest - rejected kickout request from "
							+ kickoutInfo[0] + " for room "
							+ this.mSpace.getRoomID() + " for user "
							+ kickoutInfo[1] + "(reason - " + kickoutInfo[2]
							+ ") : rejection reason - " + reason);
	} // end rejectKickoutRequest method

	public String[] receiveKickoutRequestRejection(String requestReject) {
		// Check that the requestReject is valid
		if (requestReject.contains(Network.REJECT_KICKOUT)) {
			String requestRejectInfo = requestReject
					.split(Network.REJECT_KICKOUT)[0];
			String requester = (requestRejectInfo.split("@requester")[0])
					.split("@kickee")[0];
			// Check that the requester is the primary user
			if (MainApplication.user_primary.getUsername().contains(requester)) {
				String kickee = (requestRejectInfo.split("@requester"
						+ requester + "@kickee")[0]).split("@reason")[0];
				String reason = (requestRejectInfo.split("@reason")[1])
						.split("@rejectreason")[0];
				String rejectReason = requestRejectInfo.split("@rejectreason")[1];
				String[] rejectInfo = {requester, kickee, reason,
						rejectReason };
				// TODO UI - send information around request reject to screen?
				// DEBUG
				if (D)
					Log.d(TAG, "receiveKickoutRequestRejection - received "
							+ "rejection of kickout request " + " for room "
							+ this.mSpace.getRoomID() + " for user "
							+ rejectInfo[1] + "(reason - " + rejectInfo[2]
							+ ": reason - " + rejectInfo[3]);
				return rejectInfo;
			}
			if (D)
				Log.d(TAG, "receiveKickoutRequestRejection - primary user "
						+ "did not submit this request");
		} else {
			Log.e(TAG, "receiveKickoutRequestRejection - wrong tag");
		}
		return null;
	} // end receiveKickoutRequestRejection method
}
