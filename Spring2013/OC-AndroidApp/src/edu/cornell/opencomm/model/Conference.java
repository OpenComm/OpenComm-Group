package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.controller.OCParticipantStatusListener;
import edu.cornell.opencomm.network.NetworkService;
import android.graphics.Point;

public class Conference extends MultiUserChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// CONSTRUCTORS

	public Conference(String roomName) {
		super(NetworkService.getInstance().getConnection(), roomName);
	}

	// API Functions

	public void addPresenceListener() {
		super.addParticipantStatusListener(new OCParticipantStatusListener(this));

	}
	
	public void invite(String JID, String reason) {
		super.invite(JID, reason);
	}

	// Helper Functions

	// Getters and Setters

	public ArrayList<User> updateLocations(Point center, int radius) {
		// TODO Auto-generated method stub
		return null;
	}
}
