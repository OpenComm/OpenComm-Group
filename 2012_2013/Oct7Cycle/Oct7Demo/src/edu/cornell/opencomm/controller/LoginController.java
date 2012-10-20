package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

public class LoginController {

	private static final String TAG = LoginController.class.getSimpleName();;

	private ProgressDialog loginProgress = null ;
	/**
	 * The View Object
	 */
	private LoginView loginView;

	/**
	 * The enum defining the opcodes for Login Task
	 * 
	 */
	private enum ReturnState {
		SUCEEDED, COULDNT_CONNECT, WRONG_PASSWORD, ALREADY_CLICKED
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
		// TODO: use to choose next screen
		if(validateEmail(email)){
			this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
			if(loginProgress == null){
				loginProgress = ProgressDialog.show(loginView, "Working..", "Please wait..");
				new LoginTask().execute(email, password);
			}
		}else{
			loginView.resetFocus();
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
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			validateEmail(nameText);
		}

	}
	private boolean validateEmail(String nameText){
		if (nameText != null && !nameText.equals("")) {
			if (!Util.validateString(nameText, Util.EMAIL_ADDRESS_PATTERN)) {
				notifyTip(loginView.getResources().getString(
						R.string.invalid_email));
				return false;
			}else{
				return true;
			}

		}else{
			return false;
		}
	}
	private void notifyTip(String message) {
		NotificationView notify = new NotificationView(loginView);
		notify.launch(message);
	}
	private class LoginTask extends AsyncTask<String, Void, ReturnState> {

		// ReturnState specifies the outcome of the background task.
		@Override
		protected ReturnState doInBackground(String... strings) {
			if (NetworkService.getInstance().login(strings[0], strings[1])) {
				return ReturnState.SUCEEDED;
			} else {
				return ReturnState.COULDNT_CONNECT;
			}
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			if(loginProgress != null){
				loginProgress.dismiss();
				loginProgress = null;
			}
			if (state == ReturnState.ALREADY_CLICKED
					|| state == ReturnState.SUCEEDED) {
				Intent i = new Intent(loginView, DashboardView.class);
				loginView.startActivity(i);

			} else {
				notifyTip("Invalid Email ID or password. Please try Again!");
			}
		}
	}

}
