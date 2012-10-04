package com.example.xmpp_demo_android;

import java.util.ArrayList;
import java.util.Collection;

import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

//import org.jivesoftware.smack.XMPPConnection;

/**
 * TODO: Fill in the methods below with whatever code you need them to do. Add
 * parameters to the function headers as needed. You code should be triggered
 * when you click on the UI elements, but its output should all go to logcat. If
 * you have any questions, please email Kris ASAP.
 * 
 * @author Kris
 * 
 */
public class Network {

	public static final String TAG = "Network";
	XMPPConnection connection = null;
	ArrayList<String> buddies = new ArrayList<String>();
	ArrayList<RosterEntry> onlineBuddies= new ArrayList<RosterEntry>();
	ArrayList<RosterEntry> offlineBuddies= new ArrayList<RosterEntry>();

	// Remember: getters and setters are expensive in Android. Use package
	// variables whenever possible. Don't understand what that means? Google
	// it, then ask Kris if it isn't clear.

	XMPPConnection conn;
	Roster buddyList;
	MultiUserChat chatRoom;
	Roster roster;

	public Network() {
		configure(ProviderManager.getInstance());
	}

	public void smackLogin() {
		connection = new XMPPConnection("jabber.org");
		try {
			connection.connect();
			connection.login("mucopencomm", "opencommmuc");

		} catch (XMPPException e) {
			// Log.v("Network", e.printStackTrace());
			// PRINT OUT ERROR HERE
			Log.v(TAG,
					"ERROR with connection and/or login in the smackLogin method");

		}

		if ((connection.isAuthenticated() && connection.isConnected()) == true) {
			chatRoom = null;
		} else {
			smackLogin();
		}
	}

	public void joinMUC() {
		boolean isCreated = false;

		if (!isCreated) {
			chatRoom = new MultiUserChat(connection,
					"leileileiBLOOP@conference.jabber.org");
			try {
				chatRoom.create("LEIXMPPANDROID");
				chatRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				isCreated = true;
			} catch (XMPPException e) {
				Log.v(TAG,
						"Problem in the joinMUC method with making a new room");

				// e.printStackTrace();
			}

			if (isCreated == true) {
				chatRoom.addMessageListener(new PacketListener() {
					public void processPacket(Packet packet) {

						// HERE IS WHERE YOU CAN RECEIVE MESSAGES!
						String XML = packet.toXML();
						int boundaryOne = XML.indexOf("<body>");
						int boundaryTwo = XML.indexOf("</body>");

						String message = XML.substring(boundaryOne + 6,
								boundaryTwo);

						Log.v(TAG, message);

					}
				});

				if (isCreated == false) {
					try {
						chatRoom.join("LEIXMPPANDROID");

					} catch (XMPPException e) {
						Log.v(TAG,
								"problem in the joinMUC method with joining the room");
						// e.printStackTrace();
					}

				}

			}

		}

	}

	public void inviteUser() {
		chatRoom.invite("risan@jabber.org", "enjoy as casual chat");
		buddies.add("risan@jabber.org");
		Log.v(TAG,
				"you have successfully invited a user AND added them to your buddy list!");

	}

	public void sendMessage() {
		String message = "What's Up bro!";
		Message sendThis = chatRoom.createMessage();
		sendThis.setBody(message);

		try {
			chatRoom.sendMessage(sendThis);
		} catch (XMPPException e) {
			Log.v(TAG, "message wasn't sent man ://");
			// e.printStackTrace();
		}

	}

	public void printBuddyList() {
		roster = connection.getRoster();
		Collection <RosterEntry> allEntries= roster.getEntries();
		
		for(RosterEntry userObject: allEntries){
			
			//offline
			boolean offline= roster.getPresence(userObject.getUser()).toString().contains("unavailabe");
			boolean online = roster.getPresence(userObject.getUser()).toString().contains("available");
			
			if(offline){
				offlineBuddies.add(userObject);
			}
			
			//online
			else if(online){
				onlineBuddies.add(userObject);
				
			
			}

		}
		
		print();//prints the buddy list now

	}
	
	public void print(){
		Log.v(TAG, "Offline:");
		
		for(int i=0; i<offlineBuddies.size(); i++){
			Log.v(TAG, offlineBuddies.get(i).toString());
			
		}
		
		Log.v(TAG, "Online:");
		
		for(int i=0; i<onlineBuddies.size(); i++){
			Log.v(TAG, onlineBuddies.get(i).toString());
			
		}		
	}

	

	public void quit() {
		connection.disconnect();
		Log.v(TAG, "you have disconnected");
	}

	/** A hack to get asmack to work with MUCs. DO NOT EDIT */
	public void configure(ProviderManager pm) {

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

	}
}
