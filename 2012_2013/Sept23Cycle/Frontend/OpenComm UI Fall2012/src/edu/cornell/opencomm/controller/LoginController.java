package edu.cornell.opencomm.controller;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.view.LoginView;

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



	/**
	 * @param view
	 */
	public LoginController(LoginView view){
		this.loginView = view;
	}


	/**
	 * @param usernameEdit
	 * @param passwordEdit
	 */
	public void handleLoginButtonClick(EditText usernameEdit, EditText passwordEdit) {
		loginView.getLoginOverlay().setVisibility(View.VISIBLE);
	}


	private class LoginTask extends AsyncTask<String, Void, ReturnState> {




		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Ask the design team.
			super.onProgressUpdate(values);
		}
		//[TODO] Establish a connection to the server and authenticate the username and password
		//ReturnState specifies the outcome of the background task.
		@Override
		protected ReturnState doInBackground(String... strings) {
			return null;
		}

		//[TODO] This function needs to do either
		// 1. launch dashboard
		// 2. go to last activity
		// 3. login the user
		@Override
		protected void onPostExecute(ReturnState state) {

		}
	}

}