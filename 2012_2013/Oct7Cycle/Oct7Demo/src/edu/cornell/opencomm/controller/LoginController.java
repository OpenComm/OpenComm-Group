package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

public class LoginController  {

	/**
	 * The View Object
	 */
	private LoginView loginView;

	/**
	 * The enum defining the opcodes for Login Task
	 *
	 */
	private enum ReturnState{SUCEEDED, COULDNT_CONNECT, WRONG_PASSWORD, ALREADY_CLICKED};

	private XMPPConnection connection;

	/**
	 * @param view
	 */
	public LoginController(LoginView view){
		this.loginView = view;
	}


	/**
	 * @param email
	 * @param password
	 */
	public void handleLoginButtonClick(String email, String password) {
		// TODO sign in with the given email and password
		// if successful
    	this.loginView.getLoginOverlay().setVisibility(View.VISIBLE);
    	Intent i = new Intent(this.loginView,DashboardView.class);
    	this.loginView.startActivity(i);
	}
	
	public void handleCreateAccount(){
		this.loginView.getSignupOverlay().setVisibility(View.VISIBLE);
    	Intent account = new Intent(this.loginView,SignupView.class);
    	this.loginView.startActivity(account);
	}
	
	public void handleRetrievePassword(){
		Intent account = new Intent(this.loginView,ResetPasswordView.class);
    	this.loginView.startActivity(account);
	}


	private class LoginTask extends AsyncTask<String, Void, ReturnState> {

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Ask the design team.
			super.onProgressUpdate(values);
		}
		//ReturnState specifies the outcome of the background task.
		@Override
		protected ReturnState doInBackground(String... strings) {
			// [TODO] Need correct getter methods for connection, username, password...
			
			// need to call in button click
			
			if (connection.isConnected()) {
				return ReturnState.ALREADY_CLICKED;
			}
			else {
				try {
					connection.login(strings[0], strings[1]);
				} catch (XMPPException e) {
					return ReturnState.WRONG_PASSWORD;
				}
				return ReturnState.SUCEEDED;
			}
		}

		//[TODO] This function needs to do either
		// 1. launch dashboard
		// 2. go to last activity
		// 3. login the user
		@Override
		protected void onPostExecute(ReturnState state) {
			if (state == ReturnState.ALREADY_CLICKED || state == ReturnState.SUCEEDED)
			{
				DashboardView d = new DashboardView();
				d.setVisible(true);
			}
			else if (state == ReturnState.WRONG_PASSWORD)
			{
				loginView.setVisible(false);
			}
		}
	}

}
