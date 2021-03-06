package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.CreateAccountController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;
/*
 * Author: Spandana Govindgari
 * 
 */
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

	/**
	 * /** The controller for signup
	 */
	private SignupController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_account);
		FontSetter.applySanSerifFont(this,findViewById(R.id.create_account));
		controller = new SignupController(this);
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
		String[] name = this.getFirstNameTextBox().getText().toString().split(" ");
		String firstName = name[0];
		String lastName = name[1];
		this.controller.handleSave(firstName, lastName, this.emailInput.getText().toString(), 
				this.usernameInput.getText().toString(), "",
				this.pwdInput.getText().toString(), this.pwdInput.getText().toString()); 
	}

}
