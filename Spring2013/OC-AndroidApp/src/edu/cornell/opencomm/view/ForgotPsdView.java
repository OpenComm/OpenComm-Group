package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.ForgotPsdController;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
  * View for forgot password screen. 
  * @author Lu Yang[frontend]
  */

public class ForgotPsdView extends Activity {
	

	private EditText emailEdit;	
		
	/** The controller for forgot password screen */
	private ForgotPsdController forgotPsdController;
	

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		forgotPsdController=new ForgotPsdController(this);
		emailEdit= (EditText) findViewById(R.id.email_input);
		//set font
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		emailEdit.setTypeface(tf);
	}
	
	/** Email Input */
	public EditText getEmail(){
		return this.emailEdit;
	}
	
	/** Called when email button is pressed */
	public void sendEmailPressed(View v){
		       forgotPsdController.handleSendEmailClick(getEmail().getText().toString());	
	}
}
