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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;

/** TODO add class comment -- see loginView, LoginController*/
public class SignupView extends Activity{
	// TODO add debug variable
	// TODO alter comment
	private static final String TAG = SignupView.class.getName();
	protected static final int ACTIVITY_SELECT_IMAGE = 1000;
	private static final int REQ_CODE_PICK_IMAGE = 9999;
	/**
	/**
	 * The controller for signup
	 */
	private SignupController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_view_layout);
		FontSetter.applySanSerifFont(SignupView.this, findViewById(R.id.signup_view_layout));
		controller = new SignupController(this,SignupView.this);
	}

	/**
	 * Initialize the signup view
	 * TODO why not override onResume?
	 */



	private static final int SELECT_PHOTO = 100;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		switch(requestCode) { 
		case SELECT_PHOTO:
			if(resultCode == RESULT_OK){  
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				try {
					imageStream =  getContentResolver().openInputStream(selectedImage);

					Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
					ImageView photo = (ImageView) findViewById(R.id.photoButton);
					//					yourSelectedImage.
					photo.setImageBitmap(yourSelectedImage);
					photo.invalidate();
					Log.d(TAG,"Thing workded");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * initialize photo button click event listener
	 */
	public void addPhoto(View v){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);  

		controller.handlePhotoButtonClick(v);

	}

	public void createAccount(View v){
		controller.handleSave();
	}
	public void launchPreviousActivity(View v){
		super.onBackPressed();
	}
	public View getPasswordTextBox(){
		EditText passwordTextBox = (EditText) findViewById(R.id.passwordBox);
		return passwordTextBox;
	}
	public View getConfirmPasswordTextBox(){
		EditText cnfPasswordTextBox = (EditText) findViewById(R.id.confirmPasswordBox);
		return cnfPasswordTextBox;
	}
	public View getFirstNameTextBox(){
		EditText firstName = (EditText) findViewById(R.id.firstNameBox);
		return firstName;
	}
	public View getLastNameTextBox(){
		EditText textBox = (EditText) findViewById(R.id.lastNameBox);
		return textBox;
	}
	public View getEmailTextBox(){
		EditText textBox = (EditText) findViewById(R.id.emailBox);
		return textBox;
	}
	public View getTitleTextBox(){
		EditText textBox = (EditText) findViewById(R.id.titleBox);
		return textBox;
	}
	@Override
	public void onBackPressed() {
		// Consume the key like a Boss!
	}
	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.acceptSignupOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.cancelSignupOverlay).setVisibility(View.INVISIBLE);
	}

	public void resetFocus(View view) {
		if(view != null){
		view.requestFocus();
		}
	}
}
