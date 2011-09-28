package edu.cornell.opencomm.network.sp11;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;

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
				
				muc.create("opencommss@jabber.org");
				
				// Send an empty room configuration form which indicates that we want an instant room
				muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				
				// Invite other user to join to the room
				muc.invite(addusername, "Meet me in the OpenComm Chat Room!");
				
				// Let the user listen for room invitations
				MultiUserChat.addInvitationListener(conn, new InvitationListener() {
					public void invitationReceived(XMPPConnection conn,
							String room, String inviter, String reason,
							String password, Message message) {
							// Reject the invitation
							MultiUserChat.decline(conn, room, inviter, reason);
					}

					@Override
					public void invitationReceived(Connection arg0,
							String arg1, String arg2, String arg3, String arg4,
							Message arg5) {
						// TODO Auto-generated method stub
						
					}
			      });				
				
				// Let the user listen for invitation rejections
				muc.addInvitationRejectionListener(new InvitationRejectionListener() {
					public void invitationDeclined(String invitee, String reason) {
					// Do whatever you need here...
					}
				});				
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
