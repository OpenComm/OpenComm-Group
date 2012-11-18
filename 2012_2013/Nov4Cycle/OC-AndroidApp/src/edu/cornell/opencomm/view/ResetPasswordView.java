package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.ResetPasswordController;

/**
 * View for reset password screen.
 * Functionality (handled by ResetPasswordController)
 * <ul>
 * <li>When the user inputs an email, checks for a valid email, and sends 
 * an reset password email</li>
 * <li>When the user opts to sign up instead, launches SignupView</li>
 * </ul>
 *
 * Issues [TODO]
 * - For any other issues search for string "TODO"
 *
 * @author Spandana Govindgari[frontend]
 * */
public class ResetPasswordView extends Activity {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	private static final String TAG = ResetPasswordView.class.getSimpleName();
	private ResetPasswordController controller; 
	private EditText emailEntered;
	private ImageView signupOverlay;
	private ImageView resetPwdOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_layout);
		this.emailEntered = (EditText) findViewById(R.id.emailTextBox);
		this.resetPwdOverlay = (ImageView) findViewById(R.id.resetPasswordOverlay);
		this.signupOverlay = (ImageView) findViewById(R.id.resetPwd_SignupOverlay);
		FontSetter.applySanSerifFont(ResetPasswordView.this, findViewById(R.id.reset_password_layout)); 		
		controller = new ResetPasswordController(ResetPasswordView.this, this); 
	}

	/** 
	 * When sign up button is pressed, launch signup page 
	 * */
	public void signup(View v){
		this.controller.signUpPressed();
	}
	
	/**
	 * = this view's signup overlay
	 */
	public ImageView getSignupOverlay() {
		return this.signupOverlay;
	}
	
	/**
	 * = this view's signup overlay
	 */
	public ImageView getResetPwdOverlay() {
		return this.resetPwdOverlay;
	}

	/** 
	 * When reset password has been clicked - shows a popup for unregistered/
	 * invalid emails 
	 * */
	public void resetPassword(View v){
		if (D) Log.d(TAG, "Reset Clicked");
		this.controller.resetPasswordPressed(this.emailEntered.getText().toString().trim());		
	}

	@Override
	public void onResume() {
		super.onResume();
		this.signupOverlay.setVisibility(View.INVISIBLE);
		this.resetPwdOverlay.setVisibility(View.INVISIBLE);
		this.emailEntered.setText("");
	}
	
	@Override
	public void onBackPressed() {
		this.controller.onBackPressed();
	}

}
