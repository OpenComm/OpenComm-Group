package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.LoginController;

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        emailEdit = (EditText) findViewById(R.id.editTextEmail);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (Button) findViewById(R.id.loginText);
		loginButton = (ImageButton) findViewById(R.id.loginButton);
		loginOverlay = (ImageView) findViewById(R.id.loginOverlay);
		signupOverlay = (ImageView) findViewById(R.id.signupOverlay);
        this.inflater = this.getLayoutInflater();
		
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
    	this.getSignupOverlay().setVisibility(View.VISIBLE);
    	Intent account = new Intent(this,SignupView.class);
    	startActivity(account);
    }
    /**Jump to the Reset Password page when forgot-password is clicked*/
    public void retrievePassword(View v){
    	Log.v(TAG, "retrievePassword");
    	Intent account = new Intent(this,ResetPasswordView.class);
    	startActivity(account);
    }
    
    public void login(View v){
    	this.loginController.handleLoginButtonClick(emailEdit, passwordEdit);
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
    }
}

