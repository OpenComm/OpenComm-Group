package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.ReturnState;
import edu.cornell.opencomm.network.NetworkService;

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
				if (D) Log.d(TAG, "Splash timeout:Attempting Connection");
				connect();
			}
		}, SPLASH_SCREEN_DELAY);

	}

	/**
	 * TODO: Use Network Service to connect to the server Note: Network service
	 * is a AsyncTask so there is no need to create another. Just Saying :)
	 * 
	 */
	private void connect() {
		ReturnState returnState;
		NetworkService xmppService = NetworkService.getInstance();
		boolean connected = xmppService.connect();
		returnState = connected ? ReturnState.SUCEEDED
				: ReturnState.COULDNT_CONNECT;
		if (returnState == ReturnState.SUCEEDED) {
			if (D) Log.d(TAG, "Connection Success:Launch Login View");
			launchLoginView();
		} else {
			if (D) Log.d(TAG, "Connection Failed:DisplayTip");
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
		Log.v(TAG, "Server connection error!");
		NotificationView notifyError = new NotificationView(StartAppActivity.this);
		notifyError.launch("Network Connection Failed!");
		launchLoginView();
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}
	
}