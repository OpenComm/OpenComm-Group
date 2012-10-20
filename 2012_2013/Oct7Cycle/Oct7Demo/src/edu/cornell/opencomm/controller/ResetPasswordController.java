package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

/** @author Author: Spandana Govindgari sg754 */

public class ResetPasswordController {
	
	// for debugging
	private static final String TAG = ResetPasswordController.class.getSimpleName();
	private static final boolean D = true;
	
	// for loginView
	public static final String PWDRESET = "PasswordReset";
	
	private ResetPasswordView resetPasswordView;
	private Context context; 
	
	//Constructor - initialize required fields
	public ResetPasswordController(Context context, ResetPasswordView view){
		this.resetPasswordView = view;
		this.context = context; 
	}
	
	//Handle sign up button
	public void signUpPressed(){
		if (D) Log.v(this.TAG, "Sign up pressed");
		this.resetPasswordView.findViewById(R.id.signupOverlayReset).setVisibility(View.VISIBLE);
		Intent click = new Intent(this.resetPasswordView,SignupView.class);
		this.resetPasswordView.startActivity(click);
		
	}
	
	//Handle the reset password button
	//This method should load the reset password page
	public void resetPasswordPressed(){
		//Send an email confirmation
		this.resetPasswordView.findViewById(R.id.resetPasswordOverlay).setVisibility(View.VISIBLE);
		//TODO
		//1. Should contact network and send a dummy password to this user's email
		// go back to login page
		Intent click = new Intent(this.resetPasswordView,LoginView.class);
		click.putExtra(ResetPasswordController.PWDRESET, true);
		this.resetPasswordView.startActivity(click);
	}
	
	// Back button on Android pressed
	public void onBackPressed() {
		// go back to login page
		Intent click = new Intent(this.resetPasswordView,LoginView.class);
		this.resetPasswordView.startActivity(click);
	}
	
	//Find text from view 
	public void findEmail(Editable email){
		if (isEmailValid(email)){
			if (isEmailInNetwork()){
				//prompt user to press reset
			}
			//else prompt user to sign up
		}
		//else prompt to the user
	}
	
	//checks for the validity of the entered email
	private boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	//Checks if the email if already in the network
	
	private boolean isEmailInNetwork(){
		return true; 
	}
	
	//[TODO} -Do something with Async Task 
	//If email is not in network then display error message
		//Take to sign up page
		//If found send email 
		//generate random password 
		//make popup that says message sent
}
