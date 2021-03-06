package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.ResetPasswordController;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * View for login screen. Functionality (handled by LoginController)
 * <ul>
 * <li>When the user has forgotten a password, it launches ResetPasswordView.</li>
 * <li>When the user wants to sign up, it launches SignupView.</li>
 * <li>When the user attempts to log in, checks for valid inputs, and then
 * attempts to log in, and if successful, launches DashboardView</li>
 * </ul>
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Heming Ge[frontend], Ankit Singh[frontend]
 * */
public class LoginView extends Activity {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	private static final boolean D = true;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.login_layout));
		emailEdit = (EditText) findViewById(R.id.login_emailInput);
		passwordEdit = (EditText) findViewById(R.id.login_pwdInput);
		loginOverlay = (ImageView) findViewById(R.id.login_loginOverlay);
		signupOverlay = (ImageView) findViewById(R.id.login_signupOverlay);
	}

	public ImageView getLoginOverlay() {
		return loginOverlay;
	}

	public ImageView getSignupOverlay() {
		return signupOverlay;
	}

	public void resetFocus() {
		emailEdit.requestFocus();
	}

	/** Jump to the account creation page when sign-up button is clicked */
	public void signup(View v) {
		if (D)
			Log.d(TAG, "Sign Up button clicked");
		this.loginController.handleSignup();
	}

	/** Jump to the Reset Password page when forgot-password is clicked */
	public void forgotPassword(View v) {
		if (D)
			Log.d(TAG, "Forgot Password link clicked");
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
		String email = emailEdit.getText().toString().trim();
		String password = passwordEdit.getText().toString().trim();
		this.loginController.handleLoginButtonClick(email, password);
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}

	@Override
	public void onResume() {
		super.onResume();
		// set overlays as invisible
		this.loginOverlay.setVisibility(View.INVISIBLE);
		this.signupOverlay.setVisibility(View.INVISIBLE);
		// reset email and password edits
		this.emailEdit.setText("");
		this.passwordEdit.setText("");
		Intent i = this.getIntent();
		// show toast saying that a random password has been generated and sent
		// as an email.
		boolean isPwdReset = i.getBooleanExtra(
				ResetPasswordController.PWDRESET, false);
		if (isPwdReset) {
			int duration = Toast.LENGTH_SHORT;
			Toast send = Toast.makeText(getApplicationContext(), getResources()
					.getString(R.string.resetNotify), duration);
			send.show();
		}
	}
}
