package com.example.testdemo;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
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
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.IBBProviders;
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

import android.content.Context;
import android.util.Log;

public class ChatHandler {

	private Context context;
	private XMPPConnection connection;
	private String serverName;
	private MultiUserChat chat;
	private String mainUserName;
	public static boolean inviteFailed = false;
	public static boolean kickoutFailed = false;
	public static boolean sendMessageFailed = false;
	
	public ChatHandler(Context context){
		this.context = context;
		configure();
	}
		
	public void configure(){
		configure(ProviderManager.getInstance());
		SmackConfiguration.setPacketReplyTimeout(10000);
	}
	
	public boolean connect(String server, int port){
		ConnectionConfiguration config = new ConnectionConfiguration(server,port);
		config.setSASLAuthenticationEnabled(true);
		connection = new XMPPConnection(config); //connects to server address provided
		try {
			connection.connect();
			serverName = server;
			return true;
		} catch (XMPPException e) {
			Log.e("PHAIL", "connection exception occurred at connection.connect()", e);
			return false;
		}
	}
	
	public boolean login(String userName, String password){
		try {
			connection.login(userName, password);
			mainUserName = userName;
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}
	
	public boolean createRoom(){
		chat = new MultiUserChat(connection, "OCtestRoom@" + "conference.cuopencomm");
		try {
			chat.create("OCtestRoom");
			chat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			return false;
		}
		PacketListener packetListener = new PacketListener() {
		public void processPacket(Packet arg0) {
			if(arg0 instanceof Message){
				Message message = (Message) arg0;
				String from = message.getFrom();
				String messageBody = message.getBody();
				Tester.handleMessage(from, messageBody);
			}
		}
		};
		chat.addMessageListener(packetListener);
		return true;
	}

	
	public void invite(String user){
		chat.invite(user, "Let's chat!");
	}
	
	public void kickout(String user){
		
	}
	
	public ArrayList<String> getParticipants(){
		return null;
	}
	
	public void send(String message){
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			sendMessageFailed = true;
		}
	}
	
	public ArrayList<String> getRoster(){
		return null;
	}
	
	public void logout(){
		connection.disconnect();
	}
	

		public void configure(ProviderManager pm) {
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
	
