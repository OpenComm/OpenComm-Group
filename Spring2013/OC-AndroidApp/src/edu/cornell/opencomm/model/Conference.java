package edu.cornell.opencomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;

import edu.cornell.opencomm.controller.OCParticipantStatusListener;
import edu.cornell.opencomm.network.NetworkService;

public class Conference implements Serializable {
	/**
	 * 
	 */
	String TAG = "CONFERENCE";
	
	private static final long serialVersionUID = 1L;

	private String title = "";

	private ArrayList<User> users = new ArrayList<User>();
	
	private MultiUserChat chat;

	// CONSTRUCTORS

	public Conference(String roomName) {
		chat = new MultiUserChat(NetworkService.getInstance().getConnection(), roomName);
		//super(NetworkService.getInstance().getConnection(), roomName);
	}

	// API Functions

	public void addPresenceListener() {
		chat.addParticipantStatusListener(new OCParticipantStatusListener(this));
		//super.addParticipantStatusListener(new OCParticipantStatusListener(this));

	}
	
	public void join(String jid){
		try {
			chat.join(jid);
			Log.v(TAG, "primary user successfully joined room");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	public void sendConfigurationForm(Form f){
		try {
			chat.sendConfigurationForm(f);
			Log.v(TAG, "room config form successfully sent");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public void invite(String JID, String reason) {
		chat.invite(JID,  reason);
		//super.invite(JID, reason);
	}
	
	public void leave(){
		chat.leave();
	}
	
	public Collection<Occupant> getParticipants(){
		try {
			return chat.getParticipants();
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
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
