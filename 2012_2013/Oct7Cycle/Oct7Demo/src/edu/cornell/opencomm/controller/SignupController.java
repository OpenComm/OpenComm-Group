package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.SignupView;

public class SignupController {
	/**
	 * The View
	 */
	private SignupView signupView;
	/**
	 * The context
	 */
	
	private Context context;

	/**
	 * The constructor
	 * 
	 * @param view
	 */
	public SignupController(SignupView view, Context context) {
		this.signupView = view;
		this.context =context;
	}

	// TODO
	// 1. Handle save button
	// 2. Handle focus change on the textbox- guard condition to regain focus
	// 3. Handle cancel button
	// 4. Handle the android back button, and consume everthing else

	private void notifyTip(String message) {
		NotificationView notify = new NotificationView(context);
		notify.launch(message);
	}

	public boolean validateName(String nameText) {
			
		return  Util.validateString(nameText,
					Util.NAME_PATTERN_FOCUS_CHANGE);

	}

	public void handleLastNameFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!validateName(nameText)) {
					notifyTip(context.getResources().getString(
							R.string.invalid_last_name));
				}
			}
		}

	}

	public void handlePhotoButtonClick() {
		// TODO Auto-generated method stub
		// See :
		// http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app-in-android

	}

	public void handleSave() {
		//TODO: add code base on implementation of create user task and user account manager
		//TODO check validity of input
//		new CreateUser().execute(null);
		this.signupView.findViewById(R.id.acceptSignupOverlay).setVisibility(View.VISIBLE);
		boolean allInputsValid = true;
		if (allInputsValid) {
			Intent click = new Intent(this.signupView,DashboardView.class);
			this.signupView.startActivity(click);
		}
	}

	public void handleCancel() {
		// TODO Auto-generated method stub
		this.signupView.findViewById(R.id.cancelSignupOverlay).setVisibility(View.VISIBLE);
		Intent click = new Intent(this.signupView,LoginView.class);
		this.signupView.startActivity(click);

	}

	public void handleEmailFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!Util.validateString(nameText, Util.EMAIL_ADDRESS_PATTERN)) {
					notifyTip(context.getResources().getString(
							R.string.invalid_email));
				}
			}
		}

	}

	public void handleFirstNameFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!validateName(nameText)) {
					notifyTip(context.getResources().getString(
							R.string.invalid_first_name));
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private class CreateUser extends
			AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

		// TODO send the request to the server to create a new user
		// see if you can or should reuse UserManager
		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {

			return null;
		}

		// TODO launch the next activity
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
}
