package edu.cornell.opencomm.view;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.ReturnState;
import edu.cornell.opencomm.util.Util;

public class StartAppActivity extends Activity {
	/**
	 * The TAG for logging
	 */
	private static final String TAG = StartAppActivity.class.getSimpleName();
	private static final boolean D = true;
	/**
	 * The delay for splash screen in milliseconds
	 */
	private static final int SPLASH_SCREEN_DELAY = 3000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view_layout);
		FontSetter.applySanSerifFont(StartAppActivity.this,
				findViewById(R.id.splash_view_layout));
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				connect();
			}
		}, SPLASH_SCREEN_DELAY);

	}

	/**
	 * TODO: Use Network Service to connect to the server Note: Netowrk service
	 * is a AsyncTask so there is no need to create another. Just Saying :)
	 * 
	 */
	private void connect() {
		ReturnState returnState;
		ConnectionConfiguration config = new ConnectionConfiguration(
				Util.DEFAULT_HOST, Util.DEFAULT_PORT);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			returnState = ReturnState.SUCEEDED;
		} catch (XMPPException e) {
			returnState = ReturnState.COULDNT_CONNECT;
		}
		if (returnState == ReturnState.SUCEEDED) {
			launchLoginView();
		} else {
			// TODO : Alternate flow yet to decided
			onConnectionError();
		}
	}

	/**
	 * Finishes the current activity and launches the Login Activity
	 */
	private void launchLoginView() {
		Intent intent = new Intent(StartAppActivity.this, LoginView.class);
		startActivity(intent);
	}

	/**
	 * TODO: Ask design team for flow if connection to server fails
	 */
	private void onConnectionError() {
		// TODO
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}
}
