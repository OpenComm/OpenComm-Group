package edu.cornell.opencomm.network;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.provider.*;

import android.os.Handler;
import android.util.Log;

/** An instance of this class is the XMPP connection */
public class NetworkService {
	// Debugging
	private static final String TAG = "NetworkService";
	private static final boolean D = true;
	
	// XMPP connection
	private XMPPConnection xmppConn = null;
	private ConnectionConfiguration xmppConfig = null;
	
	
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
	} // end NetworkService method
	
	/** = the XMPP connection */
	public XMPPConnection getXMPPConnection() {
		return this.xmppConn;
	} // end getXMPPConnection method
	
	/** Logs in to the server using the strongest authentication mode 
	 * supported by the server, then sets presence to available
	 * 
	 * @param uname - the username
	 * @param pwd - the password
	 * @throws XMPPException - if an error occurs
	 */
	public void login(String uname, String pwd) throws XMPPException {
		xmppConn.login(uname, pwd);
		if (xmppConn.isAuthenticated()) {
			if (D) Log.d(TAG, "Logged in as " + uname);
		}
		else {
			if (D) Log.d(TAG, "login: Log in attempt failed");
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

	        //  JEP-33: Extended Stanza Addressing
	        pm.addExtensionProvider("addresses","http://jabber.org/protocol/address", new MultipleAddressesProvider());
	 
	        //  Privacy
	        pm.addIQProvider("query","jabber:iq:privacy", new PrivacyProvider());
	 
	        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
	        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
	        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
	        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
	        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
	        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
	    }
	 
} // end Class NetworkService