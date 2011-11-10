package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;

/** An instance of this class controls the messages sent to/from participants
 * within a space.
 */
public class MessageController {
	// Debugging
	private static final String TAG = "Controller.MessageController";
	private static final boolean D = true;

	// Model variables
	private Space mSpace;

	// Network variable
	private MultiUserChat muc;

	/** Constructor: a new message controller for a specified space
	 * @param mSpace - the space within which the messages are sent and received
	 */
	public MessageController(Space mSpace) {
		this.mSpace = mSpace;
		this.muc = this.mSpace.getMUC();
		this.muc.addMessageListener(this.configMessageListener());
	}

	/** = configuration of message listener */
	public PacketListener configMessageListener() {
		PacketListener messageListener = new PacketListener() {
			/** Process the messages coming in in packets */
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				String from = message.getFrom();
				String body = message.getBody();
				Occupant userOcc = muc.getOccupant(mSpace.getRoomID()
						+ "/" + MainApplication.user_primary.getNickname());
				// if the primary user is the room's owner
				if (userOcc.getAffiliation().equals(Network.ROLE_OWNER)) {
					// if it is a invitation request:
					if (body.contains(Network.REQUEST_INVITE)) {
						if (D) Log.d(TAG, "configMessageListener - invitation request received");
						String[] inviteInfo = mSpace.getInvitationController().receiveInvitationRequest(body);
						// TODO - UI: some form of confirmation or rejection
						boolean userDecision = true;
						if (userDecision) {
							// invite user to the room
							mSpace.getInvitationController().confirmInvitationRequest(inviteInfo);
						}
						else {
							// Reject the invitation request
							// TODO - UI: permit user to type reason for rejection
							String reason = Network.DEFAULT_REJECT;
							mSpace.getInvitationController().rejectInvitationRequest(inviteInfo, reason);
						}
					}
					// if it is a kickout request:
					else if (body.contains(Network.REQUEST_KICKOUT)) {
						if (D) Log.d(TAG, "configMessageListener - kickout request received");
						String[] kickoutInfo = mSpace.getKickoutController().receiveKickoutRequest(body);
						// TODO - UI: some form of confirmation or rejection
						boolean userDecision = true;
						if (userDecision) {
							// kickout user from room
							mSpace.getKickoutController().confirmKickoutRequest(kickoutInfo);
						}
						else {
							// Reject the kickout request
							// TODO - UI: permit user to type reason for rejection
							String reason = Network.DEFAULT_REJECT;
							mSpace.getKickoutController().rejectKickoutRequest(kickoutInfo, reason);
						}
					}
					
					// normal messages
					else {
						if (D) Log.d(TAG, "Regular message receive from " + from
								+ ": " + body);
					}

				}
				// if the primary user is not the owner of the room
				else {
					// listen for rejection of invitation request
					if (body.contains(Network.REJECT_INVITE)) {

					}
					// listen for rejection of invitation request
					else if (body.contains(Network.REJECT_INVITE)) {

					}
					// listen for delete requests
					else if (body.contains(Network.REQUEST_DELETE)) {
						if (D) Log.d(TAG, "configMessageListener - delete request received");
						String requester = (body.split("@requester")[0]).split("@deletee")[0];
						if(mSpace.getOwner().getUsername().equals(requester)) {

						}
					}
					
					// normal messages
					else {
						if (D) Log.d(TAG, "Regular message receive from " + from
								+ ": " + body);
					}
				}
			}
		};
		return messageListener;

	} // end configMessageListener method
} // end Class MessageController
