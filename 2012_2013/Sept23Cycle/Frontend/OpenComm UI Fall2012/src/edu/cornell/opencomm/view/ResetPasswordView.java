package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.ResetPasswordController;

public class ResetPasswordView extends Activity {
	//For debugging purposes
	private static String TAG = "Reset Password View"; 
	private static final boolean D = true; 
	private ResetPasswordController controller; 
	private LayoutInflater inflater = null; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_layout);
		this.inflater = this.getLayoutInflater(); 
		FontSetter.applySanSerifFont(ResetPasswordView.this, findViewById(R.layout.reset_password_layout)); 		
		controller = new ResetPasswordController(ResetPasswordView.this, this); 
	}
	
	//When sign up is pressed
	public void signUpPressed(View v){
		Log.v("Reset password page", "Going to sign up"); 
		Intent click = new Intent(this,SignupView.class);
    	startActivity(click);
	}
	
	//When reset password has been clicked - shows a toast
	public void goToReset(View v){
		Log.v("Reset password page", "Reset Clicked"); 
		//TODO
		//1. Should contact network and send a dummy password to this user's email
		CharSequence text = "Must send a dummy password!";
    	int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(getApplicationContext(),text,duration);
    	send.show();
		
	}

}
