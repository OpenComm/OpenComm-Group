package edu.cornell.opencomm.controller;

import android.content.Context;
import android.text.Editable;
import edu.cornell.opencomm.view.ResetPasswordView;

//Author: Spandana Govindgari sg754

public class ResetPasswordController {
	private ResetPasswordView resetPasswordView;
	private Context context; 
	// Added logs
	private static final String LOG_TAG = "ResetPasswordController";
	
	//Constructor - initialize required fields
	public ResetPasswordController(Context context, ResetPasswordView view){
		this.resetPasswordView = view;
		this.context = context; 
	}
	
	//Handle sign up button
	public void signUpPressed(){
		
	}
	
	//Handle the reset password button
	//This method should load the reset password page
	public void resetPasswordPressed(){
		//Send an email confirmation
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
