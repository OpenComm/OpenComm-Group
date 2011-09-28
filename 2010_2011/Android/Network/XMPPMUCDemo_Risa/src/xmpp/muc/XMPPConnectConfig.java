package xmpp.muc;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

/** An instance of this class configures the XMPP connection to a specified
 * host server through a specified post
 * @author risanaka (rn96@cornell.edu)
 *
 */
public class XMPPConnectConfig {
	private String host;
	private int port;
	private ConnectionConfiguration connConfig;
	private XMPPConnection xmppConn;
	
	/** CONSTRUCTOR: a new XMPP connection to a specified host server 
	 * through a specified port with certain configurations.
	 * @param hostName - the host where the XMPP server is running.
	 * @param portNum - the port where the XMPP server is listening
	 * @param compress - if the connection is going to use stream compression
	 * @param saslAuth - if the client is going to use SASL authentication 
	 * when logging into the server.
	 */
	public XMPPConnectConfig(String hostName, int portNum, 
			boolean compress, boolean saslAuth) {
		this.host = hostName;
		this.port = portNum;
		this.connConfig = new ConnectionConfiguration(this.host, this.port);
		this.connConfig.setCompressionEnabled(compress);
		this.connConfig.setSASLAuthenticationEnabled(saslAuth);
		this.xmppConn = new XMPPConnection(this.connConfig);		
	}

	/** = this XMPP Connection */
	public XMPPConnection getXmppConn() {
		return this.xmppConn;
	} // end getXmppConn method
	
	/** = this XMPP Connection's Configuration */
	public ConnectionConfiguration getConnConfig() {
		return this.connConfig;
	} // end getConnConfig method
	
	/** = String format of this connection: */
	public String toString() {
		String temp = "";
		temp += "XMPP Connection to host server " + this.host;
		temp += " through port " + this.port;
		temp += "\n\tStream compression enabled? " 
			+ (this.connConfig.isCompressionEnabled() ? "yes" : "no");
		temp += "\n\tDebugger enabled? "
			+ (this.connConfig.isDebuggerEnabled() ? "yes" : "no");		
		temp += "\n\tAre certificates presented checked for validity? "
			+ (this.connConfig.isExpiredCertificatesCheckEnabled() ? "yes" : "no");
		temp += "\n\tAre certificates presented checked for their domain? "
			+ (this.connConfig.isNotMatchingDomainCheckEnabled() ? "yes" : "no");
		temp += "\n\tIs reconnection allowed? "
			+ (this.connConfig.isReconnectionAllowed() ? "yes" : "no");
		temp += "\n\tIs the roster loaded at log in? "
			+ (this.connConfig.isRosterLoadedAtLogin() ? "yes" : "no");
		temp += "\n\tSASL authentication enabled? "
			+ (this.connConfig.isSASLAuthenticationEnabled() ? "yes" : "no");
		temp += "\n\tAre self-signed certificates accepted? "
			+ (this.connConfig.isSelfSignedCertificateEnabled() ? "yes" : "no");
		temp += "\n\tIs the whole chain of certificates presented checked? "
			+ (this.connConfig.isVerifyChainEnabled() ? "yes" : "no");
		temp += "\n\tIs the root CA checking performed? "
			+ (this.connConfig.isVerifyRootCAEnabled() ? "yes" : "no");
		return temp;
	} // end toString method

} // end Class XMPPConnectConfig
