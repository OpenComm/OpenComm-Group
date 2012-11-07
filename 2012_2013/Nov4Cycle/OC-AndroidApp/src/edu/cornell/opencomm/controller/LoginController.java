package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PopupView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for Login page (LoginView)
 * Functionality:
 * <ul>
 * <li>When the user has forgotten a password, it launches ResetPasswordView.</li>
 * <li>When the user wants to sign up, it launches SignupView.</li>
 * <li>When the user attempts to log in, checks for valid inputs, and then 
 * attempts to log in, and if successful, launches DashboardView</li>
 * </ul>
 *
 * Issues [TODO]
 * - For any other issues search for string "TODO"
 *
 * @author Heming Ge[frontend], Risa Naka [frontend]
 * */
public class LoginController {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = LoginController.class.getSimpleName();

	private ProgressDialog loginProgress;
	
	/**
	 * The View Object
	 */
	private LoginView loginView;
	
	/**
	 * The enum defining the opcodes for Login Task:
	 * <ul>
	 * <li>SUCCEEDED - app successfully connected to the server and signed in as 
	 * the given pair (email, password), which represents a valid user</li>
	 * <li>COULDNT_CONNECT - app could not connect to the server</li>
	 * <li>INVALID_PAIR - app could connect to the server, but the given pair 
	 * was invalid</li>
	 * </ul>
	 */
	public enum ReturnState {
		SUCCEEDED, COULDNT_CONNECT, INVALID_PAIR
	};


	/**
	 * @param view
	 */
	public LoginController(LoginView view) {
		this.loginView = view;
	}

	/** Checks whether the given email and password are properly formatted
	 * @param email - ex: oc1@test.org
	 * @param password - ex: opencomm
	 */
	public void handleLoginButtonClick(String email, String password) {
		this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
		// check that the inputs are not empty and in valid formats
		boolean isEmptyEmail = (email == null || email.equals(""));
		boolean isInvalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean isEmptyPwd = (password == null || password.equals(""));
		boolean isInvalidPwd = !Util.validateString(password, Util.PASSWORD);
		// if there are any errors with the inputs
		// TODO [frontend] modify error messages in accordance to design spec
		if (isEmptyEmail || isInvalidEmail || isEmptyPwd || isInvalidPwd) {
			StringBuilder errorText = new StringBuilder();
			if (isEmptyEmail) {
				errorText.append("Email cannot be empty\n");
			}
			else if (isInvalidEmail) {
				errorText.append("Invalid email\n");
			}
			if (isEmptyPwd) {
				errorText.append("Password cannot be empty\n");
			}
			else if(isInvalidPwd) {
				errorText.append("Invalid password");
			}
			// generate popup if there is an error
			PopupView popup = new PopupView(this.loginView);
			popup.createPopup("Error", errorText.toString());
			popup.createPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					loginView.getLoginOverlay().setVisibility(View.INVISIBLE);
					loginView.resetFocus();

				}
			} );
			popup.showPopup();
		}
		else {
			// attempt to log in
			new LoginTask().execute(email, password);
		}
	}

	public void handleCreateAccount() {
		this.loginView.getSignupOverlay().setVisibility(View.VISIBLE);
		Intent account = new Intent(this.loginView, SignupView.class);
		this.loginView.startActivity(account);
	}

	public void handleRetrievePassword() {
		Intent account = new Intent(this.loginView, ResetPasswordView.class);
		this.loginView.startActivity(account);
	}

	/** @return - the outcome of the background task */
	private class LoginTask extends AsyncTask<String, Void, ReturnState> {
		@Override
		protected void onPreExecute() {
			loginProgress = ProgressDialog.show(loginView, "Attempting Login",
					"Please wait...");
		}
		@Override
		protected ReturnState doInBackground(String... strings) {
			ReturnState loginRS = NetworkService.getInstance().login(strings[0], strings[1]);
			switch (loginRS) {
			// if login is successful
			case SUCCEEDED:
				String name = NetworkService.getInstance().getConnection().getAccountManager().getAccountAttribute("name");
				// generate primary user
				// TODO [backend] generate primary user
				UserManager.PRIMARY_USER = new User(strings[0], name, 0);
				break;
			default:
				break;
			}
			return loginRS;
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			loginProgress.dismiss();
			switch (state) {
			case COULDNT_CONNECT:
				loginView.getLoginOverlay().setVisibility(View.INVISIBLE);
				// let user know that we could not establish connection w/ server
				int duration = Toast.LENGTH_SHORT;
	        	Toast send = Toast.makeText(loginView.getApplicationContext(),
	        			"Could not connect to server; please try again.",duration);
	        	send.show();
				break;
			case INVALID_PAIR:
				loginView.getLoginOverlay().setVisibility(View.INVISIBLE);
				// let user know that the pair was invalid
				duration = Toast.LENGTH_SHORT;
	        	send = Toast.makeText(loginView.getApplicationContext(),
	        			"Invalid email and/or password; please try again.",duration);
	        	send.show();
				break;
			case SUCCEEDED:
				Intent i = new Intent(loginView, DashboardView.class);
				loginView.startActivity(i);
				break;
			default:
				break;
			}
		}
	}

}
