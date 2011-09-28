package edu.cornell.opencomm.network.sp11;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import edu.cornell.opencomm.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MUCGUI extends Activity implements InvitationListener, MessageListener, ParticipantStatusListener, InvitationRejectionListener {
	private static String LOGTAG = "MUCGUI";
	private XMPPService mBoundService;
	private XMPPConnection conn;
	private String roomName = "opencomm";
	private String reason = "MUCGUI testing";
	private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	private String username;
	private String[] invitees = {"risan@jabber.org", "mucopencomm@jabber.org"};
	private Occupant[] members;
	private boolean acceptInvite = false;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muc);
        // Get the necessary parts from the intent that called this activity
        username = getIntent().getExtras().getString(Networks.KEY_USERNAME);
        conn = mBoundService.getXMPPConnection();
        // To create a room
        MultiUserChat muc = new MultiUserChat(conn, roomName);
        try {
			muc.create(username);
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		// Send invitation to invitees
		for (String inv : invitees) {
			muc.invite(inv, reason);
		}
		
		// Grant moderator access to all users
		try {
			members = (Occupant[]) muc.getParticipants().toArray();
			for (Occupant mem : members) {
				muc.grantModerator(mem.getNick());
			}
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		// to send a message
		try {
			muc.sendMessage("Hello");
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		
		// Kick users out of the room
		try {
			members = (Occupant[]) muc.getParticipants().toArray();
			for (Occupant mem : members) {
				muc.kickParticipant(mem.getNick(), reason);
			}
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}		
		
		// To leave a room
		// muc.leave();
		
		// To destroy a room
		try {
			muc.destroy(this.reason, null);
		} catch (XMPPException e) {
			Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	} // onCreate method
	
	public void invitationReceived(Connection conn, String room,
			String inviter, String reason, String password, Message message) {
		// if accepting the invitation
		if (this.acceptInvite) {
			MultiUserChat muc = new MultiUserChat(conn, room);
			try {
				muc.join(this.username, password);
			} catch (XMPPException e) {
				Log.e(MUCGUI.LOGTAG, "XMPPException error: " + 
						e.getXMPPError().getCode());
				Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
		                Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			Log.i(MUCGUI.LOGTAG, this.username + " joined room " + room);
			
		}
		// if rejecting the invitation
		else {
			MultiUserChat.decline(conn, room, inviter, reason);
			Log.i(MUCGUI.LOGTAG, this.username + " declined invitation to room " + room);
		}
	} // end invitationReceived method

	@Override
	public void invitationDeclined(String invitee, String reason) {
		Log.i(MUCGUI.LOGTAG, invitee + " rejected your invitation because " + reason);
		
	} // end invitationDeclined method
	
	// Various methods that are called as the room occupancy situation changes
	@Override
	public void adminGranted(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminRevoked(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banned(String participant, String actor, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joined(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kicked(String participant, String actor, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void left(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void membershipGranted(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void membershipRevoked(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moderatorGranted(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moderatorRevoked(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nicknameChanged(String participant, String newNickname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ownershipGranted(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ownershipRevoked(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void voiceGranted(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void voiceRevoked(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		String from = StringUtils.parseResource(message.getFrom());
		Log.i(MUCGUI.LOGTAG, "[" + df.format(new Date()) + "]" +
				from + ": " + message.getBody());
	} // end processMessage method
}
