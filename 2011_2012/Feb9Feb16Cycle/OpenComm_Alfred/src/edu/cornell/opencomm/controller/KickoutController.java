package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.Occupant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConfirmationView;

/**
 * Class to handle kickouts/kickout requests
 * 
 * @author jonathanpullano, risanaka, kriskooi
 * 
 */
public class KickoutController {

    private static final String TAG = "KickoutController";
    private static final boolean D = true;

    private Space mSpace;
    private ConfirmationView confirmationView = null;

    /**
     * ==========================================
     * =========================================== CUSTOM LISTENERS:
     * receiveKickoutRequest confirmKickoutRequest rejectKickoutRequest
     * receiveKickoutRequestRejection
     * ===========================================
     * ===========================================
     */

    /**
     * Constructor
     * 
     * @param mSpace
     *            - The space associated with this control
     */

    public KickoutController(Space space) {
        this.mSpace = space;
    } // end KickoutController constructor

    /**
     * If you are the owner, kick the user from the chat Otherwise, send out an
     * invitation request
     * 
     * @param kickMe
     *            - The user to be kicked
     * @param reason
     *            - The reason string associated with the kick
     * @throws XMPPException
     */
    public void kickoutUser(User kickMe, String reason) throws XMPPException {
        Occupant userOcc = mSpace.getAllOccupants().get(
                MainApplication.user_primary.getUsername());
        // DEBUG
        Log.v(TAG,
                "Is userOcc valid for "
                        + MainApplication.user_primary.getUsername()
                        + (userOcc != null));
        // if the primary user is the room's owner
        if (this.mSpace.getOwner().equals(MainApplication.user_primary)) {
            Log.v(TAG, "Kicking " + kickMe.getUsername());
            this.mSpace.getMUC().kickParticipant(kickMe.getNickname(), reason);
        } else {
            // message containing kickout request tag, the username of the
            // kicker,
            // the username of the kickee, and the reason
            Message msg = this.mSpace.getMUC().createMessage();
            msg.setBody(Network.REQUEST_KICKOUT + "@requester"
                    + MainApplication.user_primary.getUsername() + "@kickee"
                    + kickMe.getUsername() + "@reason"
                    + ((reason == null) ? Network.DEFAULT_KICKOUT : reason));
            msg.setType(Message.Type.groupchat);
            try {
                this.mSpace.getMUC().sendMessage(msg);
            } catch (XMPPException e) {
                if (D)
                    Log.d(TAG, "inviteUser - message not sent: "
                            + e.getXMPPError().getCode() + " - "
                            + e.getXMPPError().getMessage());
                e.printStackTrace();
            }
        }
    } // end kickOutUser method

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
     *         kick a user from a space in String array format {requesterJID,
     *         kickeeJID, kickReason}
     */
    public String[] receiveKickoutRequest(String kickoutRequest) {
        // Check the kickoutRequest is accurate
        if (kickoutRequest.contains(Network.REQUEST_KICKOUT)) {
            // extract kickout request info
            String kickoutRequestInfo = kickoutRequest
                    .split(Network.REQUEST_KICKOUT)[1];
            String requester = (kickoutRequestInfo.split("@requester")[1])
                    .split("@kickee")[0];
            String kickee = (kickoutRequestInfo.split("@requester" + requester
                    + "@kickee")[1]).split("@reason")[0];
            String reason = (kickoutRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_KICKOUT
                    : kickoutRequestInfo.split("@reason")[1]);
            String[] kickoutInfo = { requester, kickee, reason };

            // Make the kickout confirmation gui popup
            LayoutInflater inflater = (LayoutInflater) MainApplication.screen
                    .getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            confirmationView = new ConfirmationView(inflater, kickoutInfo,
                    mSpace);
            User userKickee = User.getAllUsers().get(kickee);
            User userRequester = User.getAllUsers().get(requester);
            // If you are getting kicked out
            if (userKickee == MainApplication.user_primary)
                confirmationView.setConfirmationInfo(userRequester, userKickee,
                        false);
            // If you are a moderator receiving a kickout request
            else
                confirmationView.setConfirmationInfo(userRequester, userKickee,
                        true);
            confirmationView.launch();

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
     * Confirm an kickout request. Kick out the user with the reason given by
     * the requester
     * 
     * @param kickoutInfo
     *            - String array: {requesterJID, kickeeJID, kickoutReason}
     */
    public void confirmKickoutRequest(String[] kickoutInfo)
            throws XMPPException {
        // Check the kickoutInfo is not null and length 3
        if (kickoutInfo != null && kickoutInfo.length == 3) {
            // kickout the user
            this.mSpace.getMUC()
                    .kickParticipant(kickoutInfo[1], kickoutInfo[2]);
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
            Message msg = this.mSpace.getMUC().createMessage();
            msg.setBody(Network.REJECT_KICKOUT + "@requester" + kickoutInfo[0]
                    + "@invitee" + kickoutInfo[1] + "@reason" + kickoutInfo[2]
                    + "@rejectreason"
                    + (reason == null ? Network.DEFAULT_REJECT : reason));
            msg.setType(Message.Type.groupchat);
            try {
                this.mSpace.getMUC().sendMessage(msg);
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
            Log.d(TAG, "rejectKickoutRequest - rejected kickout request from "
                    + kickoutInfo[0] + " for room " + mSpace.getRoomID()
                    + " for user " + kickoutInfo[1] + "(reason - "
                    + kickoutInfo[2] + ") : rejection reason - " + reason);
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
                String[] rejectInfo = { requester, kickee, reason, rejectReason };
                // TODO UI - send information around request reject to screen?
                // DEBUG
                if (D)
                    Log.d(TAG, "receiveKickoutRequestRejection - received "
                            + "rejection of kickout request " + " for room "
                            + mSpace.getRoomID() + " for user " + rejectInfo[1]
                            + "(reason - " + rejectInfo[2] + ": reason - "
                            + rejectInfo[3]);
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
