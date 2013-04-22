package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.CreateAccountReturnState;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.view.DashboardView;

/**
 * Author: Scarlet Yang (back-end) last edit: 3/25
 * Edited: Antoine Chkaiban (back-end) last edit 04/22/2013
 * */
public class CreateAccountController {
	private static final String TAG = CreateAccountController.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The View
	 */
	private CreateAccountView createAccountView;
	
	/**
	 * The constructor
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		this.createAccountView = view;
	}
	
	private ProgressDialog loginProgress;

	/*
	 * Verifies inputs by user, then starts Async Task to create User on server, and the VCard.
	 * Pops a Toast onPostExecute, and goes to Dashboard only if successful.
	 */
	public void handleSave(String fName, String lName, String email, String username,
			String title, String pwd, String confirmPwd) {
		
		// check validity of input
		Log.v(TAG, fName+" "+lName+" "+email+" "+username+" "+title+" "+pwd+" "+confirmPwd);
		boolean emptyFName = (fName == null || fName.length() == 0);
		boolean invalidFName = !Util.validateString(fName, Util.NAME_PATTERN_SAVE);
		boolean emptyLName = (lName == null || lName.length() == 0);
		boolean invalidLName = !Util.validateString(lName, Util.NAME_PATTERN_SAVE);
		boolean emptyEmail = (email == null || email.length() == 0);
		boolean invalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean emptyUsername = (username == null || username.length() == 0);
		boolean invalidUsername = !Util.validateString(username, Util.USERNAME);
		boolean emptyPwd = (pwd == null || pwd.length() == 0);
		boolean invalidPwd = !Util.validateString(pwd, Util.PASSWORD);
		boolean emptyCPwd = (confirmPwd == null || confirmPwd.length() == 0);
		boolean mismatchPwd = !(pwd.equals(confirmPwd));
		if (emptyFName || invalidFName || emptyLName || invalidLName
				|| emptyEmail || emptyUsername || invalidUsername || invalidEmail || emptyPwd || invalidPwd
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
			else if (invalidUsername) errorText.append("\tInvalid username\n");
			errorText.append("Please try again.");
			
			// show a toast describing the error
			Toast.makeText(this.createAccountView.getApplicationContext(),errorText.toString(), Toast.LENGTH_SHORT).show();
		} else {
			//Start Async Task
			String[] userInfo = {username, email, fName, lName, pwd};
			new CreateUser().execute(userInfo);
		}
	}
	
	/** 
	 * Creates user based on given inputs
	 * @param String[] userInfo
	 **/
	//TO DO: ADD DIALOG
	private class CreateUser extends AsyncTask<String, Void, CreateAccountReturnState> {
		
		@Override
		protected void onPreExecute() {
			loginProgress = new ProgressDialog(createAccountView);
			loginProgress.setIcon(createAccountView.getResources().getDrawable(
					R.drawable.icon));
			loginProgress.setTitle("Attempting Account Creation");
			loginProgress.setMessage("Please wait...");
			loginProgress.show();
		}
		

		@Override
		protected CreateAccountReturnState doInBackground(String... params) {
			if (SearchService.emailAlreadyExists(params[3])) {
				return CreateAccountReturnState.EMAIL_EXISTS;
			}
			else if (SearchService.jidAlreadyExists(params[0])) {
				return CreateAccountReturnState.JID_EXISTS;
			}
			else {
				return EnhancedAccountManager.createAccount(params[0], params[1], params[2],params[3], params[4]); 
			}
							
		}

		// TODO launch the next activity
		@Override
		protected void onPostExecute(CreateAccountReturnState returnState) {
			loginProgress.dismiss();
			switch (returnState) {
				case SUCCEEDED:
					Toast.makeText(createAccountView.getApplicationContext(), "Account created successfuly", Toast.LENGTH_SHORT).show();
					Intent click = new Intent(createAccountView, DashboardView.class);
					createAccountView.startActivity(click);
					break;
				case EMAIL_EXISTS:
					Toast.makeText(createAccountView.getApplicationContext(), "Email address already registered with OpenComm", Toast.LENGTH_SHORT).show();
					break;
				case JID_EXISTS:
					Toast.makeText(createAccountView.getApplicationContext(), "Username already registered with OpenComm", Toast.LENGTH_SHORT).show();
					break;
				case SERVER_ERROR:
					Toast.makeText(createAccountView.getApplicationContext(), "Server error during account creation, please try again later", Toast.LENGTH_SHORT).show();
					break;
			}
			
			
		}
	}
}
