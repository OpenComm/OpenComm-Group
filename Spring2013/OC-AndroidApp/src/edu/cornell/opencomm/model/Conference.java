package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.network.NetworkService;

import android.graphics.Point;
import android.os.Parcel;
import android.util.Log;

public class Conference implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User inviter;
	private HashMap<String, User> participants;
	MultiUserChat conferenceRoom;

	// CONSTRUCTORS

	public Conference(String roomName) {
		conferenceRoom = new MultiUserChat(NetworkService.getInstance()
				.getConnection(), roomName);
	}
	
	public Conference(MultiUserChat muc) {
		this.conferenceRoom = muc;
	}

	public MultiUserChat getMUC() {
		return this.conferenceRoom;
	}
	
	public HashMap<String,User> getParticipants() {
		return this.participants;
	}

	public ArrayList<User> updateLocations(Point center, int radius) {
		// TODO Auto-generated method stub
		return null;
	}

}
