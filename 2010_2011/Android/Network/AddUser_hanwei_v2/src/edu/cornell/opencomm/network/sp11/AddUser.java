package edu.cornell.opencomm.network.sp11;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class AddUser extends Activity {
	private EditText addUsernameEdit;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addUsernameEdit = (EditText) findViewById(R.id.entry);
    }
    
    /** Attempts to add users when OK button is clicked */
    public void handleAddUserToPS(View view) {
    	Intent i = new Intent(this, XMPPService.class);
    	i.putExtra(NetworkServices.KEY_ADDUSERNAME, addUsernameEdit.getText().toString());
    	i.setAction(NetworkServices.ACTION_ADDUSER);
    	startService(i);
    } 
}