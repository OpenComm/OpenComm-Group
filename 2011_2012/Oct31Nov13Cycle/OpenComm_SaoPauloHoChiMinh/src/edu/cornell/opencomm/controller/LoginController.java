package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

public class LoginController {
	private LoginView loginView;

	// Debugging
	private static final String LOG_TAG = "LoginController";
	private static final boolean D = true;

	// Instance of XMPP connection
	public static NetworkService xmppService;
	public static ConnectionConfiguration xmppConfiguration;
	public static XMPPConnection xmppConnection;

	public LoginController(LoginView loginView) {
		this.loginView = loginView;
	}

	public void handleLoginButtonClick(EditText usernameEdit, EditText passwordEdit) {
		loginView.getLoginButton().setBackgroundColor(R.color.light_grey);
		if (D) Log.d(LOG_TAG, "handleLogin: Attempt log in");
        // check if there is a connection
		if (xmppService == null) {
			try {
				xmppService = new NetworkService(Network.DEFAULT_HOST,
						Network.DEFAULT_PORT);
				if (D)
					Log.d(LOG_TAG, xmppService.toString());
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(LOG_TAG, "onStart: XMPP Connection not established");
				// finish the activity
				loginView.finish();
			}
			if (D)
				Log.d(LOG_TAG, "onStart: Network service started");
			if (D)
				Log.d(LOG_TAG, xmppService.toString());
		}
		try {
			if (D) {
				xmppService.login(Network.DEBUG_USERNAME, 
						Network.DEBUG_PASSWORD);
			}
			else {
				// log in using the username and password inputted by primary user			
				xmppService.login(usernameEdit.getText().toString(), 
					passwordEdit.getText().toString());
			}
		}
		catch (XMPPException e) {
			Log.e(LOG_TAG, "handleLogin: Log in failed");
			//return;
		}
	
		Intent i = new Intent(loginView, DashboardView.class);
		i.setAction(Network.ACTION_LOGIN);
		loginView.startActivity(i);
		/*LayoutInflater inflater = (LayoutInflater) loginView
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DashboardView dashboardView = new DashboardView(inflater);
		loginView.finish();
		dashboardView.launch();*/
	}
}
