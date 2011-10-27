package edu.cornell.opencomm.controller;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import android.util.Log;

import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;

/** An instance of this class is a space controller for each space (main space 
 * or private space), such as adding/deleting people from a space.
 * @author OpenComm (cuopencomm@gmail.com), Risa Naka (rn96@cornell.edu)
 *
 */
public class SpaceController {
	// Debugging
	private static final String TAG = "SpaceController";
	private static final boolean D = true;
	
	// Model objects
	private Space space; // the space that is controlled
	
	/** Constructor: a new instance of SpaceController that controls a specific 
	 * space
	 * @param space - the space to be controlled */
	public SpaceController(Space space) {
		this.space = space;
		this.space.getMUC().addParticipantStatusListener(this.configUserStatusListener());
	} // end SpaceController method
	
	/** @return - A ParticipantStatusListener that listens for the change in status of users
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
	private ParticipantStatusListener configUserStatusListener() {
		ParticipantStatusListener userStatListener = new ParticipantStatusListener() {

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
		return userStatListener;
	} // configUserStatusListener method
} // end Class SpaceController
