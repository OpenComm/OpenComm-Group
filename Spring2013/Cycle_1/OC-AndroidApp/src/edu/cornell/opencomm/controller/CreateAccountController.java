package edu.cornell.opencomm.controller;

import android.widget.Toast;
import edu.cornell.opencomm.view.CreateAccountView;


public class CreateAccountController {
	@SuppressWarnings("unused")
	private static final String TAG = CreateAccountController.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean D = true;
	/**
	 * The View
	 */
	private CreateAccountView createAccountView;

	/**
	 * The constructor
	 * 
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		//new AccountController();
		this.createAccountView = view;
	}	
	
	/*
	 * Pops a toast when the button [TODO] 
	 */
	public void handleSignUp(){
	}
}
