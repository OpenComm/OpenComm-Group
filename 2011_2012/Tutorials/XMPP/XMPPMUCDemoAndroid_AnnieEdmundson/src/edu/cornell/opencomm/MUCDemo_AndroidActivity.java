package edu.cornell.opencomm;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MUCDemo_AndroidActivity extends Activity {
	
	private static Connection _connection;
	private static MultiUserChat _muc;
	
	public static final String TAG = "MUCDemo_AndroidActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //Try to connect to jabber.org.  Catch the exception of the connection could not be made.
		try{
			_connection = new XMPPConnection("jabber.org");
			_connection.connect();
			Log.v(TAG, "Connection created.");
		}
		catch(XMPPException e){
			Log.v(TAG, "Connection could not be created.");
			System.exit(1);
		}
		
		//Try to login with username: opencommsec and password: secopencomm.  Catch the exception if the login was not successful.
		try{
			_connection.login("opencommsec", "secopencomm");
			Log.v(TAG, "Successfully logged in.");
		}
		catch(Exception e){
			Log.v(TAG, "Could not login.");
			System.exit(1);
		}
		
		//Try to create a MultiUserChat (immediate).  Catch the exception of the MUC could not be created.
		try{
			_muc = new MultiUserChat(_connection, "MUC_Demo@conference.jabber.org");
			_muc.join("demo");
			_muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			Log.v(TAG, "Chat room created.");
		}
		catch(XMPPException e2){
			Log.v(TAG, "Chat room could not be created.");
			System.exit(1);
		}
		
		//Try to delete a participant (mucopencomm@jabber.org)
		try{
			_muc.banUser("mucopencomm@jabber.org","you're not allowed here!");
		}
		catch(XMPPException e3){
			Log.v(TAG, "We couldn't ban a user.");
		}
		
		//Create a message with text: This is the first message!
		Message m = _muc.createMessage();
		m.setBody("This is the first message!");
		
		//Try to send the created message
		try {
			_muc.sendMessage(m);
		} catch (XMPPException e) {
			Log.v(TAG, "We couldn't send a message.");
		}
		
		//Invite a participant to join the chat room
		_muc.invite("mucopencomm@jabber.org", "You're cool!");
		
		//Leave (end) the chat
		_muc.leave();
		
		//Disconnect
		_connection.disconnect();
	}    
        
}