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

	private String title = "";

	private ArrayList<User> users = new ArrayList<User>();

	// CONSTRUCTORS

	public Conference(String roomName) {
		super(NetworkService.getInstance().getConnection(), roomName);
	}

	// API Functions

	public void addPresenceListener() {
		super.addParticipantStatusListener(new OCParticipantStatusListener(this));

	}

	@Override
	public void invite(String JID, String reason) {
		super.invite(JID, reason);
	}

	// Helper Functions

	// Getters and Setters

	public void setTitle(String newTitle){
		title = newTitle;
	}

	public String getTitle(){
		return title;
	}

	public ArrayList<User> getUsers(){
		return users;
	}

	public void addUser(User u){
		users.add(u);
	}

	public void removeUser(User u){
		users.remove(u);
	}

}
