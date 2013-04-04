package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.ForgotPsdView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.LoginView_v2;
import edu.cornell.opencomm.view.ResetPasswordView;

/**
 * Controller for Login page (LoginView) Functionality:
 * <ul>
 * <li>When the user has forgotten a password, it launches ResetPasswordView.</li>
 * <li>When the user wants to sign up, it launches SignupView.</li>
 * <li>When the user attempts to log in, checks for valid inputs, and then
 * attempts to log in, and if successful, launches DashboardView</li>
 * </ul>
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Heming Ge[frontend], Risa Naka [frontend]
 * */
public class LoginController {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
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
	private LoginView_v2 loginView;

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
	public LoginController(LoginView_v2 view) {
		this.loginView = view;
	}

	/**
	 * Checks whether the given email and password are properly formatted
	 * 
	 * @param email
	 *            - ex: oc1@test.org
	 * @param password
	 *            - ex: opencomm
	 */
	public void handleLoginButtonClick(String email, String password) {
		this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
		// check that the inputs are not empty and in valid formats
		Log.v("LOGIN", email);
		Log.v("LOGIN", password);
		boolean isEmptyEmail = (email == null || email.equals(""));
		boolean isInvalidEmail = !Util.validateString(email,
				Util.EMAIL_ADDRESS_PATTERN);
		boolean isEmptyPwd = (password == null || password.equals(""));
		boolean isInvalidPwd = !Util.validateString(password, Util.PASSWORD);
		// if there are any errors with the inputs
		if (isEmptyEmail || isInvalidEmail || isEmptyPwd || isInvalidPwd) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (isEmptyEmail) {
				errorText.append("\tEmail is required\n");
			} else if (isInvalidEmail) {
				errorText.append("\tInvalid email\n");
			}
			if (isEmptyPwd) {
				errorText.append("\tPassword is required\n");
			} else if (isInvalidPwd) {
				errorText.append("\tInvalid password\n\n");
			}
			errorText.append("Please try again.");
			// show a toast describing the error
			Toast.makeText(this.loginView.getApplicationContext(),
					errorText.toString(), Toast.LENGTH_SHORT).show();
			this.loginView.getLoginOverlay().setVisibility(View.INVISIBLE);
		} else {
			// attempt to log in
			new LoginTask().execute(email, password);
		}
	}

	/**
	 * When the "Sign up" button is clicked, provide user feedback and launch
	 * the Sign up page
	 */
	public void handleSignup() {
		// user feedback: white overlay
		this.loginView.getSignupOverlay().setVisibility(View.VISIBLE);
		// start sign up view
		Intent account = new Intent(this.loginView, CreateAccountView.class);
		this.loginView.startActivity(account);
	}

	/**
	 * When the "Forgot password" link is clicked, launch the Reset Password
	 * page
	 */
	public void handleForgotPassword() {
		// launch reset password page
		Intent account = new Intent(this.loginView, ForgotPsdView.class);
		this.loginView.startActivity(account);
	}

	/** @return - the outcome of the background task */
	private class LoginTask extends AsyncTask<String, Void, ReturnState> {
		@Override
		protected void onPreExecute() {
			loginProgress = new ProgressDialog(loginView);
			loginProgress.setIcon(loginView.getResources().getDrawable(
					R.drawable.icon));
			loginProgress.setTitle("Attempting Login");
			loginProgress.setMessage("Please wait...");
			loginProgress.show();
		}

		@Override
		protected ReturnState doInBackground(String... strings) {
			ReturnState loginRS = NetworkService.getInstance().login(
					strings[0], strings[1]);
			switch (loginRS) {
			// if login is successful
			case SUCCEEDED:
				String name = NetworkService.getInstance().getConnection()
						.getAccountManager().getAccountAttribute("name");
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
				// let user know that we could not establish connection w/
				// server
				Toast.makeText(loginView.getApplicationContext(),
						"Could not connect to server. Please try again.",
						Toast.LENGTH_SHORT).show();
				break;
			case INVALID_PAIR:
				loginView.getLoginOverlay().setVisibility(View.INVISIBLE);
				// let user know that the pair was invalid
				Toast.makeText(loginView.getApplicationContext(),
						"Incorrect email and/or password. Please try again.",
						Toast.LENGTH_SHORT).show();
				NetworkService.getInstance().logout();
				break;
			case SUCCEEDED:
				// launch dashboard
				Intent i = new Intent(loginView, DashboardView.class);
				loginView.startActivity(i);
				break;
			default:
				break;
			}
		}
	}

}
