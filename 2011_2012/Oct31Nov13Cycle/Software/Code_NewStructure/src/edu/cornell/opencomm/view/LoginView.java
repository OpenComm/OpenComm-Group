package edu.cornell.opencomm.view;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConfirmationController;
import edu.cornell.opencomm.controller.Login;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;

public class LoginView extends Activity {
	private static String LOG_TAG = "OC_LoginView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private LoginController loginController = new LoginController(this);
	private View loginLayout = null;

	// Debugging
	//private static final String TAG = "Controller.Login";
	private static final boolean D = true;

	// Layout Views
	private static EditText usernameEdit;
	private static EditText passwordEdit;
	private static Button loginButton;

	// Instance of XMPP connection
	public static NetworkService xmppService;
	public static ConnectionConfiguration xmppConfiguration;
	public static XMPPConnection xmppConnection;

	public LoginView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}

	/** Called when an activity is first created */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		usernameEdit = (EditText) findViewById(R.id.editTextUsername);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);

		loginButton = (Button) findViewById(R.id.buttonLogin);
	} // end onCreate method

	/** Called when the activity is becoming visible to the user. */
	public void onStart() {
		super.onStart();
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
				finish();
			}
			if (D)
				Log.d(LOG_TAG, "onStart: Network service started");
			if (D)
				Log.d(LOG_TAG, xmppService.toString());
		}
	} // end onStart method

	private void initEventsAndProperties() {
		// create property loginLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View loginViewFromInflater = inflater.inflate(
					R.layout.login_layout, null);
			if (loginViewFromInflater != null) {
				this.loginLayout = loginViewFromInflater;
			}
		}
		initializeLoginButtonClickedEvent();

	}

	private void initializeLoginButtonClickedEvent() {
		Button loginButton = getLoginButton();
		if (loginButton != null) {
			loginButton.setOnTouchListener(onLoginButtonClickedListener);
		}
	}

	public Button getLoginButton() {
		Button loginButton = null;
		if (loginLayout != null) {
			loginButton = (Button) loginLayout
					.findViewById(R.id.buttonAcceptConfirmation);
		}

		return loginButton;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	/*
	 * public PopupWindow getWindow() { return window; }
	 */

	public void setWindow(PopupWindow window) {
		this.window = window;
	}

	/*
	 * this method launches the confirmation layout on a popupwindiw, can be
	 * changed later to launch like a normal view
	 */
	public void launch() {
		if (inflater != null && loginLayout != null) {
			window = new PopupWindow(loginLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(loginLayout, 0, 1, 1);
			loginLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch login view as inflater layout is null");
		}
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// loginController.handlePopupWindowClicked();
		}
	};

	private View.OnTouchListener onLoginButtonClickedListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//loginController.handleLoginButtonClick();
			if (D) Log.d(LOG_TAG, "handleLogin: Attempt log in");
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
			Intent i = new Intent(LoginView.this, MainApplication.class);
			i.putExtra(Network.KEY_USERNAME, (D ? Network.DEBUG_USERNAME : usernameEdit.getText().toString()));
			i.setAction(Network.ACTION_LOGIN);
			startActivity(i);
			return true;
		}
	};
}
