package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.ResetPasswordController;

public class ResetPasswordView extends Activity {
	//For debugging purposes
	private static String TAG = ResetPasswordView.class.getSimpleName();	
	private static final boolean D = true; 
	private ResetPasswordController controller; 
	private LayoutInflater inflater = null; 
	private EditText emailEntered = (EditText) findViewById(R.id.emailTextBox);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_layout);
		this.inflater = this.getLayoutInflater(); 
		FontSetter.applySanSerifFont(ResetPasswordView.this, findViewById(R.id.reset_password_layout)); 		
		controller = new ResetPasswordController(ResetPasswordView.this, this); 
	}

	//When sign up is pressed
	public void signUpPressed(View v){
		this.controller.signUpPressed();
	}

	//When reset password has been clicked - shows a toast
	public void goToReset(View v){
		Log.v("Reset password page", "Reset Clicked");
		this.controller.resetPasswordPressed(emailEntered);		
	}

	public EditText getEmailEntered(){
		return this.emailEntered;
	}

	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.signupOverlayReset).setVisibility(View.INVISIBLE);
		findViewById(R.id.resetPasswordOverlay).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		this.controller.onBackPressed();
	}
	
	public void throwToast(int i){
		Context context = getApplicationContext();
		if (i == -1){
			CharSequence text = "Email not valid! Enter a valid email address";	
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			//make the edit text empty
			emailEntered.setText(null);
			
		}
		if (i == 0){
			CharSequence text = "Email not found in network! Please sign up";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			this.controller.signUpPressed();
		}
	}

}
