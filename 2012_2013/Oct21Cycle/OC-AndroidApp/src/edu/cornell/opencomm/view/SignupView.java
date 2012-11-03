package edu.cornell.opencomm.view;

import org.jivesoftware.smack.util.Base64.InputStream;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.SignupController;

public class SignupView extends Activity{
	private static final String TAG = SignupView.class.getName();
	protected static final int ACTIVITY_SELECT_IMAGE = 1000;
	private static final int REQ_CODE_PICK_IMAGE = 9999;
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
		FontSetter.applySanSerifFont(SignupView.this, findViewById(R.id.signup_view_layout));
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
		initTitleFocusChangelistener();
		initPasswordChangelistener();
		initConfirmPasswordChangelistener();
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
	 * Initialize title box
	 */
	private void initTitleFocusChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleTitleFocusChange(view, hasFocus);
			}
		};
		View title = findViewById(R.id.titleBox);
		title.setOnFocusChangeListener(listener);
	}
	/**
	 * Initialize password box
	 */
	private void initPasswordChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handlePasswordFocusChange(view, hasFocus);
			}
		};
		View password = findViewById(R.id.passwordBox);
		password.setOnFocusChangeListener(listener);
	}/**
	 * Initialize title box
	 */
	private void initConfirmPasswordChangelistener() {
		OnFocusChangeListener listener = new View.OnFocusChangeListener() {
			public void onFocusChange(View view, boolean hasFocus) {
				controller.handleConfirmPasswordFocusChange(view, hasFocus);
			}
		};
		View confrimPassword = findViewById(R.id.confirmPasswordBox);
		confrimPassword.setOnFocusChangeListener(listener);
	}



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
					imageStream = getContentResolver().openInputStream(selectedImage);
					
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
	private void initPhotoButtonClickEvent() {
		OnClickListener listener = new View.OnClickListener() {

			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);  
				
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
		view.requestFocus();
		
	}
}
