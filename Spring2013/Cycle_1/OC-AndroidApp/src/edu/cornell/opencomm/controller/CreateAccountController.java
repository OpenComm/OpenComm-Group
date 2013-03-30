package edu.cornell.opencomm.controller;

import android.widget.Toast;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.CreateAccountView;

/**
 * Author: Scarlet Yang (back-end) last edit: 3/25
 * */
public class CreateAccountController {
	@SuppressWarnings("unused")
	private static final String TAG = CreateAccountController.class
			.getSimpleName();
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
	 * Pops a toast when the button creates the user by using {@link
	 * edu.cornell.opencomm.controller.AccountController} uses the values from
	 * the view using its getters. returns true if account is successfully
	 * created and false if it fails
	 */
	public boolean handleSignUp(String userName, String emailAddress,
			String name, String passWord) {
		/*
		 * AccountController takes: String username, String nickname, String
		 * email, String firstName, String lastName, String phoneNumber,
		 * InputStream photo, String title, String password
		 */

		/*
		 * Checks to see if the fields are valid if not prompts the user
		 */
		boolean emptyFName = (name == null || name.length() == 0);
		boolean invalidFName = !Util.validateString(name,
				Util.NAME_PATTERN_SAVE);
		boolean emptyEmail = (emailAddress == null || emailAddress.length() == 0);
		boolean invalidEmail = !Util.validateString(emailAddress,
				Util.EMAIL_ADDRESS_PATTERN);
		boolean emptyPwd = (passWord == null || passWord.length() == 0);
		boolean emptyUser = (userName == null || userName.length() == 0);
		boolean invalidPwd = !Util.validateString(passWord, Util.PASSWORD);
		if (emptyFName || invalidFName || emptyEmail || invalidEmail
				|| emptyPwd || invalidPwd) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (emptyFName)
				errorText.append("\tFirst and last name required\n");
			else if (invalidFName)
				errorText.append("\tInvalid name\n");
			if (emptyEmail)
				errorText.append("\tEmail is required\n");
			else if (invalidEmail)
				errorText.append("\tInvalid email\n");
			if (emptyUser)
				errorText.append("\tUsername is required\n");
			if (emptyPwd)
				errorText.append("\tPassword is required\n");
			else if (invalidPwd)
				errorText.append("\tInvalid password (req: 10 - 30 chars)\n");
			errorText.append("Please try again.");
			// show a toast describing the error
			Toast.makeText(this.createAccountView.getApplicationContext(),
					errorText.toString(), Toast.LENGTH_SHORT).show();
			// this.createAccountView.findViewById(R.id.signup_acceptOverlay).setVisibility(View.INVISIBLE);
		}
		try {
			String username = createAccountView.getUsernameTextBox().getText()
					.toString();
			String nickname = createAccountView.getFirstNameTextBox().getText()
					.toString();
			String email = createAccountView.getEmailTextBox().getText()
					.toString();
			String firstName = nickname;
			String password = createAccountView.getPasswordTextBox().getText()
					.toString();
			// System.out.println("success");
			AccountController.createAcccount(username, nickname, email,
					firstName, "", "", null, "", password);
			// System.out.println("success");
			CharSequence text = "Go to sign up page";
			int duration = Toast.LENGTH_SHORT;
			Toast send = Toast.makeText(
					this.createAccountView.getApplicationContext(), text,
					duration);
			send.show();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
