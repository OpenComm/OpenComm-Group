package edu.cornell.opencomm.controller;

import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.controller.AccountController;

public class CreateAccountController {
	@SuppressWarnings("unused")
	private static final String TAG = CreateAccountController.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean D = true;
	/**
	 * The View
	 */
	private CreateAccountView createAccountView;
	private AccountController accountController;
	/**
	 * The constructor
	 * 
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		this.accountController = new AccountController();
		this.createAccountView = view;
	}	
	
	/*
	 * Pops a toast when the button [TODO] 
	 */
	public void handleSignUp(){
		/*String username, String nickname, String email,
			String firstName, String lastName, String phoneNumber,
			InputStream photo, String title, String password*/
		
		String username = createAccountView.getUsernameTextBox().getText().toString();
		String nickname = createAccountView.getFirstNameTextBox().getText().toString();
		String email = createAccountView.getEmailTextBox().getText().toString();
		String firstName = nickname;
		String password = createAccountView.getPasswordTextBox().getText().toString();
		accountController.createAcccount(username, nickname, email, firstName, "", "", null, "", password);
	}
}
