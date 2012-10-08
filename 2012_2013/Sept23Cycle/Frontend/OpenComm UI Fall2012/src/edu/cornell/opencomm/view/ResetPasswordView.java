package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.ResetPasswordController;

public class ResetPasswordView extends Activity {

	private ResetPasswordController controller; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password_layout); 
		FontSetter.applySanSerifFont(ResetPasswordView.this, findViewById(R.layout.reset_password_layout)); 
		makeItLookBeautiful();  
		controller = new ResetPasswordController(ResetPasswordView.this, this); 
	}

	//Applies default theme to the view including the new font
	private void makeItLookBeautiful(){		
		initializeEmailText();
		initializeReset(); 
		initializeSignUp(); 		
	}
	
	//initializes the email text box where the user enters email address
	private void initializeEmailText(){
		final EditText emailText = (EditText) findViewById(R.id.emailTextBox); 
		emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					//wait till the user completely enters the email
				}
				if (!hasFocus){
					//TODO - Send to the controller to see if the email entered is valid or not
					//if valid then - may be you can display a check mark (?) - Ask design team
					//if not valid - a cross
					//check the network to see if the email is associated or not
					controller.findEmail(emailText.getText()); 
				}					
			}
		});
	}

	//Initializes the reset button. When pressed gives a toast
	private void initializeReset(){
		final ImageView resetPass = (ImageView) findViewById(R.id.resetButtonImage);
		resetPass.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				// send to resetPasswordController.resetPasswordPressed(); 

				//for now make a toast
				Context context = getApplicationContext();
				CharSequence text = "Must send dummy password to email!";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.setMargin(MODE_APPEND, CONTEXT_RESTRICTED); 
				toast.show();

			}
		});
	}

	private void initializeSignUp() {
		final TextView signup = (TextView) findViewById(R.id.signUpText); 
		signup.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				//TODO
				//1. Send to resetPasswordController.resetPasswordPressed(); 

				//for now make a toast
				Context context = getApplicationContext();
				CharSequence text = "Go to Sign up page!";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});

	}

	//TODO
	//1. Implement the reset password button - send the control to the controller
	//2. Sign up button - implement onClick functionality- send to sign up page

}
