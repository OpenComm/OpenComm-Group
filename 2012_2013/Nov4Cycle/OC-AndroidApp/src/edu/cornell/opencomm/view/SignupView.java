package edu.cornell.opencomm.view;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;


/**
 * View for sign up screen. Functionality (handled by SignupController)
 * <ul>
 * <li>When the user has provided valid registration inputs, creates account to database, and 
 * email with new dummhy password sent.</li>
 * <li>When the user provides invalid inputs, returns toasts with errors.</li>
 * <li>When the user opts out by cancel button or back button, returns to previous activity.</li>
 * </ul>
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Ankit Singh[frontend], Risa Naka[frontend]
 * */
public class SignupView extends Activity {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	private static final String TAG = SignupView.class.getSimpleName();
	protected static final int ACTIVITY_SELECT_IMAGE = 1000;
	// TODO [frontend/Ankit] what is the purpose of REQ_CODE_PICK_IMAGE?
	private static final int REQ_CODE_PICK_IMAGE = 9999;

	private static final int SELECT_PHOTO = 100;

	private EditText firstNameInput;
	private EditText lastNameInput;
	private EditText emailInput;
	private EditText titleInput;
	private EditText confirmPwdInput;
	private EditText pwdInput;
	
	private ImageView acceptOverlay;
	private ImageView cancelOverlay;

	/**
	 * /** The controller for signup
	 */
	private SignupController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		FontSetter.applySanSerifFont(SignupView.this,findViewById(R.id.signup_layout));
		controller = new SignupController(this);
		this.firstNameInput = (EditText) findViewById(R.id.signup_firstNameInput);
		this.lastNameInput = (EditText) findViewById(R.id.signup_lastNameInput);
		this.emailInput = (EditText) findViewById(R.id.signup_emailInput);
		this.titleInput = (EditText) findViewById(R.id.signup_titleInput);
		this.pwdInput = (EditText) findViewById(R.id.signup_pwdInput);
		this.confirmPwdInput = (EditText) findViewById(R.id.signup_confirmPwdInput);
		this.acceptOverlay = (ImageView) findViewById(R.id.signup_acceptOverlay);
		this.cancelOverlay = (ImageView) findViewById(R.id.signup_cancelOverlay);
	}

	/** = password input */
	public EditText getPasswordTextBox() {
		return this.pwdInput;
	}

	/** = confirmPassword input */
	public EditText getConfirmPasswordTextBox() {
		return this.confirmPwdInput;
	}

	/** = first name input */
	public EditText getFirstNameTextBox() {
		return this.firstNameInput;
	}

	/** = last name input */
	public EditText getLastNameTextBox() {
		return this.lastNameInput;
	}

	/** = email input */
	public EditText getEmailTextBox() {
		return this.emailInput;
	}

	/** = title input */
	public EditText getTitleTextBox() {
		return this.titleInput;
	}

	/** Called when save is clicked: checks if the inputs are all valid, and if they are not, 
	 * generates a toast with the errors. If the inputs are all valid, registers account w/ server 
	 */
	public void save(View v) {
		controller.handleSave(this.firstNameInput.getText().toString().trim(),
				this.lastNameInput.getText().toString().trim(), this.emailInput
						.getText().toString().trim(), this.titleInput.getText()
						.toString().trim(), this.pwdInput.getText().toString()
						.trim(), this.confirmPwdInput.getText().toString()
						.trim());
	}

	/** Called when cancel is clicked: return to the previous page 
	 */
	public void cancel(View v) {
		this.cancelOverlay.setVisibility(View.VISIBLE);
		super.onBackPressed();
	}

	/** Chooses a photo to use as a profile
	 * TODO [frontend/Ankit] - currently not being sent to the server
	 * TODO [frontend/Ankit] - handlePhotoButtonClick is an empty method*/
	public void choosePhoto(View v) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
		controller.handlePhotoButtonClick(v);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.acceptOverlay.setVisibility(View.INVISIBLE);
		this.cancelOverlay.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				try {
					imageStream = getContentResolver().openInputStream(
							selectedImage);

					Bitmap yourSelectedImage = BitmapFactory
							.decodeStream(imageStream);
					ImageView photo = (ImageView) findViewById(R.id.signup_photoInput);
					photo.setImageBitmap(yourSelectedImage);
					photo.invalidate();
					if (D) Log.d(TAG, "Photo imported");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
