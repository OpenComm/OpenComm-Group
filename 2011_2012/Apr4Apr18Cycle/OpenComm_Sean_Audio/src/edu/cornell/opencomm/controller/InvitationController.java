package edu.cornell.opencomm.controller;


import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import com.cornell.opencomm.jingleimpl.JingleIQPacket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.InvitationView;

/**
 * Controller class for MultiUserChat invitations
 * @author jonathanpullano, risanaka, kriskooi
 */
public class InvitationController {

    private InvitationView invitationView = null;

    private Space mSpace;

    private Invitation invitation;

    private static final String TAG = "InvitationController";
    private static final boolean D = Values.D;


    //TODO: Do these constructors need to talk?

    /**
     * Constructor
     * @param invitationView
     */
    public InvitationController(InvitationView invitationView) {
        if (D) Log.d(TAG, "InvitationController constructor called");
        this.invitationView = invitationView;
    }

    /**
     * Constructor
     * @param mSpace - The space associated with this controller
     */
    public InvitationController(Space mSpace) {
        this.mSpace = mSpace;
        this.mSpace.getMUC();
        this.mSpace.getMUC().addInvitationRejectionListener(new InvitationRejectionListener() {

            @Override
            public void invitationDeclined(String invitee, String reason) {
                if(D) Log.d(TAG, "invitationDeclined() called");
                //TODO: Trigger update to view (if any)
                Log.v(TAG, "invitationDeclined - Invitee: " + invitee +
                        " declined invitation because: " + reason);
            }
        });
    }

    /**
     * Called when the popup window is click
     * Dismisses the window
     */
    public void handlePopupWindowClicked() {
        //TODO: sanity check
        if (D) Log.d(TAG, "handlePopupWindowClicked() called");
        invitationView.getWindow().dismiss();
    } // end handlePopupWindowClicked

