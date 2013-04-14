package edu.cornell.opencomm.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.PrivacyList;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
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

import com.cornell.opencomm.jingleimpl.JingleIQPacket;
import com.cornell.opencomm.jingleimpl.ReasonElementType;
import com.cornell.opencomm.jingleimpl.sessionmgmt.JingleIQBuddyPacketRouter;

import edu.cornell.opencomm.audio.JingleController;
import edu.cornell.opencomm.controller.LoginController.ReturnState;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.InvitationsList;

import android.util.Log;

/**
 * Service that handles the network connection
 * 
 * @author Ankit Singh [frontend], Risa Naka [frontend], Kris Kooi [backend],
 *         Brian O'Connor [backend]
 * 
 */
public class NetworkService {

	public static final boolean D = true;
	private static final String TAG = "Network.NetworkService";

	public static final String DEFAULT_HOST = "cuopencomm.no-ip.org";
	public static final String DEFAULT_HOSTNAME = "@cuopencomm";
	public static final int DEFAULT_PORT = 5222;
	public static final String DEFAULT_RESOURCE = "OpenComm";

	private static NetworkService _instance = null;

	// XMPP connection
	private XMPPConnection xmppConn;
	private ConnectionConfiguration xmppConfig;
	private PrivacyList blockList;
	private AccountManager accountManager;

	public static String generateRoomID(){
		Random rng = new Random();
		String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
		char[] text = new char[10];
	    for (int i = 0; i < 10; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	public static NetworkService getInstance() {
		if (_instance == null) {
			_instance = new NetworkService(DEFAULT_HOST, DEFAULT_PORT);
		}
		return _instance;
	}

	/**
	 * Constructor: a network service for the application that creates and
	 * maintains an XMPP connection for a specific host and port
	 * 
	 * @param host
	 *            - the host where the XMPP server is running.
	 * @param port
	 *            - the port where the XMPP is listening.
	 * @throws XMPPException
	 */
	public NetworkService(String host, int port) {
		// BUGFIX for asmack
		configure(ProviderManager.getInstance());
		SmackConfiguration.setPacketReplyTimeout(100000);

		// create connection to host:port
		this.xmppConfig = new ConnectionConfiguration(host, port);
		this.xmppConfig.setSASLAuthenticationEnabled(true);
		this.xmppConn = new XMPPConnection(xmppConfig);
		this.accountManager = this.xmppConn.getAccountManager();

		// Audio connection
		JingleIQBuddyPacketRouter.setup(this.xmppConn);
	}// end NetworkService method

	public XMPPConnection getConnection() {
		if (!this.xmppConn.isConnected()) {
			try {
				this.xmppConn.connect();
			} catch (XMPPException e) {
				if (D)
					Log.d(TAG, "Connection error!");
			}
		}
		return this.xmppConn;
	}

	public ReturnState login(String email, String password) {
		try {
			// attempt to connect
			// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			this.xmppConn.connect();
			// extract JID from the email address by removing nonalphanumeric
			// characters from the email address
			String jid = email.replaceAll("[^a-zA-Z0-9]", "")
					+ DEFAULT_HOSTNAME;
			if (D)
				Log.d(TAG, "Attempt login: email - " + email + ", jid - " + jid
						+ ", password - " + password);
			try {
				Log.v(TAG, "username: " + jid);
				Log.v(TAG, "password: " + password);
				this.xmppConn.login(jid, password, DEFAULT_RESOURCE);
				Log.v(TAG, "successfully logged in");
				MultiUserChat.addInvitationListener(NetworkService
						.getInstance().getConnection(),
						new InvitationListener() {

							@Override
							public void invitationReceived(Connection conn, String room, String inviter, String reason, String password, Message message) {
								InvitationsList.getInstance().addInvitation(new Invitation(conn, room, inviter, reason, password, message));
							}

						});
				return ReturnState.SUCCEEDED;
			} catch (XMPPException e) {
				// if login failed
				try {
					String[] s = email.split("@");
					Log.v(TAG, "username: " + s[0]);
					Log.v(TAG, "password: " + password);
					// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
					this.xmppConn.login(s[0], password, DEFAULT_RESOURCE);
					Log.v(TAG, "success2");
				} catch (XMPPException e2) {
					Log.v(TAG, "xmppexception");
					Log.e(TAG, e.getMessage());
					return ReturnState.INVALID_PAIR;
				}
				Log.v(TAG, "xmppexception");
				Log.e(TAG, e.getMessage());
				return ReturnState.INVALID_PAIR;
			}
		} catch (XMPPException e) {
			if (D)
				Log.d(TAG, "Connection to server failed");
			return ReturnState.COULDNT_CONNECT;
		}
	}

	public boolean logout() {
		this.xmppConn.disconnect();

		// Disconnect audio
		HashMap<String, JingleController> allJCtrls = JingleController
				.getUsernameToJingleController();
		Iterator<String> jCtrlIter = allJCtrls.keySet().iterator();
		while (jCtrlIter.hasNext()) {
			JingleController jCtrl = allJCtrls.get(jCtrlIter.next());
			ReasonElementType reason = new ReasonElementType(
					ReasonElementType.TYPE_SUCCESS, "Done, Logging Off!");
			reason.setAttributeSID(jCtrl.getSID());
			jCtrl.getJiqActionMessageSender().sendSessionTerminate(
					UserManager.PRIMARY_USER.getJingle().getBuddyJID(),
					jCtrl.getBuddyJID(), jCtrl.getSID(), reason, jCtrl);
			jCtrl.getSessionState().changeSessionState(
					JingleIQPacket.AttributeActionValues.SESSION_TERMINATE);
		}

		// reconnect to the server
		_instance = new NetworkService(DEFAULT_HOST, DEFAULT_PORT);
		return true;
	}

	public PrivacyList getBlockList() {
		return this.blockList;
	}

	public AccountManager getAccountManager() {
		if (this.accountManager == null) {
			this.accountManager = new AccountManager(this.getConnection());
		}
		return this.accountManager;
	}

	/**
	 * TODO Remove extraneous providers
	 * 
	 * @param pm
	 *            - provider manager for connection
	 */
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
