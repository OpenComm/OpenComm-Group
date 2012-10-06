package edu.cornell.opencomm.view;

import edu.cornell.opencomm.controller.ResetPasswordController;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.cornell.opencomm.R;

public class ResetPasswordView extends Activity {

   private static ImageButton resetPass; 
   private static EditText emailText; 
   private static TextView signup; 
   private static ResetPasswordController resetPasswordController; 
   
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);
        
        resetPass = (ImageButton) findViewById(R.id.resetButtonImage);
        emailText = (EditText) findViewById(R.id.emailTextBox); 
        signup = (TextView) findViewById(R.id.signupButton); 
        
        createEvent(); 
        resetPasswordController = new ResetPasswordController(this);        
	}
	
	private void createEvent(){
		ImageButton reset = getReset();
		TextView signUp = getSignUp(); 
		EditText email = getEmail(); 
		if (reset != null){
			reset.setOnClickListener(resetClick); 
		}
		if (signUp != null){
			signUp.setOnClickListener(signUpClicked); 
		}
		if(email != null){
			email.setOnEditorActionListener(emailEntered); 
		}
		
	}
	
	public ImageButton getReset(){
		return resetPass;
	}
	
	public EditText getEmail(){
		return emailText; 
	}
	
	private View.OnClickListener resetClick = new View.OnClickListener() {
		
		public void onClick(View v) {
			resetPasswordController.resetPasswordPressed(); 			
		}
	};
	
	private TextView.OnEditorActionListener emailEntered = new TextView.OnEditorActionListener() {
		
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	public TextView getSignUp(){
		return signup;
	}
	
	private View.OnClickListener signUpClicked = new View.OnClickListener() {
		//When sign up is clicked
		public void onClick(View v) {
			//TODO - Dont know which method to call
			
		}
	};
	
    
    //TODO
    //1. Implement the reset password button - send the control to the controller
    //2. Sign up button - implement onClick functionality- send to sign up page

}
