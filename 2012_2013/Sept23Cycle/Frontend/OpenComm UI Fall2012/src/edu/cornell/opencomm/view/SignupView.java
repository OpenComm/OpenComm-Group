package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;

public class SignupView extends Activity{
	/**
	/**
	 * The controller for signup
	 */
	private SignupController controller;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_view_layout);
		FontSetter.applySanSerifFont(SignupView.this, findViewById(R.layout.signup_view_layout));
		controller = new SignupController(this,SignupView.this);
		init();
	}

	/**
	 * Initialize the signup view
	 * TODO why not override onResume?
	 */
	private void init() {
		initFirstNameFocusChangelistener();
		initLastNameFocusChangelistener();
		initEmailFocusChangelistener();
		initPhotoButtonClickEvent();
		initSaveButtonClickEvent();
		initSaveImageClickEvent();
		initCancelButtonClickEvent();
		initCancelImageClickEvent();
	}

	/**
	 * Initialize the FirstName box
	 */
	private void initFirstNameFocusChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleFirstNameFocusChange(view, hasFocus);
			}
		};
		View firstNameBox = findViewById(R.id.firstNameBox);
		firstNameBox.setOnFocusChangeListener(listener);
	}

	/**
	 * Initialize the Last Name box
	 */
	private void initLastNameFocusChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleLastNameFocusChange(view, hasFocus);
			}
		};
		View lastName = findViewById(R.id.lastNameBox);
		lastName.setOnFocusChangeListener(listener);
	}

	/**
	 * Initialize email box
	 */
	private void initEmailFocusChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleEmailFocusChange(view, hasFocus);
			}
		};
		View email = findViewById(R.id.emailBox);
		email.setOnFocusChangeListener(listener);
	}




	/**
	 * initialize photo button click event listener
	 */
	private void initPhotoButtonClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handlePhotoButtonClick();

			}
		};
		View photoButton = findViewById(R.id.photoButton);
		photoButton.setOnClickListener(listener);
	}

	/**
	 * initialize save button click event listener
	 */
	private void initSaveButtonClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleSave();

			}
		};
		View saveButton = findViewById(R.id.saveButton);
		saveButton.setOnClickListener(listener);
	}

	/**
	 * initialize save image click event listener
	 */
	private void initSaveImageClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleSave();

			}
		};
		View saveButton = findViewById(R.id.saveImage);
		saveButton.setOnClickListener(listener);
	}

	/**
	 * initialize save button click event listener
	 */
	private void initCancelButtonClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleCancel();

			}
		};
		View saveButton = findViewById(R.id.cancelButton);
		saveButton.setOnClickListener(listener);
	}

	/**
	 * initialize save image click event listener
	 */
	private void initCancelImageClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleCancel();

			}
		};
		View saveButton = findViewById(R.id.cancelImage);
		saveButton.setOnClickListener(listener);
	}
	public String getPasswordText(){
		EditText passwordTextBox = (EditText) findViewById(R.id.passwordBox);
		return passwordTextBox.getText().toString();
	}
	public String getConfirmPasswordText(){
		EditText cnfPasswordTextBox = (EditText) findViewById(R.id.confirmPasswordBox);
		return cnfPasswordTextBox.getText().toString();
	}
	public String getFirstNameText(){
		EditText firstName = (EditText) findViewById(R.id.firstNameBox);
		return firstName.getText().toString();
	}
	public String getLastNameText(){
		EditText textBox = (EditText) findViewById(R.id.lastNameBox);
		return textBox.getText().toString();
	}
	public String getEmailText(){
		EditText textBox = (EditText) findViewById(R.id.emailBox);
		return textBox.getText().toString();
	}
	public String getTitleText(){
		EditText textBox = (EditText) findViewById(R.id.titleBox);
		return textBox.getText().toString();
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		//AS: Consume the back for now, we need to use the menu key later
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return super.dispatchKeyEvent(event);
		}
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.acceptSignupOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.cancelSignupOverlay).setVisibility(View.INVISIBLE);
	}
}
