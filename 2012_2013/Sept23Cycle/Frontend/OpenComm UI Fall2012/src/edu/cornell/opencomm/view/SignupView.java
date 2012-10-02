package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;

public class SignupView extends Activity {
	/**
	 * The constant for save click
	 */
	public static final int SAVE_CLICK = 1;
	/**
	 * The constant for cancel click
	 */
	public static final int CANCEL_CLICK = 2;
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
		setContentView(R.layout.activity_signup_view);
		FontSetter.applySanSerifFont(SignupView.this, findViewById(R.layout.activity_signup_view));
		init();
	}

	/**
	 * Initialize the signup view
	 */
	private void init() {
		initFirstNameFocusChangeListner();
		initLastNameFocusChangeListner();
		initPasswordChangeListner();
		initConfirmPasswordChangeListner();
		initEmailFocusChangeListner();
		initTitleFocusChangeListner();
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
	 * Initialize the Password box
	 */
	private void initPasswordChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handlePasswordFocusChange(view, hasFocus);
			}
		};
		View password = findViewById(R.id.passwordBox);
		password.setOnFocusChangeListener(listner);
	}

	/**
	 * Initialize the confirm password box
	 */
	private void initConfirmPasswordChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleConfirmPasswordFocusChange(view, hasFocus);
			}
		};
		View confirmPassword = findViewById(R.id.confirmPassword);
		confirmPassword.setOnFocusChangeListener(listner);
	}

	/**
	 * Initialize the title box
	 */
	private void initTitleFocusChangeListner() {
		OnFocusChangeListener listner = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleTitleFocusChange(view, hasFocus);
			}
		};
		View title = findViewById(R.id.titleBox);
		title.setOnFocusChangeListener(listner);
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

}
