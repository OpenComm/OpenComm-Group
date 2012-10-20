package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

public class LoginController {

	private static final String TAG = "Controller.LoginController";
	
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
		this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
		if(loginProgress == null){
			loginProgress = ProgressDialog.show(loginView, "Working..", "Please wait..");
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
				NotificationView loginErrorTip = new NotificationView(loginView);
				loginErrorTip.launch("Invalid Email ID or password. Please try Again!");
			}
		}
	}

}