    /** Handle when the accept button is pressed
     * 1) If a moderator request - call confirm
     * 2) If an invite to you - then accept */
    public void handleAcceptButtonHover() {
        if (D) Log.d(TAG, "handleAcceptButtonHover() called");
        // Highlight the accept button and dismiss the window
        invitationView.getAcceptOverlay().setVisibility(View.VISIBLE);
        invitationView.getWindow().dismiss();
        // The after effects
        Invitation invite = invitationView.getInvitation();
        boolean isModeratorRequest = invite.getIsModeratorRequest();
        if(isModeratorRequest) {
            Log.v("InvitationController", "Moderator accepted the invite request");
            confirmInvitationRequest(invite.getInviteInfo());
        }
        else {
            Log.v("InvitationController", "You accepted the invite request");
         // if you are the only person in the mainspace, then replace your mainspace
            if(Space.getMainSpace().getAllParticipants().size() <= 1) {
                SpaceController.swapMainSpace(MainApplication.screen.getContext(),
                        invite.getMUC().getRoom());
             // Initiate Jingle session with all members of the chat room.
                Log.d(TAG, "Initiating Jingle session");
                Iterator<String> partItr = Space.getMainSpace().getAllParticipants().keySet().iterator();
    			if(Space.getMainSpace().getAllParticipants() == null || !partItr.hasNext()){
    				Log.i("InvitationController", "Space has no occupants!!");
    			}
    			else {
    				while(partItr.hasNext()){
    					String participant = partItr.next();
    					User partUser = Space.getMainSpace().getAllParticipants().get(participant);
    					if (!MainApplication.userPrimary.equals(partUser) && (Space.getMainSpace() != null && !Space.getMainSpace().getOwner().equals(MainApplication.userPrimary))) {
    						// Send session-initiate to the buddy.
    						partUser.getjCtrl().getJiqActionMessageSender().sendSessionInitiate(MainApplication.userPrimary.getUsername() + "@" + Network.DEFAULT_HOST + "/Smack", participant + "/Smack", partUser.getjCtrl());
    						partUser.getjCtrl().getSessionState().changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_INITIATE);
    						Log.d("InvitationController", "JingleController sent");
    					}
    				}												
    			}
            }
            // else create a new space
            else
                SpaceController.addExistingSpace(MainApplication.screen.getContext(), false,
                        invite.getRoom());
        }
    } //end handleAcceptButtonHover

    /** Handle when the cancel button is pressed */
    public void handleCancelButtonHover() {
        if (D) Log.d(TAG, "handleCancelButtonHover() called");
        // Dismisses the window for now
        invitationView.getCancelOverlay().setVisibility(View.VISIBLE);
        invitationView.getWindow().dismiss();
        // The after effects
        Invitation invite = invitationView.getInvitation();
        boolean isModeratorRequest = invite.getIsModeratorRequest();
        if(isModeratorRequest)
            rejectInvitationRequest(invite.getInviteInfo(),
                    Network.DEFAULT_REJECT_INVITATION_REQUEST);
        else
            // Decline the invite
            MultiUserChat.decline(invitation.getConnection(), invitation.getRoom(), invitation.getInviter(), null);
    } // end handleCancelButtonHover

    /** ============================================================================================
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
        //TODO: go through after fixing requestmessage
        if (D) Log.d(TAG, "inviteUser called");
        Occupant userOcc = mSpace.getAllOccupants().get(MainApplication.userPrimary.getUsername());
        //DEBUG
        Log.v(TAG, "Is userOcc valid for " + MainApplication.userPrimary.getUsername()
                + (userOcc != null));
        // if the primary user is the room's owner, send the invitation
        if (mSpace.getOwner().equals(MainApplication.userPrimary)) {
            Log.v("InvitationController", "Invited user as owner");
            this.mSpace.getMUC().invite(invitee.getUsername(), ((reason == null)
                    ? Network.DEFAULT_INVITE : reason));
        }
        // send message to owner invitation request
        else {
            Log.v("InvitationController", "Invited user as participant");
            // message containing invite request tag, the username of the inviter,
            // the username of the invitee, and the reason
            Message msg = this.mSpace.getMUC().createMessage();
            msg.setBody(Network.REQUEST_INVITE + "@inviter" +
                    MainApplication.userPrimary.getUsername() + "@invitee" +
                    invitee.getUsername() + "@reason" +
                    ((reason == null) ? Network.DEFAULT_INVITE : reason));
            msg.setType(Message.Type.groupchat);
            try {
                this.mSpace.getMUC().sendMessage(msg);
            } catch (XMPPException e) {
                if (D) Log.d(TAG, "inviteUser - message not sent: "
                        + e.getXMPPError().getCode() + " - " + e.getXMPPError().getMessage());
                e.printStackTrace();
            }
        }
        Log.v("InvitationController", "End of inviteUser method");
    } // end inviteUser method

    /**
     * = Information of invitation request received when a non-owner tries to
     * invite a user to a space in String array format {inviterJID,
     * inviteeJID, inviteReason}
     *
     * @param inviteRequest
     *            - message sent to the room when a non-owner tries to invite a
     *            user to a room. Contains the InviteRequest Tag<br>
     *            (format: (Network.REQUEST_INVITE)@(Inviter's JID)@invitee
     *            (Invitee's JID)@reason(Invitation reason)
     */
    public String[] receiveInvitationRequest(String inviteRequest) {
        if (D) Log.d(TAG, "receiveInvitationRequest() called");
        // Check the inviteRequest is accurate
        if (inviteRequest.contains(Network.REQUEST_INVITE)) {
            Log.v("InvitationController", inviteRequest);
            // extract invitation request info
            String inviteRequestInfo = inviteRequest
                    .split(Network.REQUEST_INVITE)[1];
            String inviter = (inviteRequestInfo.split("@inviter")[1])
                    .split("@invitee")[0];
            String invitee = (inviteRequestInfo.split("@inviter" + inviter
                    + "@invitee")[1]).split("@reason")[0];
            String reason = (inviteRequestInfo.split("@reason").length == 0 ? Network.DEFAULT_INVITE
                    : inviteRequestInfo.split("@reason")[1]);
            String[] inviteInfo = { inviter, invitee, reason };


            // For the invitation popup
            LayoutInflater inflater = (LayoutInflater) MainApplication.screen.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            invitationView = new InvitationView(inflater, new Invitation(inviteInfo, true), this);
            User userInvitee = User.getAllUsers().get(invitee);
            User userInviter = User.getAllUsers().get(inviter);
            invitationView.setInvitationInfo(userInviter, userInvitee, true);
            MainApplication.screen.getActivity().displayPopup(invitationView);

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
     * Confirm an invitation request. Invite the invitee with the reason given
     * by the requester
     *
     * @param inviteInfo
     *            - String array: {inviterJID, inviteeJID, inviteReason}
     */
    public void confirmInvitationRequest(String[] inviteInfo) {
        if (D) Log.d(TAG, "confirmInvitationRequest called()");
        // Check the inviteInfo is not null and length 3
        if (inviteInfo != null && inviteInfo.length == 3) {
            this.mSpace.getMUC().invite(inviteInfo[1], inviteInfo[2]);
            if (D) Log.d(TAG, "confirmInvitationRequest - confirmed invitation request from "
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
     *            - inviteInfo - String array: {inviterJID, inviteeJID,
     *            inviteReason}
     * @param reason
     *            - reason for the rejection
     */
    public void rejectInvitationRequest(String[] inviteInfo, String reason) {
        if (D) Log.d(TAG, "rejectInvitationRequest() called");
        //TODO: review post restructure
        // Check that the inviteInfo is valid
        if (inviteInfo != null && inviteInfo.length == 3) {
            // send the room the rejection notification
            Message msg = this.mSpace.getMUC().createMessage();
            msg.setBody(Network.REJECT_INVITE + "@inviter"
                    + inviteInfo[0] + "@invitee" + inviteInfo[1] + "@reason"
                    + inviteInfo[2] + "@rejectreason"
                    + (reason == null ? Network.DEFAULT_REJECT : reason));
            msg.setType(Message.Type.groupchat);
            try {
                this.mSpace.getMUC().sendMessage(msg);
            } catch (XMPPException e) {
                if (D) Log.d(TAG, "rejectInvitationRequest - message not sent: "
                        + e.getXMPPError().getCode() + " - "
                        + e.getXMPPError().getMessage());
                e.printStackTrace();
            }
        }
        // DEBUG
        if (D) Log.d(TAG, "rejectInvitationRequest - rejected invitation request from "
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
     *            (format: (Network.REJECTT_INVITE)@inviter(Inviter's JID)@invitee
     *            (Invitee's JID)@reason(Invitation reason)@rejectionreason(Rejection
     *            reason)
     *
     * @return Information of rejection of invitation request received from the
     * owner of the room in String array format {inviterJID,
     * inviteeJID, inviteReason, rejectionReason}
     */
    public String[] receiveInvitationRequestRejection(String requestReject) {
        if (D) Log.d(TAG, "receiveInvitationRequestRejection");
        // Check that the requestReject is valid
        if (requestReject.contains(Network.REJECT_INVITE)) {
            String requestRejectInfo = requestReject
                    .split(Network.REJECT_INVITE)[0];
            String inviter = (requestRejectInfo.split("@inviter")[0])
                    .split("@invitee")[0];
            // Check that the inviter is the primary user
            if (MainApplication.userPrimary.getUsername().contains(inviter)) {
                String invitee = (requestRejectInfo.split("@inviter"
                        + inviter + "@invitee")[0]).split("@reason")[0];
                String reason = (requestRejectInfo.split("@reason")[1])
                        .split("@rejectreason")[0];
                String rejectReason = requestRejectInfo.split("@rejectreason")[1];
                String[] rejectInfo = { inviter, invitee, reason,
                        rejectReason };
                // TODO UI - send information around request reject to screen?
                // DEBUG
                if (D) Log.d(TAG, "receiveInvitationRequestRejection - received "
                        + "rejection of invitation request " + " for room "
                        + this.mSpace.getRoomID() + " for user "
                        + rejectInfo[1] + "(reason - " + rejectInfo[2]
                                + ": reason - " + rejectInfo[3]);
                return rejectInfo;
            }
            if (D) Log.d(TAG, "receiveInvitationRequestRejection - primary user "
                    + "did not submit this request");
        } else {
            Log.e(TAG, "receiveInvitationRequestRejection - wrong tag");
        }
        return null;
    } // end receiveInvitationRequestRejection

    /**
     * Returns the most recent invitation received
     * @return InvitationController
     */
    public Invitation getInvitation() {
        return this.invitation;
    } // end getInvitation
}
