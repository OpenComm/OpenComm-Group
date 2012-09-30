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

public class AndroidMuc implements android.view.View.OnClickListener {
	
 	private MainActivity xmpp;
 	//EditText simpleEditText = (EditText) findViewById(R.id.editTextSimple);
 	//String strValue = simpleEditText.getText().toString();
 	
 	public AndroidMuc(MainActivity xmppClient){
 		this.xmpp=xmppClient;	
 		
 	}
 	
	


	public void connect(View v) {
		
		//Log.v("??", "test");
		
		ConnectionConfiguration config= new ConnectionConfiguration("jabber.org");
		config.setReconnectionAllowed(true);
		config.setSASLAuthenticationEnabled(true);
		XMPPConnection connection= new XMPPConnection(config);
		
		
		try {
			connection.connect();
			 Log.v("AndroidMUC", "[SettingsDialog] Connected to " + connection.getHost());
		} catch (XMPPException e) {
			 Log.v("AndroidMUC", "[SettingsDialog] Failed to connect to " + connection.getHost());
	         Log.v("AndroidMUC", e.toString());
	         
		}
		
		try{
			connection.login("mucopencomm", "opencommmuc");
			Log.v("AndroidMUC","You have successfully connected!");
			Presence presence = new Presence (Presence.Type.available);
			connection.sendPacket(presence);
			xmpp.connetionSetup(connection);
			
		}
		catch(XMPPException ex){
			Log.v("AndroidMUC", "failure to login");
			Log.v("AndroidMUC", ex.toString());
			
		}
		
		
		
		
		
	}



	@Override
	public void onClick(View v) {

		
	}

}
