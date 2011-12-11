package edu.cornell.opencomm.network;

import java.util.Collection;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.*;
import org.jivesoftware.smackx.search.UserSearch;

import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.InvitationView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

/** An instance of this class is the XMPP connection */
public class NetworkService {
	// Debugging
	private static final String TAG = "NetworkService";
	private static final boolean D = true;
	
	// XMPP connection
	private XMPPConnection xmppConn = null;
	private ConnectionConfiguration xmppConfig = null;
	private InvitationListener invitationListener;
	
	
	/** Constructor: a network service for the application that creates and 
	 * maintains an XMPP connection for a specific host and port
	 * 
	 * @param host - the host where the XMPP server is running.
	 * @param port - the port where the XMPP is listening.
	 * @throws XMPPException 

	 */
	public NetworkService(String host, int port) throws XMPPException {
        // BUGFIX
        configure(ProviderManager.getInstance());
        SmackConfiguration.setPacketReplyTimeout(100000);
		xmppConfig = new ConnectionConfiguration(host, port);
		xmppConn = new XMPPConnection(xmppConfig);
		xmppConn.connect();
		if (xmppConn.isConnected()) {
			if (D) {
				Log.d(TAG, "XMPP connection established to " + 
						host + " through " + port);
			}
		}
		else {
			if (D) {
				Log.d(TAG, "XMPP connection not established");
			}
		}
		invitationListener = 
		/*MultiUserChat.addInvitationListener(xmppConn, */new InvitationListener(){

			/**
			 * Automagically called when this client receives an invitation to join a MUC
			 */
			@Override
			public void invitationReceived(Connection connection, String room, 
					String inviter, String reason, String password, Message message) {
				Invitation invitation = new edu.cornell.opencomm.model.Invitation(
						connection, room, inviter, reason, password, message); 
				
				Log.v("NetworkService", "Invitation Received for room " + room);

				// Find the room
				String roomID = Network.ROOM_NAME + room + "@conference.jabber.org";
				MultiUserChat muc = new MultiUserChat(LoginController.xmppService.getXMPPConnection(), roomID);
				// Get the people in the room
			/*	Object[] members = null;
				String nickname;
				try {
					members= muc.getParticipants().toArray();
				} catch (XMPPException e) {
					Log.v("NetworkService", "Cannot retrieve participants from server.");
				}
				// Get the user/occupant object of the inviter
				if(members!=null){
					Occupant occupantInviter = null;
					for(Object o : members){
						String jid = ((Occupant)o).getJid();
						if(jid==inviter)
							occupantInviter = (Occupant)o;
					} */
					/* Get information from person's profile
					 * such as: name, phone, email.
					 * For now, we do not have profile information
					 */
					//if(occupantInviter!=null){
					//	nickname = occupantInviter.getNick();
				
						String nickname = inviter.split("@")[0];
						// Create the invitation
						LayoutInflater inflater = (LayoutInflater) MainApplication.screen.getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						InvitationView invitationView = new InvitationView(inflater, invitation);
						invitation.setMUC(muc);
						invitationView.setInvitationInfo(-1, nickname, "None", "None");
						MainApplication.screen.getActivity().displayPopup(invitationView);
					}

				//} 
				
				/*Log.v("InvitationController", "How is room formatted?" + room);
				//answer: room@server (ex. hellokitty@conference.jabber.org)

				
				Space inviteSpace = Space.allSpaces.get(room);
				inviteSpace.getInvitationController().setInvitation(invitation);
				Log.v("InvitationController", "The invitation received exists! " +
						"It is: " + inviteSpace.getInvitationController().
						getInvitation().toString());
				
				//TODO: Trigger update to the view!
				//TODO: call InvitationController.accept, or InvitationController.decline
				
				//DEBUG
				Log.v("InvitationController", "invitationReceived - Invitation " +
						"received from: " + inviter + " to join room: " + room);  */
		//	} 
			
		}/*)*/;
		MultiUserChat.addInvitationListener(xmppConn,invitationListener);
	} // end NetworkService method
	
	/** = the XMPP connection */
	public XMPPConnection getXMPPConnection() {
		return this.xmppConn;
	} // end getXMPPConnection method
	
	/** = the invitaitonListener */
	public InvitationListener getInvitiationListener(){
		return invitationListener;
	}
	
	/** Logs in to the server using the strongest authentication mode 
	 * supported by the server, then sets presence to available
	 * 
	 * @param uname - the username
	 * @param pwd - the password
	 * @throws XMPPException - if an error occurs
	 */
	public void login(String uname, String pwd) throws XMPPException {
		// check that the connection is not already authenticated
		if (!xmppConn.isAuthenticated()) {
			xmppConn.login(uname, pwd);
			if (D) {
				if (xmppConn.isAuthenticated()) {
					Log.d(TAG, "Logged in as " + uname);
				}
				else {
					Log.d(TAG, "login: Log in attempt failed");
				}
			}
		}
		else {
			Log.e(TAG, "login: Already logged in as " + xmppConn.getUser());
		}
	} // end login method
	
	/** Disconnect the XMPP connection */
	public void disconnect() {
		xmppConn.disconnect();
	} // end disconnect method
	
