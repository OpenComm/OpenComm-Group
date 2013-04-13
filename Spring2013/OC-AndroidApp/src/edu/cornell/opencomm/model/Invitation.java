package edu.cornell.opencomm.model;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;

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
	
	public Invitation(Connection conn, String room, String inviter, String reason, String password, Message message) {
		this.conn = conn;
		this.room = room;
		this.inviter = inviter;
		this.reason = reason;
		this.password = password;
		this.message = message;
	}
	
	public MultiUserChat accept() {
		ConferenceRoom muc = new ConferenceRoom(NetworkService.getInstance().getConnection(), room);
		try {
			muc.join();
		} catch (XMPPException e) {
			System.out.println("Could not join chatroom:" + e);
		}
		return muc;
	}
	
	public void decline() {
		MultiUserChat.decline(conn, room, inviter, declineMessage);
	}
	
}