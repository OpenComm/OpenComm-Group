package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

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
        setContentView(R.layout.activity_login_view);
        this.inflater = this.getLayoutInflater();
        emailEdit = (EditText) findViewById(R.id.editTextEmail);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (Button) findViewById(R.id.loginText);
		loginButton = (ImageButton) findViewById(R.id.loginButton);
		loginOverlay = (ImageView) findViewById(R.id.loginOverlay);
		signupOverlay = (ImageView) findViewById(R.id.loginOverlay);
		
		initializeLoginButtonClickedEvent();
		loginController = new LoginController(this);
    }

    private void initializeLoginButtonClickedEvent() {
		ImageButton loginButton = getLoginButton();
		if (loginButton != null) {
			loginButton.setOnClickListener(onLoginButtonClickedListener);
		}
		if (loginText != null) {
			loginText.setOnClickListener(onLoginButtonClickedListener);
		}
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
	
	private View.OnClickListener onLoginButtonClickedListener = new View.OnClickListener() {

		public void onClick(View v) {
			loginController.handleLoginButtonClick(emailEdit, passwordEdit);
		}
	};
	 /**Jump to the account creation page when sign-up button is clicked*/
    public void createAccount(View v){
    	Log.v("Crystal", "create Account");
    	this.getSignupOverlay().setVisibility(View.VISIBLE);
    	Intent account = new Intent(this,SignupView.class);
    	startActivity(account);
    }
    /**Jump to the Reset Password page when forgot-password is clicked*/
    public void retrievePassword(View v){
    	Log.v("Crystal", "retrievePassword");
    	Intent account = new Intent(this,ResetPasswordView.class);
    	startActivity(account);
    }
    
    public void login(View v){
    	CharSequence text = "Go to dashboard";
    	int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(getApplicationContext(),text,duration);
    	send.show();
    }
}

