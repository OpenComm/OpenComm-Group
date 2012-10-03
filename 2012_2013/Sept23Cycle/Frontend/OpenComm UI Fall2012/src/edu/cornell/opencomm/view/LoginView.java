package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.LoginController;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.Activity;

public class LoginView extends Activity {
	private static ImageView loginOverlay;
	private LoginController loginController = null;
	
	private static EditText emailEdit;
	private static EditText passwordEdit;
	private static ImageButton loginButton;
	private static Button loginText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        
        emailEdit = (EditText) findViewById(R.id.editTextEmail);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (Button) findViewById(R.id.loginText);
		loginButton = (ImageButton) findViewById(R.id.loginButton);
		loginOverlay = (ImageView) findViewById(R.id.loginOverlay);
		
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
	public ImageButton getLoginButton() {
		return loginButton;
	}
	private View.OnClickListener onLoginButtonClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			loginController.handleLoginButtonClick(emailEdit, passwordEdit);
		}
	};
    //TODO Implement the the following functionality
    //1. Handle Login button- Delegate the control to controller
    //2. Handle reset password- Launch reset password activity 
    //3. Handle signup - launch the signup activity
}

