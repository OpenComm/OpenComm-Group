package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.network.NetworkService;

import android.graphics.Point;

public class Conference implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
