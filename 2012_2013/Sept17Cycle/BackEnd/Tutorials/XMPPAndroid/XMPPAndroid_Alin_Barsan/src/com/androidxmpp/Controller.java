package com.androidxmpp;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class Controller {

	
	private final String DEFAULT_ROOM_NAME = "alinxmpptest";
	private boolean packetListenerBuilt = false;
	private XMPPConnection connection;
	private Context context;
	private String server;
	private String userName;
	private boolean chatRoomGenerated;
	private MultiUserChat groupChat;
	private PacketListenerController pcktListen;
	private PacketListener packetListener;
	private ArrayList<String> invitedUsers = new ArrayList<String>();
	
	public void setContext(Context context){
		this.context = context;
	}
	public void disconnect(){
		connection.disconnect();
	}
	
	public void establishConnection(String server){
		XMPPConnection connection = new XMPPConnection(server);
		try {
			connection.connect();
		}
		catch (Exception e){
			//do nothing
		}
		if(connection.isConnected()){
    		String text1 = "Connected to server: '" + server + "'";
    		Toast toast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
    		toast.show();
    		this.connection = connection;
    	}
    	else{
    		String text1 = "Failed to connect to server";
    		Toast toast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
    		toast.show();
    	}
	}
	
	public void reEstablishConnection(){
		XMPPConnection connection = new XMPPConnection(server);
		this.connection = connection;
		try {
			connection.connect();
		} catch (XMPPException e) {
			//do nothing, should work since this is just rebuilding initial successfull connection
		}
	}
	public void login(String userName, String password){
		this.userName = userName;
    	if(!connection.isConnected()){
    		String text = "Must connect to server before logging in";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
    		return;
    	}
    	else if (userName.equals("") || userName == null){
    		String text = "You must enter a username";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
    		return;
    	}
    	else if (password.equals("") || password == null){
    		String text = "You must enter a password";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
    		return;
    	}
		try {
			connection.login(userName, password);
			String text = "Successfully logged in";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
			this.createMultiUserChat();
		} 
		catch (XMPPException e) {
			String text = "Failed to log in...\n\nPlease wait...";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
    		this.reEstablishConnection();
		}

	}
	
	public void createMultiUserChat(){
		try{	
			groupChat = new MultiUserChat(connection,DEFAULT_ROOM_NAME  + "@conference.jabber.org");
			groupChat.create(DEFAULT_ROOM_NAME);
			//groupChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			chatRoomGenerated = true;
		}
		catch(Exception e){
	    	String text2 = "Failed to create a default chat room";
	    	Toast toast = Toast.makeText(context, text2, Toast.LENGTH_LONG);
	    	toast.show();
		}
		
	
	}
	

	
	public void invite(String userToInvite){
		if(!chatRoomGenerated){
			this.createMultiUserChat();
			groupChat.addInvitationRejectionListener(new InvitationRejectionListener() {
	          	public void invitationDeclined(String invitee, String reason) {
	          		System.out.println("User '" + invitee + "' declined your chat invitation.");
	          		System.out.println("Reason: " + reason);
	          	}
			});
			if(!packetListenerBuilt){
				pcktListen = new PacketListenerController();
				pcktListen.createPacketListener(context);
				pcktListen.addPacketListenerTo(groupChat);
				packetListenerBuilt = true;
			}
		}
		if(chatRoomGenerated){
			groupChat.invite(userToInvite, "User '" + userToInvite + "' has invited you to join a chat room");
			invitedUsers.add(userToInvite);
			String text = "Succeeded in sending an invitation to: " + userToInvite;
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void sendMessage(MultiUserChat groupChat, String message){
		try {
			groupChat.sendMessage(message);
		} catch (XMPPException e) {
			//do nothing, packetlistener will indicate whether message sent or not.
		}
	}
	public void printBuddyList(){
		Roster roster = connection.getRoster(); //gets other users on this connection
		Collection<RosterEntry> entries = roster.getEntries();
		ArrayList<RosterEntry> onlineUsers = new ArrayList<RosterEntry>();
		ArrayList<RosterEntry> offlineUsers = new ArrayList<RosterEntry>();
		for(RosterEntry entry: entries){
			if(entry.toString().contains("[Buddies]")){ //if other users are marked as buddies, print them to the list
				String user = entry.getUser();
				if(roster.getPresence(user).toString().contains("unavailable")){ //if user is offline, add them to offlineUsers
					offlineUsers.add(entry);
				}
				else{
					onlineUsers.add(entry);
				}
			}
		}
		String onlineInRoom = "";
		onlineInRoom+="Online Buddies in your chat room:\n";
		if(groupChat != null){
			if(invitedUsers.size() == 0){
				onlineInRoom+="There are 0 buddies in your chat room\n";
			}
			else{
				for(String user: invitedUsers){
					onlineInRoom+=user + "\n"; //warning: this code assumes that users actually accept your invitation (and are online)

				}
		}
		}
		String online = "";
		online+="Online Buddies:\n";
		if(onlineUsers.size() == 0){
			online+="There are 0 buddies online\n";
		}
		else{
			for(RosterEntry entry: onlineUsers){
				online += entry.toString().substring(0, entry.toString().indexOf("[Buddies]")) + "\n";
				
			}
		}
		String offline = "";
		offline += "Offline Buddies:\n";
		if(offlineUsers.size() == 0){
			offline+="There are 0 buddies offline\n";
		}
		else{
			for(RosterEntry entry: offlineUsers){
				offline+=entry.toString().substring(0, entry.toString().indexOf("[Buddies]")) + "\n";

			}
		}
		MainActivity.writeBuddyList(onlineInRoom, online, offline);
	}
	
	
	
}



