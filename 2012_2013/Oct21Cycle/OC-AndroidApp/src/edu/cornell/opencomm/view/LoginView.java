package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.ResetPasswordController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * View for login screen.
 * Functionality (handled by LoginController)
 * <ul>
 * <li>When the user has forgotten a password, it launches ResetPasswordView.</li>
 * <li>When the user wants to sign up, it launches SignupView.</li>
 * <li>When the user attempts to log in, checks for valid inputs, and then 
 * attempts to log in, and if successful, launches DashboardView</li>
 * </ul>
 *
 * Issues [TODO]
 * - For any other issues search for string "TODO"
 *
 * @author Heming Ge[frontend], Ankit Singh[frontend]
 * */
public class LoginView extends Activity {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	private static final String TAG = LoginView.class.getSimpleName();
	
	private LoginController loginController = null;
	
	private static EditText emailEdit;
	private static EditText passwordEdit;
	private static ImageButton loginButton;
	@SuppressWarnings("unused")
	private static Button loginText;
	private static ImageView loginOverlay;
	private LayoutInflater inflater = null;
	private static ImageView signupOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        FontSetter.applySanSerifFont(this, findViewById(R.id.login_layout));
        emailEdit = (EditText) findViewById(R.id.editTextEmail);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (Button) findViewById(R.id.loginText);
		loginButton = (ImageButton) findViewById(R.id.loginButton);
		loginOverlay = (ImageView) findViewById(R.id.loginOverlay);
		signupOverlay = (ImageView) findViewById(R.id.signupOverlay);
        this.inflater = this.getLayoutInflater();
		loginController = new LoginController(this);
		initEmailFocusChangelistener();
    }
    /**
	 * Initialize email box
	 */
	private void initEmailFocusChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				loginController.handleEmailFocusChange(view, hasFocus);
			}
		};
		emailEdit.setOnFocusChangeListener(listener);
	}
	public ImageView getLoginOverlay() {
		return loginOverlay;
	}
	public ImageView getSignupOverlay() {
		return signupOverlay;
	}
	public ImageButton getLoginButton() {
		return loginButton;
	}
	public LayoutInflater getInflater() {
        return inflater;
    }
	public void resetFocus(){
		emailEdit.requestFocus();
	}
	 /**Jump to the account creation page when sign-up button is clicked*/
    public void createAccount(View v){
    	if (D) Log.d(TAG, "create Account");
    	this.loginController.handleCreateAccount();
    }
    /**Jump to the Reset Password page when forgot-password is clicked*/
    public void retrievePassword(View v){
    	Log.v(TAG, "retrievePassword");
    	this.loginController.handleRetrievePassword();
    }
    
    public void login(View v){
    	String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
    	this.loginController.handleLoginButtonClick(email, password);
    }
    
    @Override
    public void onBackPressed() {
    	// back button disabled
    }
    @Override
    public void onResume() {
    	super.onResume();
    	this.getLoginOverlay().setVisibility(View.INVISIBLE);
    	this.getSignupOverlay().setVisibility(View.INVISIBLE);
    	emailEdit.setText("");
    	passwordEdit.setText("");
    	Intent i = this.getIntent();
    	// show tip saying that a random password has been generated and sent as an email.
    	boolean isPwdReset = i.getBooleanExtra(ResetPasswordController.PWDRESET, false);
    	if (isPwdReset) {
    		// TODO generate tip view notifying password sent to email
    		int duration = Toast.LENGTH_SHORT;
        	Toast send = Toast.makeText(getApplicationContext(),getResources().getString(R.string.resetNotify),duration);
        	send.show();
    	}
    }
}

