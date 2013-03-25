package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.CreateAccountController;
import edu.cornell.opencomm.controller.FontSetter;

public class CreateAccountView extends Activity {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	private static final String TAG = CreateAccountView.class.getSimpleName();
	private EditText nameInput;
	private EditText emailInput;
	private EditText usernameInput;
	private EditText pwdInput;
	
	private ImageView acceptOverlay;
	private ImageView cancelOverlay;

	/**
	 * /** The controller for signup
	 */
	private CreateAccountController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_account);
		FontSetter.applySanSerifFont(this,findViewById(R.id.create_account));
		controller = new CreateAccountController(this);
		this.nameInput = (EditText) findViewById(R.id.name_field);
		this.emailInput = (EditText) findViewById(R.id.email_field);
		this.usernameInput = (EditText) findViewById(R.id.username_field); 
		this.pwdInput = (EditText) findViewById(R.id.password_field);
		//this.acceptOverlay = (ImageView) findViewById(R.id.signup_acceptOverlay);
		//this.cancelOverlay = (ImageView) findViewById(R.id.signup_cancelOverlay);
	}

	/** = password input */
	public EditText getPasswordTextBox() {
		return this.pwdInput;
	}

	/** = first name input */
	public EditText getFirstNameTextBox() {
		return this.nameInput;
	}

	/** = email input */
	public EditText getEmailTextBox() {
		return this.emailInput;
	}

	/** = username input*/
	public EditText getUsernameTextBox() {
		return this.usernameInput;
	}
	/**
	 * TODO: check if inputs are valid when the sign up button is pressed
	 * */
	public void signUpPressed(View v){
		CharSequence text = "Go to sign up page";
		int duration = Toast.LENGTH_SHORT; 
		Toast send = Toast.makeText(getApplicationContext(),text,duration);
		send.show(); 
		this.controller.handleSignUp(); 
		//TODO: change to a different view after button pressed 
	}

}
