package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** An instance of this class starts the Network version of the application */
public class Login extends Activity {
	// Debugging
	private static final String TAG = "OC_Login";
	private static final boolean D = true;
	
	// Layout Views
	private static EditText usernameEdit;
	private static EditText passwordEdit;
	private static Button loginButton;
	
	// Instance of XMPP connection
	public static NetworkService xmppService;
	public static ConnectionConfiguration xmppConfiguration;
	public static XMPPConnection xmppConnection;
	
	/** Called when an activity is first created */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        usernameEdit = (EditText)findViewById(R.id.input_username);
        passwordEdit = (EditText)findViewById(R.id.input_password);
        
        loginButton = (Button)findViewById(R.id.login_button);
    } // end onCreate method
	
	/** Called when the activity is becoming visible to the user. */
	public void onStart() {
		super.onStart();
		// check if there is a connection
		if (xmppService == null) {
			try {
				xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
				if (D) Log.d(TAG, xmppService.toString());
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "onStart: XMPP Connection not established");
				// finish the activity
				finish();
			}
			if (D) Log.d(TAG, "onStart: Network service started");
			//if (D) Log.d(TAG, xmppService.toString());
		}
	} // end onStart method
	
	/** Called when the login button is clicked */
	public void handleLogin(View view){
		if (D) Log.d(TAG, "handleLogin: Attempt log in");
		usernameEdit.setText("opencommss@jabber.org");
		passwordEdit.setText("ssopencomm");
		try {
			// log in using the username and password inputted by primary user
			xmppService.login(usernameEdit.getText().toString(), 
					passwordEdit.getText().toString());
		}
		catch (XMPPException e) {
			Log.e(TAG, "handleLogin: Log in failed");
			return;
		}
		Intent i = new Intent(Login.this, MainApplication.class);
		i.putExtra(Network.KEY_USERNAME, usernameEdit.getText().toString());
		i.setAction(Network.ACTION_LOGIN);
		startActivity(i);
	} // end handleLogin method
} // end Class Login
