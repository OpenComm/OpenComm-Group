package com.example.xmpp_demo_android;

import java.util.ArrayList;
import java.util.Collection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

import android.util.Log;

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

	private static XMPPConnection connection;
	private static MultiUserChat muc;

	// Remember: getters and setters are expensive in Android. Use package
	// variables whenever possible. Don't understand what that means? Google
	// it, then ask Kris if it isn't clear.
	XMPPConnection conn;
	Roster buddyList;
	MultiUserChat chatRoom;

	public Network() {
		configure(ProviderManager.getInstance());
	}

	public void smackLogin() {
		ConnectionConfiguration config = new ConnectionConfiguration(
				"jabber.org", 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);

		connection = new XMPPConnection(config);
		// Connect to the server
		// Log into the server
		try {
			connection.connect();
			if (connection.isConnected()) {
				connection.login("opencommss@jabber.org", "ssopencomm", null);
			}
		} catch (XMPPException e) {
			Log.w(TAG, e.getMessage());
		}
	}

	public void joinMUC() {
		muc = new MultiUserChat(connection, "fdafdsfda@conference.jabber.org");
		try {
			muc.create("user");
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			Log.w(TAG, e.getMessage());
			try {
				muc.join("user");
			} catch (XMPPException e1) {
				Log.w(TAG, e1.getMessage());
			}
		}
		/*
		 * NB: register the message listener with the MUC in this method Do not
		 * make a different class! Only global listeners need to go in a second
		 * class. This listener only applies to this MultiUserChat, so it gets
		 * registered in the same method where the MUC is created.
		 */
		muc.addMessageListener(new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				Log.println(0, TAG, "Packet received");

			}

		});
	}

	public void inviteUser() {
		if (muc.isJoined()) {
			muc.invite("designopencomm@jabber.org", "Let's talk");
		}
	}

	public void sendMessage() {
		try {
			muc.sendMessage("Hello!");

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.getMessage());
		}
	}

	public void printBuddyList() {
		ArrayList<String> offline = new ArrayList<String>();
		ArrayList<String> online = new ArrayList<String>();
		ArrayList<String> onlineAndInMUC = new ArrayList<String>();
		Roster roster = connection.getRoster();
		Collection<RosterEntry> buddies = roster.getEntries();
		for (RosterEntry buddy : buddies) {
			try {
				//System.out.println(muc.getParticipants().contains(buddy));
				if (muc.getParticipants().contains(buddy))
					onlineAndInMUC.add(buddy.getUser());
				else if (roster.getPresence(buddy.getUser()).isAvailable())
					online.add(buddy.getUser());
				else
					offline.add(buddy.getUser());
			} catch (XMPPException e) {
				Log.w(TAG, e.getMessage());
			}
		}
		Log.v(TAG, "Online and in MUC:");
		for (String b : onlineAndInMUC)
			Log.v(TAG," - " + b);
		System.out.println("Online:");
		for (String b : online)
			Log.v(TAG," - " + b);
		System.out.println("Offline:");
		for (String b : offline)
			Log.v(TAG," - " + b);
	}

	public void quit() {
		connection.disconnect();
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
