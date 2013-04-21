package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

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
	
	/**
	 * The constructor
	 * 
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		this.createAccountView = view;
	}

	/*
	 * Pops a toast when the button creates the user by using {@link
	 * edu.cornell.opencomm.controller.AccountController} uses the values from
	 * the view using its getters. returns true if account is successfully
	 * created and false if it fails
	 */
	public void handleSave(String fName, String lName, String email, String username,
			String title, String pwd, String confirmPwd) {
		//this.signupView.findViewById(R.id.signup_acceptOverlay).setVisibility(View.VISIBLE);
		// check validity of input
		Log.v("SIGNUP", fName+" "+lName+" "+email+" "+username+" "+title+" "+pwd+" "+confirmPwd);
		boolean emptyFName = (fName == null || fName.length() == 0);
		boolean invalidFName = !Util.validateString(fName, Util.NAME_PATTERN_SAVE);
		boolean emptyLName = (lName == null || lName.length() == 0);
		boolean invalidLName = !Util.validateString(lName, Util.NAME_PATTERN_SAVE);
		boolean emptyEmail = (email == null || email.length() == 0);
		boolean invalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean emptyUsername = (username == null || username.length() == 0);
		//TODO: validate username
		boolean emptyPwd = (pwd == null || pwd.length() == 0);
		boolean invalidPwd = !Util.validateString(pwd, Util.PASSWORD);
		boolean emptyCPwd = (confirmPwd == null || confirmPwd.length() == 0);
		boolean mismatchPwd = !(pwd.equals(confirmPwd));
		if (emptyFName || invalidFName || emptyLName || invalidLName
				|| emptyEmail || emptyUsername || invalidEmail || emptyPwd || invalidPwd
				|| emptyCPwd || mismatchPwd) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (emptyFName) errorText.append("\tFirst name is required\n");
			else if (invalidFName) errorText.append("\tInvalid first name\n");
			if (emptyLName) errorText.append("\tLast name is required\n");
			else if (invalidLName) errorText.append("\tInvalid last name\n");
			if (emptyEmail) errorText.append("\tEmail is required\n");
			else if (invalidEmail) errorText.append("\tInvalid email\n");
			if (emptyPwd) errorText.append("\tPassword is required\n");
			else if (invalidPwd) errorText.append("\tInvalid password (req: 10 - 30 chars)\n");
			if (emptyCPwd) errorText.append("\tPassword confirmation is required\n");
			else if (mismatchPwd) errorText.append("\tPassword confirmation does not match\n");
			if (emptyUsername) errorText.append("\tUsername is required\n");
			errorText.append("Please try again.");
			// show a toast describing the error
			Toast.makeText(this.createAccountView.getApplicationContext(),
					errorText.toString(), Toast.LENGTH_SHORT).show();
			//this.signupView.findViewById(R.id.signup_acceptOverlay).setVisibility(View.INVISIBLE);
		} else {
			String[] userInfo = {username, fName, lName, email, title, pwd};
			new CreateUser().execute(userInfo);
		}
	}
	
	/** Creates user based on given inputs
	 * TODO [backend] what error results are outputted for each case (creation fail, already 
	 * registered emails, etc) */
	private class CreateUser extends AsyncTask<String, Void, Boolean> {
		

		@Override
		protected Boolean doInBackground(String... params) {
			if (CheckEmail.emailAlreadyExists(params[3])) {
				//Toast.makeText(createAccountView.getApplicationContext(), "Email address already registered with OpenComm", Toast.LENGTH_SHORT).show();
				return false;
			}
			else if (CheckEmail.jidAlreadyExists(params[0])) {
				//Toast.makeText(createAccountView.getApplicationContext(), "Username already registered with OpenComm", Toast.LENGTH_SHORT).show();
				return false;
			}
			else {
				return HttpRequest.createAccount(params[0], params[1], params[3],params[1], params[2], "0", null, params[4], params[5]); 
			}
							
		}

		// TODO launch the next activity
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(createAccountView.getApplicationContext(), "Account created successfuly", Toast.LENGTH_SHORT).show();
				Intent click = new Intent(createAccountView, DashboardView.class);
				createAccountView.startActivity(click);
			} else {
				Toast.makeText(createAccountView.getApplicationContext(), "Error during account creation, please try again", Toast.LENGTH_SHORT).show();
			}
			
			
		}
	}
}
