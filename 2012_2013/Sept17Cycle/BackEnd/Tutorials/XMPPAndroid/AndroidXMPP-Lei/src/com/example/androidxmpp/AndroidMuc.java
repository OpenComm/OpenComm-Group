package com.example.androidxmpp;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class AndroidMuc extends Dialog implements android.view.View.OnClickListener {
	
 	private MainActivity xmppClient;
 	
 	public AndroidMuc(MainActivity xmppClient){
 		super(xmppClient);
 		this.xmppClient=xmppClient;	
 	}
 	
	


	public void connect(View v) {
		
		Log.i("??", "test");
		
		/**
		XMPPConnection connection= new XMPPConnection("jabber.org");
		try {
			connection.connect();
			 Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
		} catch (XMPPException e) {
			 Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
	         Log.e("XMPPClient", e.toString());
	         
		}
		
		try{
			connection.login("mucopencomm", "opencommmuc");
			Presence presence = new Presence (Presence.Type.available);
			connection.sendPacket(presence);
			
		}
		catch(XMPPException ex){
			
		}
		
		dismiss();
		
		
		*/
		
	}



	@Override
	public void onClick(View v) {

		
	}

}
