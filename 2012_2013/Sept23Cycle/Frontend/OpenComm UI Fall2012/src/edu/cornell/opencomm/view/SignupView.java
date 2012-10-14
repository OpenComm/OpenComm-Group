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
	 */
	private void init() {
		initFirstNameFocusChangeListner();
		initLastNameFocusChangeListner();
		initEmailFocusChangeListner();
		initPhotoButtonClickEvent();
		initSaveButtonClickEvent();
		initSaveImageClickEvent();
		initCancelButtonClickEvent();
		initCancelImageClickEvent();
	}

	/**
	 * Initialize the FirstName box
	 */
	private void initFirstNameFocusChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleFirstNameFocusChange(view, hasFocus);
			}
		};
		View firstNameBox = findViewById(R.id.firstNameBox);
		firstNameBox.setOnFocusChangeListener(listner);
	}

	/**
	 * Initialize the Last Name box
	 */
	private void initLastNameFocusChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleLastNameFocusChange(view, hasFocus);
			}
		};
		View lastName = findViewById(R.id.lastNameBox);
		lastName.setOnFocusChangeListener(listner);
	}

	/**
	 * Initialize email box
	 */
	private void initEmailFocusChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleEmailFoucsChange(view, hasFocus);
			}
		};
		View email = findViewById(R.id.emailBox);
		email.setOnFocusChangeListener(listner);
	}




	/**
	 * initialize photo button click event listner
	 */
	private void initPhotoButtonClickEvent() {
		OnClickListener listner = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handlePhotoButtonClick();

			}
		};
		View photoButton = findViewById(R.id.photoButton);
		photoButton.setOnClickListener(listner);
	}

	/**
	 * initialize save button click event listner
	 */
	private void initSaveButtonClickEvent() {
		OnClickListener listner = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleSave();

			}
		};
		View saveButton = findViewById(R.id.saveButton);
		saveButton.setOnClickListener(listner);
	}

	/**
	 * initialize save image click event listner
	 */
	private void initSaveImageClickEvent() {
		OnClickListener listner = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleSave();

			}
		};
		View saveButton = findViewById(R.id.saveImage);
		saveButton.setOnClickListener(listner);
	}

	/**
	 * initialize save button click event listner
	 */
	private void initCancelButtonClickEvent() {
		OnClickListener listner = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleCancel();

			}
		};
		View saveButton = findViewById(R.id.cancelButton);
		saveButton.setOnClickListener(listner);
	}

	/**
	 * initialize save image click event listner
	 */
	private void initCancelImageClickEvent() {
		OnClickListener listner = new View.OnClickListener() {

			public void onClick(View v) {
				controller.handleCancel();

			}
		};
		View saveButton = findViewById(R.id.cancelImage);
		saveButton.setOnClickListener(listner);
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
}
