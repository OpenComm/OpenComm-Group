package edu.cornell.opencomm.network.sp11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class XMPPService extends Service {
	private static String LOGTAG = "XMPPService";
	private String username;
	private String password;
	public static XMPPUser user1 = new XMPPUser("opencommss@jabber.org", "ssopencomm");
	public static XMPPUser user2 = new XMPPUser("risan@jabber.org", "reesaspbc176");
	public static XMPPUser user3 = new XMPPUser("mucopencomm@jabber.org", "opencommmuc");
    public static int userCount = 3; // change as needed
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (NetworkServices.ACTION_SIGNIN.equals(intent.getAction())) {
			try {
				Intent i = intent;
				username = i.getStringExtra(NetworkServices.KEY_USERNAME);
				password = i.getStringExtra(NetworkServices.KEY_PASSWORD);
				// Create a new XMPP Connection to host server jabber.org through
				// port 5222 with stream compression and SASL Auth. enabled
				XMPPConnectConfig xmppConn =
					new XMPPConnectConfig("jabber.org", 5222, true, true);
				XMPPConnection conn = xmppConn.getXmppConn();
				Log.i(XMPPService.LOGTAG, "XMPP Connection created:\n" + xmppConn);
				// Connect to the server
				conn.connect();
				Log.i(XMPPService.LOGTAG, "\tXMPP Connection successfully connected? " + 
						(conn.isConnected() ? "yes" : "no"));
				
				// Log in as OpenCommSS
				Log.i(XMPPService.LOGTAG, "\nLog in as user " + this.username + ":");
				// SASLAuthentication.supportSASLMechanism("PLAIN", 0);
				try {
					conn.login(this.username, this.password);
				}
				catch (VerifyError e) {
					Log.e(XMPPService.LOGTAG, "Verify Error");
					xmppConn.getConnConfig().setSASLAuthenticationEnabled(false);
					conn = xmppConn.getXmppConn();
					conn.login(this.username, this.password);
				}
				Log.i(XMPPService.LOGTAG, "\tUser " + this.username
						+ " successfully logged in? " 
						+ (conn.isAuthenticated() ? "yes" : "no"));
	
				// Disconnect from the server
				conn.disconnect();
				Log.i(XMPPService.LOGTAG,"Disconnected? " + (conn.isConnected() ? " no" : " yes"));
				Log.i(XMPPService.LOGTAG,"Demo concluded.");
				
			}
			catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return startId;
	}
		
	
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
