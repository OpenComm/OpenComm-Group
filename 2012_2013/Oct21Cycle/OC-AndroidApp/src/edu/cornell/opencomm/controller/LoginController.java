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

public class LoginController {

	@SuppressWarnings("unused")
	private static final String TAG = LoginController.class.getSimpleName();;

	private ProgressDialog loginProgress = null;
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

	/**
	 * @param email
	 * @param password
	 */
	public void handleLoginButtonClick(String email, String password) {
		// TODO Ankit->Ankit: crappy way of writing code :( fix it
		this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
		if (email == null || email.equals("")) {
			PopupView popup = new PopupView(loginView);
			popup.createPopup("Error", "Email cannot be empty");
			popup.createPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					loginView.resetFocus();

				}
			} );
			popup.showPopup();
		}
		else if (!Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN)) {
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
		else if (password == null || password.equals("")) {
			PopupView popup = new PopupView(loginView);
			popup.createPopup("Error", "Password cannot be empty");
			popup.createPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					loginView.resetFocus();

				}
			} );
			popup.showPopup();
		}
		else if (loginProgress == null) {
			loginProgress = ProgressDialog.show(loginView, "Working..",
					"Please wait..");
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
		protected ReturnState doInBackground(String... strings) {
			if (NetworkService.getInstance().login(strings[0], strings[1])) {
				UserManager.PRIMARY_USER = new User(strings[0], strings[0], 0);
				return ReturnState.SUCCEEDED;
			} else {
				return ReturnState.COULDNT_CONNECT;
			}
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			if (loginProgress != null) {
				loginProgress.dismiss();
				loginProgress = null;
			}
			if (state == ReturnState.ALREADY_CLICKED
					|| state == ReturnState.SUCCEEDED) {
				Intent i = new Intent(loginView, DashboardView.class);
				loginView.startActivity(i);

			} else {
				notifyTip("Invalid Email ID or password. Please try Again!");
			}
		}
	}

}
