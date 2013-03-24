package com.example.view;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.util.Log;

public class ChatHandler {

	private XMPPConnection connection;
	private static final boolean D = true;
	// set boolean to false to disable debugging log statements
	private static final String TAG = "ChatHandler";
	private MultiUserChat chat;

	public ChatHandler() {
		configure();
		// configures the provider managers upon instantiation
	}

	
	
	public void configure() {
		configure(ProviderManager.getInstance());
		SmackConfiguration.setPacketReplyTimeout(10000);
	}
	
	
	public boolean connect(String server, int port) {
		ConnectionConfiguration config = new ConnectionConfiguration(server,
				port);
		config.setSASLAuthenticationEnabled(true);
		connection = new XMPPConnection(config);
		// configures the connection and establishes it. Returns false if failed
		try {
			connection.connect();
			if (D)
				Log.v(TAG, "connection successful");
			return true;
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG,
						"connection exception occurred at connection.connect()");
			return false;
		}
	}

	public boolean login(String userName, String password) {
		try {
			connection.login(userName, password);
			// logs in with username and password, returns false if failed
			if (D)
				Log.v(TAG, "login successful");
			return true;
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG, "login failed");
			return false;
		}
	}

	public boolean createRoom() {
		chat = new MultiUserChat(connection, "OCtestRoom@"
				+ "conference.cuopencomm");
		// instantiates the MUC
		try {
			chat.join("OCtest");
			// creates chat room by attempting to join a nonexistant one, makes
			// a default room. Configures it to default
			chat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			if (D)
				Log.v(TAG, "MUC successfully created");
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG, "failed to create MUC");
			return false;
		}
		PacketListener packetListener = new PacketListener() {
			// function for processing the packets. Assumes that they are all
			// messages since it is added as a message listener
			public void processPacket(Packet arg0) {
				Message message = (Message) arg0;
				String from = message.getFrom();
				String messageBody = message.getBody();
				from = from.replace("octestroom@conference.cuopencomm/", "");
				// retrieves and configures the information from the message
				String fullMessage = from + ":  " + messageBody;
				synchronized(ChatRoom.messages) {
				ChatRoom.messages.add(fullMessage);
				}
			}
		};
		chat.addMessageListener(packetListener);
		// adds the message listener
		return true;
	}

	public void invite(String user) {
		chat.invite(user, "let's chat");
		// sends a simple invitation, does not listen for rejections/acceptance.
		// Note that this function uses the form "oc2testorg@cuopencomm"
		if (D)
			Log.v(TAG, "invitation sent to user: " + user);
	}

	public void kickout(String user) {
		try {
			chat.kickParticipant(user, "I dont need a reason");
			// kicks the specified user. Note that this function uses the form
			// "oc2testorg" unlike the invite function
			if (D)
				Log.v(TAG, "kickout function called");
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG, "failed to kick participant");
		}
	}

	public ArrayList<String> getParticipants() {
		Collection<Occupant> occupants = null;
		try {
			occupants = chat.getParticipants();
			//retreives participants
			if (D)
				Log.v(TAG, "Retreived MUC participants");
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG, "Failed to retrieve participants of the MUC");
			return null;
		}
		if (D)
			Log.v(TAG, "Participant List:");
		ArrayList<String> participants = new ArrayList<String>();
		for (Occupant occupant : occupants) {
			//retrieves and stores the user info as a string
			String part = occupant.getJid();
			int cutoff = part.indexOf("/");
			part = part.substring(0, cutoff);
			participants.add(part);
			if (D)
				Log.v(TAG, part);
		}

		return participants;
	}

	public void send(String message) {
		try {
			chat.sendMessage(message);
			//sends a message to the chat, messagelistener will catch this
		} catch (XMPPException e) {
			if (D)
				Log.v(TAG, "failed to send message");
		}
	}

	public ArrayList<String> getRoster() {
		ArrayList<String> buddies = new ArrayList<String>();
		Collection<RosterEntry> entries = connection.getRoster().getEntries();
		//gets buddies as RosterEntries
		if (D)
			Log.v(TAG, "Buddylist:");
		for (RosterEntry entry : entries) {
			buddies.add(entry.getUser());
			//stores the entries as strings representing user names
			if (D)
				Log.v(TAG, entry.getUser());
		}

		return buddies;
	}

	public void logout() {
		connection.disconnect();
		//severs the connection
		if (D)
			Log.v(TAG, "disconnected");
	}

	public void configure(ProviderManager pm) {
		//configuration method.
		if (D)
			Log.v(TAG, "configuring provider managers...");
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

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

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		// [TODO] : Ask Kris
		// pm.addIQProvider("query","http://jabber.org/protocol/bytestreams",
		// new BytestreamsProvider());
		// pm.addIQProvider("open","http://jabber.org/protocol/ibb", new
		// IBBProviders.Open());
		// pm.addIQProvider("close","http://jabber.org/protocol/ibb", new
		// IBBProviders.Close());
		// pm.addExtensionProvider("data","http://jabber.org/protocol/ibb", new
		// IBBProviders.Data());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
}
