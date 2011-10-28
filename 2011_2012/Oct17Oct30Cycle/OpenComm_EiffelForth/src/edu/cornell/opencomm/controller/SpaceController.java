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
	
	/** Constructor: a new instance of SpaceController that controls a specific 
	 * space
	 * @param space - the space to be controlled */
	public SpaceController(Space space) {
		this.space = space;
		this.muc = this.space.getMUC();
		this.muc.addParticipantStatusListener(this.configParticipantStatusListener());
		this.muc.addInvitationRejectionListener(this.configInvitationRejectionListener());
		this.muc.addMessageListener(this.configMessageListener());
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
	
	/** END CUSTOM LISTENERS
	 * 
	 * =============================================================================================
	 * =============================================================================================
	 * =============================================================================================
	 * =============================================================================================
	 * 
	 * CONFIGURATION OF LISTENERS:
	 * 
	 * configInvitationRejectionListener
	 * configParticipantStatusListener
	 * configMessageListener
	 * 
	 * ===========================================
	 * ===========================================
	 */
	private InvitationRejectionListener configInvitationRejectionListener() {
		InvitationRejectionListener inviteRejectListener = new InvitationRejectionListener() {

			@Override
			public void invitationDeclined(String invitee, String reason) {
				// TODO UI - invitation declined
			}
			
		};
		return inviteRejectListener;
	} // end configInvitationRejectionListener method
	
	
	/** @return - A ParticipantStatusListener that listens for the change in status of participants 
	 * (does not include the primary user -- for primary user, use UserStatusListener)
	 * within a space.
	 * <p>TODO - alter the internal methods with any necessary UI changes to reflect
	 * the network changes that call the methods</p>
	 * Information on Roles and Affiliates:
	 * <ul>
	 * <li>
	 * <a href="http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/muc.html#role">Roles</a>
	 * </li>
	 * <li>
	 * <a href="http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/muc.html#afiliation">Affiliates</a>
	 * </li>
	 * </ul>
	 */
	private ParticipantStatusListener configParticipantStatusListener() {
		ParticipantStatusListener participantStatListener = new ParticipantStatusListener() {

			/** Called when administrator privilege is granted to a user in the room 
			 * @param userRoomInfo - the user receiving privilege<br>
			 * (ex: room_name@conference.jabber.org/nickname)
			 */
			public void adminGranted(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - admin privilege granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end adminGranted method

			/** Called when administrator privilege is revoked from a user in the room 
			 * @param userRoomInfo - the user whose privilege is revoked<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void adminRevoked(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] infoSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (infoSplit != null) {
						// extract the room's name
						String roomname = infoSplit[0];
						// extract the user's nickname
						String nickname = infoSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - admin privilege revoked from " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end adminRevoked method

			/** Called when a user bans another user from a room 
			 * @param bannedUserRoomInfo - the user who is banned from the room<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 * @param banningUser - the user who is banning the banned_user from the room<br>
			 * (ex: user@host.org)
			 * @param reason - the reason given by the banning user for banning the banned user from the room
			 */
			public void banned(String bannedUserRoomInfo, String banningUser, String reason) {
				// DEBUG
				if (D) {
					String[] bannedUserSplit = this.splitUserRoomInfo(bannedUserRoomInfo);
					// check that the split is successful
					if (bannedUserSplit != null) {
						// extract the room's name
						String bannedRoomname = bannedUserSplit[0];
						// extract the user's nickname
						String bannedNickname = bannedUserSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - " + bannedNickname + " banned by "
								+ banningUser + " from room " + bannedRoomname + "\ndue to: " + 
								 ((reason == null) ? Network.DEFAULT_BAN : reason));
					}
				}
			} // end banned method

			/** Called when a user joins a room, either by invite or on its own
			 * @param userRoomInfo - the user who joined the room<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void joined(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - " + nickname + " joined room "
								+ roomname);
					}
				}
			} // end joined method

			/** Called when a user kicks another user from a room 
			 * @param kickedUserRoomInfo - the user who is kicked from the room<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 * @param kickingUser - the user who is kicking the kicked_user from the room<br>
			 * (ex: user@host.org)
			 * @param reason - the reason given by the kickng user for kicking the kicked user from the room
			 */
			public void kicked(String kickedUserRoomInfo, String kickingUser, String reason) {
				// DEBUG
				if (D) {
					String[] kickedUserSplit = this.splitUserRoomInfo(kickedUserRoomInfo);
					// check that the split is successful
					if (kickedUserSplit != null) {
						// extract the room's name
						String kickedRoomname = kickedUserSplit[0];
						// extract the kicked user's nickname
						String kickedNickname = kickedUserSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - " + kickedNickname + " kicked by "
								+ kickingUser + " from room " + kickedRoomname + "\ndue to: " + 
								 ((reason == null) ? Network.DEFAULT_KICKOUT : reason));
					}
				}
			} // end kicked method

			/** Called when a user leaves the room on its own 
			 * @param userRoomInfo - the user leaving the room<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void left(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - " + nickname + " joined room "
								+ roomname);
					}
				}
			} // end left method

			/** Called when membership is granted to a user
			 * @param userRoomInfo - the user and the room in which it is receiving membership<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void membershipGranted(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - membership granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end membershipGranted method

			/** Called when membership is revoked from a user
			 * @param userRoomInfo - the user and the room in which its membership is revoked<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void membershipRevoked(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - membership revoked from " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end membershipRevoked method

			/** Called when moderator privilege is granted to a user
			 * @param userRoomInfo - the user and the room in which it is receiving membership<br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void moderatorGranted(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - moderator privileges granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end moderatorGranted method

			/** Called when moderator privilege is revoked from a user
			 * @param userRoomInfo - the user and the room in which it is revoking moderator <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void moderatorRevoked(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - moderator privileges revoked from " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end moderatorRevoked method

			/** Called when a user changes its nickname in the room
			 * @param userRoomInfo - the user and the room in which it is revoking moderator <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 * @param newNickname - the new nickname that the user is changing to
			 */
			public void nicknameChanged(String userRoomInfo, String newNickname) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - new nickname for " 
								+ nickname + " in room " + roomname + ": " + newNickname);
					}
				}
			} // end nicknameChanged method

			/** Called when ownership privilege is granted to a user
			 * @param userRoomInfo - the user and the room in which it is granting ownership <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void ownershipGranted(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - ownership privileges granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end ownershipGranted method

			/** Called when ownership privilege is revoked from a user
			 * @param userRoomInfo - the user and the room in which it is revoking ownership <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void ownershipRevoked(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - ownership privileges revoked from " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end ownershipRevoked method

			/** Called when voice is granted to a user
			 * @param userRoomInfo - the user and the room in which it is receiving voice <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void voiceGranted(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - voice granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end voiceGranted method

			/** Called when voice is granted to a user
			 * @param userRoomInfo - the user and the room in which it is revoking moderator <br>
			 * (ex: roomname@conference.jabber.org/nickname)
			 */
			public void voiceRevoked(String userRoomInfo) {
				// DEBUG
				if (D) {
					String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
					// check that the split is successful
					if (userSplit != null) {
						// extract the room's name
						String roomname = userSplit[0];
						// extract the user's nickname
						String nickname = userSplit[1];
						if (D) Log.d(TAG, "addUserStatusListener - voice granted to " 
								+ nickname + " in room " + roomname);
					}
				}
			} // end voiceRevoked method
			
			/** = String array of room name and nickname stored in String of format 
			 * "roomname@conference.jabber.org/nickname"
			 * @param info - string containing room name and nickname
			 * @return null if the split does not produce a String array with lengths 2<br>
			 * else a String array in which [0] contains room name, [1] contains nickname
			 */
			public String[] splitUserRoomInfo(String info) {
				String[] userRoomInfoSplit = info.split("@conference.jabber.org/");
				// check the result of split for an array with length 2
				if (userRoomInfoSplit == null || userRoomInfoSplit.length != 2) {
					Log.e(TAG, "addUserStatusListener/splitUserRoomInfo - " + 
							"given string does not contain sequence \"@conference.jabber.org\"");
					return null;
				}
				return userRoomInfoSplit;
			} // end splitUserRoomInfo method			
		};
		return participantStatListener;
	} // configParticipantStatusListener method
	
	/** = configuration of message listener */
	public PacketListener configMessageListener() {
		PacketListener messageListener = new PacketListener() {
			/** Process the messages coming in in packets */
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				String from = message.getFrom();
				String body = message.getBody();
				// if the primary user is the owner of the room
				Occupant userOcc = muc.getOccupant(MainApplication.user_primary.getUsername() 
						+ "/" + MainApplication.user_primary.getNickname());
				// if the primary user is the room's owner
				if (userOcc.getAffiliation().equals(Network.ROLE_OWNER)) {
					// if it is a invitation request:
					if (body.contains(Network.REQUEST_INVITE)) {
						if (D) Log.d(TAG, "configMessageListener - invitation request received");
						String[] inviteInfo = receiveInvitationRequest(body);
						// TODO - UI: some form of confirmation or rejection
						boolean userDecision = true;
						if (userDecision) {
							// invite user to the room
							confirmInvitationRequest(inviteInfo);
						}
						else {
							// Reject the invitation request
							// TODO - UI: permit user to type reason for rejection
							String reason = Network.DEFAULT_REJECT;
							rejectInvitationRequest(inviteInfo, reason);
						}
					}
					// if it is a kickout request:
					else if (body.contains(Network.REQUEST_KICKOUT)) {
						if (D) Log.d(TAG, "configMessageListener - kickout request received");
						String[] kickoutInfo = receiveKickoutRequest(body);
						// TODO - UI: some form of confirmation or rejection
						boolean userDecision = true;
						if (userDecision) {
							// kickout user from room
							confirmKickoutRequest(kickoutInfo);
						}
						else {
							// Reject the kickout request
							// TODO - UI: permit user to type reason for rejection
							String reason = Network.DEFAULT_REJECT;
							rejectKickoutRequest(kickoutInfo, reason);
						}
					}
					// normal messages
					else {
						if (D) Log.d(TAG, "Regular message received");
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
					// normal messages
					else {
						if (D) Log.d(TAG, "Regular message received");
					}
				}
				

				
			}
		};
		return messageListener;
		
	}
	
	/** END CONFIGURATION OF LISTENERS
	 * 
	 * =============================================================================================
	 * =============================================================================================
	 */
} // end Class SpaceController
