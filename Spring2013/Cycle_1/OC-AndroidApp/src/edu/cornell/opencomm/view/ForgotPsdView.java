package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;

import edu.cornell.opencomm.controller.ForgotPsdController;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
  * View for forgot password screen. 
  * @author Lu Yang[frontend]
  */

public class ForgotPsdView extends Activity {
	
	private static final View ImageView = null;
	private ForgotPsdController forgotPsdController;
	private EditText emailEdit;
	private ImageView sendEmail;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		emailEdit= (EditText) findViewById(R.id.email_input);
		sendEmail = (ImageView) findViewById(R.id.send_email_button);
		forgotPsdController=new ForgotPsdController(this);
	}
	
	public void sendEmail(View v){
		Button button = (Button)findViewById(R.id.send_email_button);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        // Do something in response to button click
		    }
		});		
	}
}
