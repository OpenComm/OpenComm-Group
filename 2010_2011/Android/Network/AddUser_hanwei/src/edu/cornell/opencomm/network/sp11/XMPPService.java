package edu.cornell.opencomm.network.sp11;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class XMPPService extends Service {	
	private String addusername;
	MultiUserChat muc;

	public int onStartCommand(Intent intent, int flags, int startId) {
		if (NetworkServices.ACTION_ADDUSER.equals(intent.getAction())) {
			try {
				Intent i = intent;
				addusername = i.getStringExtra(NetworkServices.KEY_ADDUSERNAME);
				
				// Create a new XMPP Connection to host server jabber.org through
				// port 5222 with stream compression and SASL Auth. enabled
				XMPPConnectConfig xmppConn = new XMPPConnectConfig("jabber.org", 5222, true, true);
				XMPPConnection conn = xmppConn.getXmppConn();
				
				// Connect to the server
				conn.connect();		
				
				// Log in as OpenCommSS
				conn.login("opencommss@jabber.org", "ssopencomm");
				
		    	// Create a MultiUserChat using an XMPPConnection for a room
				muc = new MultiUserChat(conn, "OpenComm Chat Room");
				
				// Invite other user to join to the room
				muc.invite(addusername, "Meet me in the OpenComm Chat Room!");
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
