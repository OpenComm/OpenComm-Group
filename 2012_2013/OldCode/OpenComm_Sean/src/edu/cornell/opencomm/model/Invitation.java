package edu.cornell.opencomm.model;



import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;
import edu.cornell.opencomm.Values;



/**
 * Immutable Model representation of an invitation
 * @author jonathan
 */
public class Invitation {
    private Connection connection;
    private String room;
    private String inviter;
    private String reason;
    private String password;
    private Message message;
    private String[] inviteInfo;
    private MultiUserChat muc;
    private boolean isModeratorRequest;

    private final static String TAG = "Invitation";
    private static boolean D = Values.D;

    /**
     * Constructor
     * @param connection - the Connection that received the invitation.
     * @param room - the room that invitation refers to.
     * @param inviter - the inviter that sent the invitation. (e.g. crone1@shakespeare.lit).
     * @param reason - the reason why the inviter sent the invitation.
     * @param password - the password to use when joining the room.
     * @param message - the message used by the inviter to send the invitation.
     */
    public Invitation(Connection connection, String room, String inviter,
            String reason, String password, Message message, MultiUserChat muc) {
        if (D) Log.d(TAG, "Invitation constructor called");
        this.connection = connection;
        this.room = room;
        this.inviter = inviter;
        this.reason = reason;
        this.password = password;
        this.message = message;
        this.muc = muc;
    }

    /**
     * Constructor
     * @param inviteInfo
     * @param isModeratorRequest
     */
    public Invitation(String[] inviteInfo, boolean isModeratorRequest) {
        if (D) Log.d(TAG, "Invitation constructor called");
        this.inviteInfo = inviteInfo;
        this.isModeratorRequest = isModeratorRequest;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @return the inviter
     */
    public String getInviter() {
        return inviter;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * [String requester's JID, String invitee's JID, String reason]
     * @return inviteInfo, in the above format
     */
    public String[] getInviteInfo() {
        return inviteInfo;
    }

    /**
     * @return the muc
     */
    public MultiUserChat getMUC() {
        return muc;
    }

    /**
     * @return isModeratorRequest
     */
    public boolean getIsModeratorRequest() {
        return isModeratorRequest;
    }

    /**
     * ModeratorRequest setter method
     */
    public void setIsModeratorRequest(boolean moderatorRequest){
        this.isModeratorRequest = moderatorRequest;
    }
}
