/**
 * An instance of this class is the XMPP connection
 *
 * Issues [TODO]
 * - Look into authentication used in login function once we move to openfire
 * - Disconnect() or shutdown()
 *
 * @author rahularora, risanaka, kriskooi, anneedmundson, jp
 * */

package edu.cornell.opencomm.network;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.InvitationPopupPreviewView;
import edu.cornell.opencomm.view.InvitationView;

public class NetworkService {
	// Debugging
	private static final String TAG = "NetworkService";
	private static final boolean D = Values.D;

	// XMPP connection
	private XMPPConnection xmppConn = null;
	private ConnectionConfiguration xmppConfig = null;
	private InvitationListener invitationListener;
	private ConnectionListener connectionListener;
	private boolean isConnected;

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
		// BUGFIX
		configure(ProviderManager.getInstance());
		SmackConfiguration.setPacketReplyTimeout(100000);

		// Create a connection to the server on a specific port
		xmppConfig = new ConnectionConfiguration(host, port);
		xmppConn = new XMPPConnection(xmppConfig);
		try {
			xmppConn.connect();
			isConnected = xmppConn.isConnected();
			if (isConnected) {
				if (D) {
					Log.d(TAG, "XMPP connection established to " + host
							+ " through " + port);
				}

				connectionListener = new ConnectionListener(){
					ReconnectionManager reconnectionManager;
					@Override
					public void connectionClosed() {
						// connection was closed by the foreign host
						// or we have closed the connection
						Log.d(TAG, "ConnectionListener: connectionClosed() called - " +
								"connection was shutdown by foreign host or by us");
						reconnectionManager.connectionClosed();
					}

					@Override
					public void connectionClosedOnError(Exception e) {
						// this happens mainly because of on IOException
						// eg. connection timeouts because of lost connectivity
						Log.d(TAG, "xmpp disconnected due to error: ", e);
						reconnectionManager.connectionClosedOnError(e);

					}

					@Override
					public void reconnectingIn(int arg0) {
						reconnectionManager.reconnectingIn(arg0);
						Log.d(TAG, "Reconnecting in "+arg0);
					}

					@Override
					public void reconnectionFailed(Exception arg0) {
						reconnectionManager.reconnectionFailed(arg0);
						Log.d(TAG, "Reconnection Manager is running");
					}

					@Override
					public void reconnectionSuccessful() {
						reconnectionManager.reconnectionSuccessful();
						Log.d(TAG, "Reconnection Manager is sucessful");
					}


				};

				xmppConn.addConnectionListener(connectionListener);

				invitationListener = new InvitationListener() {
					/**
					 * Automatically called when this client receives an
					 * invitation to join a MUC
					 */
					@Override
					public void invitationReceived(Connection connection,
							String room, String inviter, String reason,
							String password, Message message) {

						Log.v("NetworkService", "Invitation Received for room "
								+ room);

						String roomID = room;
						MultiUserChat muc = new MultiUserChat(
								LoginController.xmppService.getXMPPConnection(),
								roomID);
						String nickname = inviter.split("@")[0];

						Invitation invitation = new Invitation(connection,
								room, inviter, reason, password, message, muc);

						// This invitation is for a conference - since you are
						// not in one atm.
						// Create regular invitation popup.
						Log.v("Space size", ""
								+ Space.getMainSpace().getAllParticipants()
								.size());
						Log.v("Owner nickname", Space.getMainSpace().getOwner()
								.getNickname());
						Log.v("Invitation nickname", nickname);
						// if(!nickname.equals(Space.getMainSpace().getOwner().getNickname())){
						// If invite is not from someone from your main space,
						// accept invitation to main space. Else create popup
						// for side chat acceptance.
						if (!Space.getMainSpace().getAllNicknames()
								.containsKey(nickname)) {
							// Create the invitations
							LayoutInflater inflater = (LayoutInflater) MainApplication.screen
									.getActivity().getSystemService(
											Context.LAYOUT_INFLATER_SERVICE);
							InvitationView invitationView = new InvitationView(
									inflater, invitation);
							invitationView.setInvitationInfo(-1, nickname,
									"None", "None");
							MainApplication.screen.getActivity().displayPopup(
									invitationView);
						} else{
							//Side chat invitation
							try {
								Log.v("InvitationPopupPreviewView: ",
										"Attempting to create invitation preview popup");

								InvitationPopupPreviewView ippv = null;
								try {
									ippv = new InvitationPopupPreviewView(
											MainApplication.screen.getContext(), invitation);
								} catch (XMPPException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								MainApplication.screen.getActivity().displayPopup(
										ippv);
								// SpaceController.addExistingSpace(MainApplication.screen.getContext(),
								// false,
								// invitation.getRoom());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};

				MultiUserChat.addInvitationListener(xmppConn,
						invitationListener);
			}

			else {
				if (D) {
					Log.d(TAG, "XMPP connection not established");
				}
			}
		} catch (XMPPException e) {
			Log.e(TAG, printXMPPError(e));
		}
	} // end NetworkService method

	/**
	 * get the XMPP connection
	 * */
	public XMPPConnection getXMPPConnection() {
		return this.xmppConn;
	}

	/**
	 * get the XMPP configuration
	 * */
	public ConnectionConfiguration getConnectionConfiguration() {
		return this.xmppConfig;
	}

	/**
	 * get the invitationListener
	 * */
	public InvitationListener getInvitiationListener() {
		return this.invitationListener;
	}

	/**
	 * Logs in to the server using the strongest authentication mode supported
	 * by the server, then sets presence to available
	 *
	 * @param uname
	 *            - the username
	 * @param pwd
	 *            - the password
	 * @throws XMPPException
	 *             - if an error occurs
	 */
	public boolean login(String uname, String pwd) {
		// check that the connection is not already authenticated
		if (!xmppConn.isAuthenticated()) {
			try {
				xmppConn.login(uname, pwd);
				if (D) {
					if (xmppConn.isAuthenticated()) {
						Log.d(TAG, "Logged in as " + uname);
						return true;
					} else {
						Log.d(TAG, "login: Log in attempt failed");
						return false;
					}
				}
			} catch (XMPPException e) {
				// TODO: Force close can happen here if connection fails.
				// Maybe try again, and if it still fails, tell user login
				// failed.
				Log.e(TAG, printXMPPError(e));
				return false;
			}
		} else {
			Log.v(TAG, "login: Already logged in as " + xmppConn.getUser());
		}
		return true;
	}

	/* *
	 * Disconnect the XMPP connection
	 */
	public void disconnect() {
		xmppConn.disconnect();
	}

	/* *
	 * String representation of the network service
	 */
	@Override
	public String toString() {
		String networkServiceRepresentation = "";
		networkServiceRepresentation += "XMPP Connection to host server "
				+ this.xmppConfig.getHost()
				+ " through port "
				+ this.xmppConfig.getPort()
				+ "\n\tStream compression enabled? "
				+ (this.xmppConfig.isCompressionEnabled() ? "yes" : "no")
				+ "\n\tDebugger enabled? "
				+ (this.xmppConfig.isDebuggerEnabled() ? "yes" : "no")
				+ "\n\tAre certificates presented checked for validity? "
				+ (this.xmppConfig.isExpiredCertificatesCheckEnabled() ? "yes"
						: "no")
						+ "\n\tAre certificates presented checked for their domain? "
						+ (this.xmppConfig.isNotMatchingDomainCheckEnabled() ? "yes"
								: "no")
								+ "\n\tIs reconnection allowed? "
								+ (this.xmppConfig.isReconnectionAllowed() ? "yes" : "no")
								+ "\n\tIs the roster loaded at log in? "
								+ (this.xmppConfig.isRosterLoadedAtLogin() ? "yes" : "no")
								+ "\n\tSASL authentication enabled? "
								+ (this.xmppConfig.isSASLAuthenticationEnabled() ? "yes" : "no")
								+ "\n\tAre self-signed certificates accepted? "
								+ (this.xmppConfig.isSelfSignedCertificateEnabled() ? "yes"
										: "no")
										+ "\n\tIs the whole chain of certificates presented checked? "
										+ (this.xmppConfig.isVerifyChainEnabled() ? "yes" : "no")
										+ "\n\tIs the root CA checking performed? "
										+ (this.xmppConfig.isVerifyRootCAEnabled() ? "yes" : "no");

		if (this.xmppConn.isConnected()) {
			networkServiceRepresentation += "\nXMPP Connection established";
			if (this.xmppConn.isAuthenticated()) {
				networkServiceRepresentation += "\nXMPP Connection authenticated for "
						+ this.xmppConn.getUser();
			} else {
				networkServiceRepresentation += "\nXMPP Connection not authorized";
			}
		} else {
			networkServiceRepresentation += "\nXMPP Connection not established";
		}
		return networkServiceRepresentation;
	}

	/**
	 * TODO Ask Kris about it and write the comments
	 *
	 * @param pm
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

	public static String printXMPPError(XMPPException e) {
		String xmppExceptionString = "";
		if (e != null && e.getXMPPError() != null) {
			xmppExceptionString += "XMPPError: ";
			xmppExceptionString += "(" + e.getXMPPError().getCode() + ")";
			xmppExceptionString += (e.getXMPPError().getMessage() != null ? " - "
					+ e.getXMPPError().getMessage()
					: "");
		}
		return xmppExceptionString;
	}

	/**
	 * Destroy conference
	 * */
	public static void destroyMUC(MultiUserChat muc) {
		try {
			muc.destroy(null, null);
		} catch (XMPPException e) {
			Log.e(TAG, printXMPPError(e));
		}
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	public boolean isOnline(View v) {
		ConnectivityManager cm = (ConnectivityManager) v.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

} // end Class NetworkService
