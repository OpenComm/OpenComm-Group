package com.androidxmpp;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

import android.content.Context;
import android.widget.Toast;

public class Controller {

	
	private final String DEFAULT_ROOM_NAME = "alinxmpptest";
	private boolean packetListenerBuilt = false;
	private XMPPConnection connection;
	private Context context;
	private String server;
	private String userName;
	private boolean chatRoomGenerated;
	private MultiUserChat groupChat;
	private ArrayList<String> invitedUsers = new ArrayList<String>();
	
	public Controller(Context context){
		configure(ProviderManager.getInstance());
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
    	else{try {
			connection.login(userName, password);
			String text = "Successfully logged in";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
		} 
		catch (Exception e) {
			String text = "Failed to log in...";
    		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    		toast.show();
		}
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
				PacketListener packetListener = new PacketListener() {
					public void processPacket(Packet arg0) {
						String message = arg0.toString();
						String text = "A message has been posted on the chat room:\n\n" + message;    			
						Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		    			toast.show();							
					}
				    };
				groupChat.addMessageListener(packetListener);
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
	
	public void sendMessage(String message){
		try {
			groupChat.sendMessage(message);
		} catch (XMPPException e) {
			//do nothing, packetlistener will indicate whether message sent or not.
		}
	}
	
	/*
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
	}
	*/
	
	public void configure(ProviderManager pm){
		//Roster Exchange
		pm.addExtensionProvider("x","jabber:x:roster", new RosterExchangeProvider());
		//Message Events
		pm.addExtensionProvider("x","jabber:x:event", new MessageEventProvider());
		//Group Chat Invitations
		pm.addExtensionProvider("x","jabber:x:conference", new GroupChatInvitation.Provider());
		//Data Forms
		pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());
		//MUC User
		pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user",  new MUCUserProvider());
		//MUC Admin
		pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
		//MUC Owner
		pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
	}
}



