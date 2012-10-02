package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import edu.cornell.opencomm.view.SignupView;

import android.os.AsyncTask;
import android.view.View;

public class SignupController {
	/**
	 * The Vie
	 */
	private SignupView view;
	/**The constructor
	 * @param view
	 */
	public SignupController(SignupView view){
		this.view = view;
	}
	//TODO
	//1. Handle save button
	//2. Handle focus change on the textbox- guard condition to regain focus
	//3. Handle cancel button
	//4. Handle the android back button, and consume everthing else
	

	public void handleFirstNameFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	public void handleLastNameFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	public void handlePasswordFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	public void handleConfirmPasswordFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	public void handleTitleFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	public void handlePhotoButtonClick() {
		// TODO Auto-generated method stub
		
	}

	public void handleSave() {
		// TODO Auto-generated method stub
		
	}

	public void handleCancel() {
		// TODO Auto-generated method stub
		
	}

	public void handleEmailFoucsChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}
	private class CreateUser extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

		//TODO send the request to the server to create a new user
		// see if you can or should reuse UserManager
		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {

			return null;
		}
		//TODO launch the next activity
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
}
