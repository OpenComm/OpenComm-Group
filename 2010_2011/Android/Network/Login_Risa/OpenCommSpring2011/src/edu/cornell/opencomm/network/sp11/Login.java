package edu.cornell.opencomm.network.sp11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	private static String LOGTAG = "Login";
	private EditText usernameEdit;
	private EditText passwordEdit;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameEdit = (EditText) findViewById(R.id.userInput);
        passwordEdit = (EditText) findViewById(R.id.pwdInput);
    }
    
    /** Attempts login when submit button is clicked */
    public void handleLogin(View view) {
    	Intent i = new Intent(this, XMPPService.class);
    	i.putExtra(NetworkServices.KEY_USERNAME, usernameEdit.getText().toString());
    	i.putExtra(NetworkServices.KEY_PASSWORD, passwordEdit.getText().toString());
    	i.setAction(NetworkServices.ACTION_SIGNIN);
    	startService(i);
    }
}