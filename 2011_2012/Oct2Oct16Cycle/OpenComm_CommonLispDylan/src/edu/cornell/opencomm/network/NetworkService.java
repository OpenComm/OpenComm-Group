package edu.cornell.opencomm.network;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

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
	
	// Status of Connection
	private int connStatus = Network.STATE_NONE;
	// Handler between UI activities
	private Handler xmppHandler;
	
	/** Constructor: a network service for the application that creates and 
	 * maintains an XMPP connection for a specific host and port
	 * 
	 * @param host - the host where the XMPP server is running.
	 * @param port - the port where the XMPP is listening.
	 * @throws XMPPException 

	 */
	public NetworkService(String host, int port) throws XMPPException {
		xmppConfig = new ConnectionConfiguration(host, port);
		xmppConn = new XMPPConnection(xmppConfig);
		xmppConn.connect();
		if (xmppConn.isConnected()) {
			connStatus = Network.STATE_CONNECTED;
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
	
	/** = the status of the XMPP connection */
	public int getConnectionStatus() {
		return this.connStatus;
	} // end getConnectionStatus method
	
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
			connStatus = Network.STATE_LOGIN;
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
	
} // end Class NetworkService