	/** = String representation of the network service */
	public String toString() {
		String temp = "";
		temp += "XMPP Connection to host server " + this.xmppConfig.getHost();
		temp += " through port " + this.xmppConfig.getPort();			
		temp += "\n\tStream compression enabled? " 
			+ (this.xmppConfig.isCompressionEnabled() ? "yes" : "no");
		temp += "\n\tDebugger enabled? "
			+ (this.xmppConfig.isDebuggerEnabled() ? "yes" : "no");		
		temp += "\n\tAre certificates presented checked for validity? "
			+ (this.xmppConfig.isExpiredCertificatesCheckEnabled() ? "yes" : "no");
		temp += "\n\tAre certificates presented checked for their domain? "
			+ (this.xmppConfig.isNotMatchingDomainCheckEnabled() ? "yes" : "no");
		temp += "\n\tIs reconnection allowed? "
			+ (this.xmppConfig.isReconnectionAllowed() ? "yes" : "no");
		temp += "\n\tIs the roster loaded at log in? "
			+ (this.xmppConfig.isRosterLoadedAtLogin() ? "yes" : "no");
		temp += "\n\tSASL authentication enabled? "
			+ (this.xmppConfig.isSASLAuthenticationEnabled() ? "yes" : "no");
		temp += "\n\tAre self-signed certificates accepted? "
			+ (this.xmppConfig.isSelfSignedCertificateEnabled() ? "yes" : "no");
		temp += "\n\tIs the whole chain of certificates presented checked? "
			+ (this.xmppConfig.isVerifyChainEnabled() ? "yes" : "no");
		temp += "\n\tIs the root CA checking performed? "
			+ (this.xmppConfig.isVerifyRootCAEnabled() ? "yes" : "no");
		if (this.xmppConn.isConnected()) {
			temp += "\nXMPP Connection established";
			if (this.xmppConn.isAuthenticated()) {
				temp += "\nXMPP Connection authenticated for " + this.xmppConn.getUser();
			}
			else {
				temp += "\nXMPP Connection not authorized";
			}
		}
		else {
			temp += "\nXMPP Connection not established";
		}
		return temp;
	}
	
	public void configure(ProviderManager pm) {
        //  Private Data Storage
        pm.addIQProvider("query","jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
 
 
        //  Time
        try {
            pm.addIQProvider("query","jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }
 
        //  Roster Exchange
        pm.addExtensionProvider("x","jabber:x:roster", new RosterExchangeProvider());
 
        //  Message Events
        pm.addExtensionProvider("x","jabber:x:event", new MessageEventProvider());
 
        //  Chat State
        pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
 
        pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
 
        pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
 
        pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
 
        pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
 
        //  XHTML
        pm.addExtensionProvider("html","http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
 
        //  Group Chat Invitations
        pm.addExtensionProvider("x","jabber:x:conference", new GroupChatInvitation.Provider());
 
        //  Service Discovery # Items    
        pm.addIQProvider("query","http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
 
        //  Service Discovery # Info
        pm.addIQProvider("query","http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
 
        //  Data Forms
        pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());
 
        //  MUC User
        pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user", new MUCUserProvider());
 
        //  MUC Admin    
        pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
 
        //  MUC Owner    
        pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
        
        //  Delayed Delivery
        pm.addExtensionProvider("x","jabber:x:delay", new DelayInformationProvider());
 
        //  Version
        try {
            pm.addIQProvider("query","jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            //  Not sure what's happening here.
        }
 
        //  VCard
        pm.addIQProvider("vCard","vcard-temp", new VCardProvider());
 
        //  Offline Message Requests
        pm.addIQProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
 
        //  Offline Message Indicator
        pm.addExtensionProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
 
        //  Last Activity
        pm.addIQProvider("query","jabber:iq:last", new LastActivity.Provider());
 
        //  User Search
        pm.addIQProvider("query","jabber:iq:search", new UserSearch.Provider());
 
        //  SharedGroupsInfo
        pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());
 
        //  JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses","http://jabber.org/protocol/address", new MultipleAddressesProvider());
 
        //   FileTransfer
        pm.addIQProvider("si","http://jabber.org/protocol/si", new StreamInitiationProvider());
 
        pm.addIQProvider("query","http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
 
        pm.addIQProvider("open","http://jabber.org/protocol/ibb", new IBBProviders.Open());
 
        pm.addIQProvider("close","http://jabber.org/protocol/ibb", new IBBProviders.Close());
 
        pm.addExtensionProvider("data","http://jabber.org/protocol/ibb", new IBBProviders.Data());
 
        //  Privacy
        pm.addIQProvider("query","jabber:iq:privacy", new PrivacyProvider());
 
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
    } // end configure method
    
    public static String printXMPPError(XMPPException e) {
        String temp = "";
        if (e != null && e.getXMPPError() != null) {
            temp += "XMPPError: ";
            temp += "(" + e.getXMPPError().getCode() + ")";
            temp += (e.getXMPPError().getMessage() != null ? " - " + e.getXMPPError().getMessage() : "");
        }
        return temp;
    } // return printXMPPError method
	 
} // end Class NetworkService
