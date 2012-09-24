package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.UserView;

/** A ParticipantStatusListener that listens for the change in
 * status of participants (does not include the primary user -- for primary
 * user, use UserStatusListener) within a space.
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
public class ParticipantStatusController implements ParticipantStatusListener {

    Space mSpace;

    //Debugging
    public static final String TAG = "ParticipantStatusController";
    public static final boolean D = Values.D;

    /**
     * Constructor
     */
    public ParticipantStatusController(Space mSpace) {
        if (D) Log.d(TAG, "ParticipantStatusController constructor called");
        this.mSpace = mSpace;
        this.mSpace.getMUC().addParticipantStatusListener(this);
    }

    @Override
    /**
     * Called when administrator privilege is granted to a user in the room
     * @param userRoomInfo - the user receiving privilege<br>
     * (ex: room_name@conference.jabber.org/nickname)
     */
    public void adminGranted(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "adminGranted called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - admin privilege " +
                        "granted to " + nickname + " in room " + roomname);
            }
        }
    } // end adminGranted method

    @Override
    /**
     *  Called when administrator privilege is revoked from a user in the room
     * @param userRoomInfo - the user whose privilege is revoked<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void adminRevoked(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "adminRevoked called");
            String[] infoSplit = this.splitUserRoomInfo(userRoomInfo);
            if (infoSplit != null) {
                String roomname = infoSplit[0];
                String nickname = infoSplit[1];
                Log.d(TAG, "addUserStatusListener - admin privilege " +
                        "revoked from " + nickname + " in room " + roomname);
            }
        }
    } // end adminRevoked method

    @Override
    /**
     * Called when a user bans another user from a room
     * @param bannedUserRoomInfo - the user who is banned from the room<br>
     * (ex: roomname@conference.jabber.org/nickname)
     * @param banningUser - the user who is banning the banned_user
     * from the room<br>
     * (ex: user@host.org)
     * @param reason - the reason given by the banning user for banning
     * the banned user from the room
     */
    public void banned(String bannedUserRoomInfo, String banningUser, String reason) {
        if (D) {
            Log.d(TAG, "banned called");
            String[] bannedUserSplit = this.splitUserRoomInfo(bannedUserRoomInfo);
            if (bannedUserSplit != null) {
                String bannedRoomname = bannedUserSplit[0];
                String bannedNickname = bannedUserSplit[1];
                Log.d(TAG, "addUserStatusListener - " + bannedNickname +
                        " banned by " + banningUser + " from room " +
                        bannedRoomname + "\ndue to: " +
                        ((reason == null) ? Network.DEFAULT_BAN : reason));
            }
        }
        String[] userSplit = this.splitUserRoomInfo(bannedUserRoomInfo);
        if(userSplit != null){
            User u = User.getAllNicknames().get(userSplit[1]);
            Space s = Space.getAllSpaces().get(userSplit[0]);
            if (u != null && s != null){
                s.getAllParticipants().remove(u.getUsername());
                s.getAllNicknames().remove(u.getNickname());
            }
        }
    } // end banned method

    @Override
    /**
     *  Called when a user joins a room, either by invite or on its own
     * @param userRoomInfo - the user who joined the room<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void joined(String userRoomInfo) {
        if (D) Log.d(TAG, "joined called");
        String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
        if(userSplit !=null){
            User user = User.nicknameToUser.get(userSplit[1]);
            if(user == null) {
                user = new User(userSplit[1], userSplit[1], R.drawable.question);
            }
            mSpace.getSpaceController().addUser(userRoomInfo, user);
        }

        if (D) {
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                if (D) Log.d(TAG, "addUserStatusListener - " + nickname
                        + " joined room " + roomname);
            }
        }
        if(userSplit != null){
            User u = User.getAllNicknames().get(userSplit[1]);
            Space s = Space.getAllSpaces().get(userSplit[0]);
            if (u != null && s != null){
                s.getAllParticipants().put(u.getUsername(), u);
                s.getAllNicknames().put(u.getNickname(), u);
                Log.v(TAG, s.getMUC().getOccupant(s.getRoomID() + "/" + u.getNickname()).getAffiliation());
                try {
                    s.getMUC().grantMembership(u.getUsername());
                } catch (XMPPException e) {
                    Log.v(TAG, "Failed to grant membership to " + u.getUsername());
                }
                Log.v(TAG, s.getMUC().getOccupant(s.getRoomID() + "/" + u.getNickname()).getAffiliation());
            }
        }

    } // end joined method

    @Override
    /**
     * Called when a user kicks another user from a room
     * @param kickedUserRoomInfo - the user who is kicked from the room<br>
     * (ex: roomname@conference.jabber.org/nickname)
     * @param kickingUser - the user who is kicking the kicked_user from the room<br>
     * (ex: user@host.org)
     * @param reason - the reason given by the kicking user for kicking
     * the kicked user from the room
     */
    public void kicked(String kickedUserRoomInfo, String kickingUser, String reason) {
        if (D) Log.d(TAG, "kicked called");
        String[] kickedUserSplit = this.splitUserRoomInfo(kickedUserRoomInfo);
        if(kickedUserSplit != null){
            User user = User.nicknameToUser.get(kickedUserSplit[1]);
            mSpace.getSpaceController().deleteUser(kickedUserRoomInfo, user);
        }
        if (D) {
            if (kickedUserSplit != null) {
                String kickedRoomname = kickedUserSplit[0];
                String kickedNickname = kickedUserSplit[1];
                Log.d(TAG, "addUserStatusListener - " + kickedNickname +
                    " kicked by " + kickingUser + " from room " +
                    kickedRoomname + "\ndue to: " +
                    ((reason == null) ? Network.DEFAULT_KICKOUT : reason));
            }
        }
        String[] userSplit = this.splitUserRoomInfo(kickedUserRoomInfo);
        if(userSplit != null){
            User u = User.getAllNicknames().get(userSplit[1]);
            Space s = Space.getAllSpaces().get(userSplit[0]);
            if (u != null && s != null){
                s.getAllParticipants().remove(u.getUsername());
                s.getAllNicknames().remove(u.getNickname());
                MainApplication.screen.getActivity().invalidateSpaceView();
            }
        }
    } // end kicked method

    @Override
    /**
     *  Called when a user leaves the room on its own
     * @param userRoomInfo - the user leaving the room<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void left(String userRoomInfo) {
        if (D) Log.d(TAG, "left called");

        String[] userRoomSplit = this.splitUserRoomInfo(userRoomInfo);
        boolean disconnected = false;
        if(userRoomSplit != null) {
            User user = User.nicknameToUser.get(userRoomSplit[1]);
            Roster roster = LoginController.xmppService.getXMPPConnection().getRoster();
            Presence p = roster.getPresence(user.getUsername());
            if(p != null && (
                    p.getType() == Presence.Type.unavailable ||
                    p.getType() == Presence.Type.error)) {//User intentionally d/c
                if(D) Log.d(TAG, "User dc");
                for(UserView view : MainApplication.screen.getAllIcons()) {
                    if(view.getPerson().getUsername() == user.getUsername()) {
                        view.setDisconnected(true);
                        view.invalidate();
                        MainApplication m = (MainApplication)view.getContext();
                        m.invalidateSpaceView();
                    }
                }
                disconnected = true;
            } else {
                mSpace.getSpaceController().deleteUser(userRoomInfo, user);
                if(D) Log.d(TAG, "User left");
            }
        }
        if (D) {
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - " + nickname +
                        " left room " + roomname);
            }
        }
        String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
        if(userSplit != null && !disconnected){
            User u = User.getAllNicknames().get(userSplit[1]);
            Space s = Space.getAllSpaces().get(userSplit[0]);
            if (u != null && s != null) {
                s.getAllParticipants().remove(u.getUsername());
                s.getAllNicknames().remove(u.getNickname());
            }
        }
    } // end left method

    @Override
    /**
     *  Called when membership is granted to a user
     * @param userRoomInfo - the user and the room in which it is
     * receiving membership<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void membershipGranted(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "membershipGranted called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - membership granted to "
                        + nickname + " in room " + roomname);
            }
        }
    } // end membershipGranted method

    @Override
    /**
     * Called when membership is revoked from a user
     * @param userRoomInfo - the user and the room in which its
     * membership is revoked<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void membershipRevoked(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "membershipRevoked called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - membership " +
                        "revoked from " + nickname + " in room " + roomname);
            }
        }
    } // end membershipRevoked method

    @Override
    /**
     *  Called when moderator privilege is granted to a user
     * @param userRoomInfo - the user and the room in which it is
     * receiving membership<br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void moderatorGranted(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "moderatorGranted called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - moderator " +
                   "privileges granted to " + nickname + " in room " +
                    roomname);
            }
        }
    } // end moderatorGranted method

    @Override
    /**
     * Called when moderator privilege is revoked from a user
     * @param userRoomInfo - the user and the room in which it is revoking moderator <br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void moderatorRevoked(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "moderatorRevoked called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - moderator " +
                       "privileges revoked from " + nickname + " in room " +
                        roomname);
            }
        }
    } // end moderatorRevoked method

    @Override
    /**
     * Called when a user changes its nickname in the room
     * @param userRoomInfo - the user and the room in which it is revoking moderator <br>
     * (ex: roomname@conference.jabber.org/nickname)
     * @param newNickname - the new nickname that the user is changing to
     */
    public void nicknameChanged(String userRoomInfo, String newNickname) {
        if (D) {
            Log.d(TAG, "nicknameChanged called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - new nickname for "
                        + nickname + " in room " + roomname + ": " + newNickname);
            }
        }
    } // end nicknameChanged method

    @Override
    /**
     * Called when ownership privilege is granted to a user
     * @param userRoomInfo - the user and the room in which it is granting ownership <br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void ownershipGranted(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "ownershipGranted called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - ownership " +
                        "privileges granted to " + nickname + " in room " +
                        roomname);
            }
        }
    } // end ownershipGranted method

    @Override
    /**
     * Called when ownership privilege is revoked from a user
     * @param userRoomInfo - the user and the room in which it is
     * revoking ownership <br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void ownershipRevoked(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "ownershipRevoked called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - ownership " +
                        "privileges revoked from " + nickname + " in room " +
                        roomname);
            }
        }
    } // end ownershipRevoked method

    @Override
    /**
     * Called when voice is granted to a user
     * @param userRoomInfo - the user and the room in which it is receiving voice <br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void voiceGranted(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "voiceGranted called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - voice granted to "
                        + nickname + " in room " + roomname);
            }
        }
    } // end voiceGranted method

    @Override
    /**
     *  Called when voice is granted to a user
     * @param userRoomInfo - the user and the room in which it is
     * revoking moderator <br>
     * (ex: roomname@conference.jabber.org/nickname)
     */
    public void voiceRevoked(String userRoomInfo) {
        if (D) {
            Log.d(TAG, "voiceRevoked called");
            String[] userSplit = this.splitUserRoomInfo(userRoomInfo);
            if (userSplit != null) {
                String roomname = userSplit[0];
                String nickname = userSplit[1];
                Log.d(TAG, "addUserStatusListener - voice granted to "
                        + nickname + " in room " + roomname);
            }
        }
    } // end voiceRevoked method

    /**
     *  = String array of room name and nickname stored in String of format
     * "roomname@conference.jabber.org/nickname"
     * @param info - string containing room name and nickname
     * @return null if the split does not produce a String array with lengths 2<br>
     * else a String array in which [0] contains room name, [1] contains nickname
     */
    public String[] splitUserRoomInfo(String info) {
        if (D) Log.d(TAG, "splitUserRoomInfo called");
        String[] userRoomInfoSplit = info.split("@" + Network.DEFAULT_CONFERENCE + "/");
        // check the result of split for an array with length 2
        if (userRoomInfoSplit == null || userRoomInfoSplit.length != 2) {
            Log.e(TAG, "addUserStatusListener/splitUserRoomInfo - " +
                    "given string does not contain sequence \"@" + Network.DEFAULT_CONFERENCE + "\"");
            return null;
        }
        userRoomInfoSplit[0] += "@" + Network.DEFAULT_CONFERENCE;
        return userRoomInfoSplit;
    } // end splitUserRoomInfo method

}
