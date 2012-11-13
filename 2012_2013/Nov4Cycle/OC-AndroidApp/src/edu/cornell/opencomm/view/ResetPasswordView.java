package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = ResetPasswordView.class.getSimpleName();
	private ResetPasswordController controller; 
	private EditText emailEntered;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_layout);
		this.emailEntered = (EditText) findViewById(R.id.emailTextBox);
		FontSetter.applySanSerifFont(ResetPasswordView.this, findViewById(R.id.reset_password_layout)); 		
		controller = new ResetPasswordController(ResetPasswordView.this, this); 
	}

	/** 
	 * When sign up button is pressed, launch signup page 
	 * */
	public void signUpPressed(View v){
		this.controller.signUpPressed();
	}

	/** 
	 * When reset password has been clicked - shows a popup for unregistered/
	 * invalid emails 
	 * */
	public void resetPassword(View v){
		Log.v("Reset password page", "Reset Clicked");
		this.controller.resetPasswordPressed(this.emailEntered.getText().toString());		
	}

	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.signupOverlayReset).setVisibility(View.INVISIBLE);
		findViewById(R.id.resetPasswordOverlay).setVisibility(View.INVISIBLE);
		this.emailEntered.setText("");
	}
	
	@Override
	public void onBackPressed() {
		this.controller.onBackPressed();
	}

}
