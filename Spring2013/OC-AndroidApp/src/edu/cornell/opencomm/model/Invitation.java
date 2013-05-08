package edu.cornell.opencomm.model;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class Invitation {

	public static String declineMessage = "I'm busy right now";

	public Connection conn;
	public String room;
	public String inviter;
	public String reason;
	public String password;
	public Message message;
	public boolean processed = false;

	public Invitation(Connection conn, String room, String inviter,
			String reason, String password, Message message) {
		this.conn = conn;
		this.room = room;
		this.inviter = inviter;
		this.reason = reason;
		this.password = password;
		this.message = message;
	}
	
	public String getInviterName() {
		return SearchService.getUser(inviter.split("@")[0]).getNickname();
	}
	
	public void process() {
		processed = true;
	}

	public void decline() {
		MultiUserChat.decline(conn, room, inviter, declineMessage);
	}

}