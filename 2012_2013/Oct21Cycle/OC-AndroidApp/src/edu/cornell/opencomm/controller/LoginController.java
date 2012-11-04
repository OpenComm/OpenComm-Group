package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
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
	 * The enum defining the opcodes for Login Task
	 * 
	 */
	private enum ReturnState {
		SUCCEEDED, COULDNT_CONNECT, WRONG_PASSWORD, ALREADY_CLICKED
	};

	/**
	 * @param view
	 */
	public LoginController(LoginView view) {
		this.loginView = view;
	}

	/** Checks whether the given email and password are properly formatted
	 * @param email
	 * @param password
	 */
	public void handleLoginButtonClick(String email, String password) {
		this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
		// check that the inputs are not empty and in valid formats
		boolean isEmptyEmail = (email == null || email.equals(""));
		boolean isInvalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean isEmptyPwd = (password == null || password.equals(""));
		boolean isInvalidPwd = !Util.validateString(password, Util.PASSWORD);
		// if there are any errors with the inputs
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

	public void handleEmailFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText email = (EditText) view;
			String emailText = email.getText().toString();
			if (emailText != null && !emailText.equals("")) {
				if (!Util.validateString(emailText, Util.EMAIL_ADDRESS_PATTERN)) {
					PopupView popup = new PopupView(loginView);
					popup.createPopup("Error", "Incorrect Email");
					popup.createPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							loginView.resetFocus();

						}
					} );
					popup.showPopup();
				}
			}
		}
	}

	private void notifyTip(String message) {
		NotificationView notify = new NotificationView(loginView);
		notify.launch(message);
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
			if (NetworkService.getInstance().login(strings[0], strings[1])) {
				// if network service is in debug mode
				if (NetworkService.D) {
					UserManager.PRIMARY_USER = new User("opencommsec@cuopencomm.no-ip.org", "opencommsec@cuopencomm.no-ip.org", 0);
				}
				else {
					UserManager.PRIMARY_USER = new User(strings[0], strings[0], 0);
				}
				return ReturnState.SUCCEEDED;
			} else {
				return ReturnState.COULDNT_CONNECT;
			}
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			loginProgress.dismiss();
			if (state == ReturnState.ALREADY_CLICKED
					|| state == ReturnState.SUCCEEDED) {
				Intent i = new Intent(loginView, DashboardView.class);
				loginView.startActivity(i);

			} else {
				notifyTip("Invalid Email ID or password. Please try again!");
			}
		}
	}

}
