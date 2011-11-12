package edu.cornell.opencomm.model;

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
	
	//TODO: add setters if needed
}
