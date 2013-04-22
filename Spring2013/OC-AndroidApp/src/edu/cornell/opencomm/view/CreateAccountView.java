package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.CreateAccountController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.util.Util;

/**
 * @author Spandana Govindgari
 * Last Edited: Antoine Chkaiban (back-end) last edit 04/22/2013
 */
public class CreateAccountView extends Activity {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = CreateAccountView.class.getSimpleName();
	private EditText nameInput;
	private EditText emailInput;
	private EditText usernameInput;
	private EditText pwdInput;
	
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
		//get fields
		String name = nameInput.getText().toString();
		String username = usernameInput.getText().toString();
		String email = emailInput.getText().toString();
		String pwd = pwdInput.getText().toString();
		
		// check validity of input
		boolean emptyName = (name == null || name.length() == 0);
		boolean invalidName = !Util.validateString(name, Util.NAME_PATTERN_SAVE);
		boolean emptyEmail = (email == null || email.length() == 0);
		boolean invalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean emptyUsername = (username == null || username.length() == 0);
		boolean invalidUsername = !Util.validateString(username, Util.USERNAME);
		boolean emptyPwd = (pwd == null || pwd.length() == 0);
		boolean invalidPwd = !Util.validateString(pwd, Util.PASSWORD);
		if (emptyName || invalidName || emptyEmail || emptyUsername || invalidUsername || invalidEmail || emptyPwd || invalidPwd) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (emptyName) errorText.append("\tName is required\n");
			else if (invalidName) errorText.append("\tInvalid name\n");
			if (emptyEmail) errorText.append("\tEmail is required\n");
			else if (invalidEmail) errorText.append("\tInvalid email\n");
			if (emptyPwd) errorText.append("\tPassword is required\n");
			else if (invalidPwd) errorText.append("\tInvalid password (req: 10 - 30 chars)\n");
			if (emptyUsername) errorText.append("\tUsername is required\n");
			else if (invalidUsername) errorText.append("\tInvalid username\n");
			errorText.append("Please try again.");
					
			// show a toast describing the error
			Toast.makeText(getApplicationContext(), errorText.toString(), Toast.LENGTH_SHORT).show();
		} else {
			String[] firstLastName = name.split(" ");
			this.controller.handleSave(firstLastName[0], firstLastName[0], emailInput.getText().toString(), usernameInput.getText().toString(), pwdInput.getText().toString());
		}
	}

}
