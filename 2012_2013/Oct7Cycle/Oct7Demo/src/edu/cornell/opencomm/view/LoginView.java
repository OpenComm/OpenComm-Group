package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.ResetPasswordController;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class LoginView extends Activity {
	/**
	 * The TAG for logging
	 */
	private static final String TAG = LoginView.class.getSimpleName();
	private static final boolean D = true;
	
	private LoginController loginController = null;
	
	private static EditText emailEdit;
	private static EditText passwordEdit;
	private static ImageButton loginButton;
	private static Button loginText;
	private static ImageView loginOverlay;
	private LayoutInflater inflater = null;
	private static ImageView signupOverlay;
	private static String email;
	private static String password;
	
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
		
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
//		initializeLoginButtonClickedEvent();
		loginController = new LoginController(this);
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
