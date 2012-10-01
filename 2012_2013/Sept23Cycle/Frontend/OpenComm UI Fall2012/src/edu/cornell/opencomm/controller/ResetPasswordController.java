package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.ResetPasswordView;

//Author: Spandana Govindgari sg754

public class ResetPasswordController {
	private ResetPasswordView resetPasswordView;

	// Added logs
	private static final String LOG_TAG = "ResetPasswordController";
	
	//Constructor - initialize required fields
	public ResetPasswordController(ResetPasswordView resetPasswordView){
		this.resetPasswordView = resetPasswordView;
	}
	
	//Handle sign up button
	public void signUpPressed(){
		
	}
	
	//Handle the reset password button
	public void resetPasswordPressed(){
		
	}
	
	//Find text from view 
	public void findUserName(){
		
	}
	
	//checks for the validity of the entered email
	private boolean isEmailFormatValid(){	
		return true; 
	}
	
	//Checks if the email if already in the network
	private boolean isEmailInNetwork(){
		return true; 
	}
	
	//[TODO} -Do something with Async Task 
}
