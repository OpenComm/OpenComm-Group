package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.LoginController;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginView extends Activity {
	
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	private static final boolean D = true;	
	private WakeLock mWakeLock;

	/**
	 * The TAG for logging
	 */
	private static final String TAG = LoginView.class.getSimpleName();
	
	
	private LoginController loginController;
	
	private EditText emailEdit;
	private EditText passwordEdit;
	private ImageView loginOverlay;
	private ImageView signupOverlay;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_v2);
		emailEdit = (EditText) findViewById(R.id.tbx_usrn);
		passwordEdit = (EditText) findViewById(R.id.tbx_pwd);
		loginOverlay = (ImageView) findViewById(R.id.login_loginOverlay);
		signupOverlay = (ImageView) findViewById(R.id.login_signupOverlay);
		loginController = new LoginController(this);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		/*
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		mWakeLock.acquire();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	/* [TODO]: check to see if this should be onPause() */
	@SuppressLint("Wakelock")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	
	/** Jump to the account creation page when sign-up button is clicked */
	public void signup(View v) {
		if(D)
			Log.d(TAG, "Sign up button clicked");
		this.loginController.handleSignup();
	}
	
	
	/** Jump to the Reset Password page when forgot-password is clicked */
	public void forgot(View v) {
		if(D)
			Log.d(TAG, "Forgot password text button clicked");
		this.loginController.handleForgotPassword();
	}
	
	
	/**
	 * Attempt to login using the given email and password, launches Dashboard
	 * when successful
	 * 
	 * @param v
	 *            - view that triggered this method (login button in LoginView)
	 */
	 public void login(View v) {
		if(D)
			Log.d(TAG, "Login button clicked");
		String email = emailEdit.getText().toString().trim();
		String password = passwordEdit.getText().toString().trim();
		this.loginController.handleLoginButtonClick(email, password);
	 }
	 
	 
	 public ImageView getLoginOverlay() {
			return loginOverlay;
		}

	 public ImageView getSignupOverlay() {
			return signupOverlay;
		}
}
