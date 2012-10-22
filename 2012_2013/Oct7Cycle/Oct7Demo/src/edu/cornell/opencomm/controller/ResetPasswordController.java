package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
	//Constructor - initialize required fields
	public ResetPasswordController(Context context, ResetPasswordView view){
		this.resetPasswordView = view; 
	}

	//Handle sign up button
	public void signUpPressed(){
		if (D) Log.v(TAG, "Sign up pressed");
		this.resetPasswordView.findViewById(R.id.signupOverlayReset).setVisibility(View.VISIBLE);
		Intent click = new Intent(this.resetPasswordView,SignupView.class);
		this.resetPasswordView.startActivity(click);
	}

	//Handle the reset password button
	//This method should load the reset password page
	public void resetPasswordPressed(EditText emailEntered){
		//Send an email confirmation
		this.resetPasswordView.findViewById(R.id.resetPasswordOverlay).setVisibility(View.VISIBLE);
		//TODO
		//1. Should contact network and send a dummy password to this user's email
		int i = findEmail((Editable) emailEntered);
		if (i == 1){
			// go back to login page
			Intent click = new Intent(this.resetPasswordView,LoginView.class);
			click.putExtra(ResetPasswordController.PWDRESET, true);
			this.resetPasswordView.startActivity(click);
		}
		this.resetPasswordView.throwToast(i);
	}

	// Back button on Android pressed
	public void onBackPressed() {
		// go back to login page
		Intent click = new Intent(this.resetPasswordView,LoginView.class);
		this.resetPasswordView.startActivity(click);
	}



	//Find text from view 
	public int findEmail(Editable email){
		if (isEmailValid(email)){
			if (isEmailInNetwork(email)){
				return 1; 
			}
			else {			
				return -1;
			}
		}
		else return 0; 
	}



	//checks for the validity of the entered email
	private boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	//Checks if the email if already in the network

	private boolean isEmailInNetwork(Editable email){
		//TODO - SEND TO BACKEND to check
		return true; 
	}

	//[TODO} -Do something with Async Task 
	//If email is not in network then display error message
	//Take to sign up page
	//If found send email 
	//generate random password 
	//make popup that says message sent
}
