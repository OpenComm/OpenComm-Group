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

import edu.cornell.opencomm.MainApplication;
import edu.cornell.opencomm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Induction point of the entire application
 *
 */
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
    	/** Create a main application activity and pass it what we get from the user
    	 *  running the login intent
    	 *  
    	 */
    	Intent i = new Intent(this, MainApplication.class);
    	i.putExtra(Networks.KEY_USERNAME, usernameEdit.getText().toString());
    	i.putExtra(Networks.KEY_PASSWORD, passwordEdit.getText().toString());
    	i.setAction(MainApplication.ACTION_CREATE_MAIN);
    	startActivity(i);
    }
    
    
}