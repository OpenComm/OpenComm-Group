package edu.cornell.opencomm.model;



import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;



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

	/**
	 * Creates a InvitationController
	 * @param connection - the Connection that received the invitation.
	 * @param room - the room that invitation refers to.
	 * @param inviter - the inviter that sent the invitation. (e.g. crone1@shakespeare.lit).
	 * @param reason - the reason why the inviter sent the invitation.
	 * @param password - the password to use when joining the room.
	 * @param message - the message used by the inviter to send the invitation.
	 */
	public Invitation(Connection connection, String room, String inviter,
			String reason, String password, Message message) {
		this.connection = connection;
		this.room = room;
		this.inviter = inviter;
		this.reason = reason;
		this.password = password;
		this.message = message;
	}
	
	public Invitation(String[] inviteInfo, boolean isModeratorRequest){
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
	
	/** [String requester's JID, String invitee's JID, String reason] */
	public String[] getInviteInfo(){
		return inviteInfo;
	}
	
	public MultiUserChat getMUC(){
		return muc;
	}
	
	public boolean getIsModeratorRequest(){
		return isModeratorRequest;
	}
	
	//TODO: add setters if needed
	public void setInviteInfo(String[] inviteInfo){
		this.inviteInfo = inviteInfo;
	}
	
	public void setMUC(MultiUserChat newMUC){
		this.muc = newMUC;
	}
	
	public void setIsModeratorRequest(boolean isit){
		this.isModeratorRequest = isit;
	}
}
