package net.opencomm.smackTester;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class OCManager {
	private String server;
	private String user;
	private int port;
	private Collection rtpouts;
	private Collection rtpins;
	private Roster myRoster;
	private XMPPConnection conn;
	public OCManager() {

	}
	public OCManager(String user, String password, String server, int port) {
		this.user=user;
		this.server=server;
		this.port=port;
		ConnectionConfiguration config = new ConnectionConfiguration(this.server, this.port);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		conn = new XMPPConnection(config);
		boolean success=false;
		while(!success) {
			try {
				conn.connect();
				conn.login(this.user, password);
				success=true;
			}
			catch(XMPPException e) {		
			}
		}
		conn.addPacketListener(new OCListener, new OCFilter);
	}
	public Roster getRoster() {
		return myRoster=conn.getRoster();
	}
	public void kill() {
		conn.disconnect();
	
	}
}
