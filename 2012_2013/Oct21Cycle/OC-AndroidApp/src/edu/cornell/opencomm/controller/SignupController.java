package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.PopupView;
import edu.cornell.opencomm.view.SignupView;

public class SignupController {
	/**
	 * The TAG for logging
	 */
	private static final String TAG = SignupController.class.getSimpleName();
	
	private static final boolean D = true;
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


	private void showErrorPopup(String message,View view) {
		PopupView popup = new PopupView(context);
		popup.createPopup("Error", message);
		popup.createPositiveButton("OK", new PopupButtonOnlickListener(view));
		popup.showPopup();
	}

	public boolean validateName(String nameText) {

		return  Util.validateString(nameText,
				Util.NAME_PATTERN_SAVE);

	}

	public void handleLastNameFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!validateName(nameText)) {
					showErrorPopup(context.getResources().getString(
							R.string.invalid_last_name),view);
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
		boolean allInputsValid = validateAllFields();
		if (allInputsValid) {
			Intent click = new Intent(this.signupView,DashboardView.class);
			this.signupView.startActivity(click);
		}
	}
	public boolean validateAllFields(){
		return validateEmptyField(context.getResources().getString(R.string.first_name),signupView.getFirstNameTextBox())
				|| validateEmptyField(context.getResources().getString(R.string.last_name),signupView.getLastNameTextBox())
				|| validateEmptyField(context.getResources().getString(R.string.email),signupView.getEmailTextBox())
				|| validateEmptyField(context.getResources().getString(R.string.password),signupView.getPasswordTextBox());
	}
	private boolean validateEmptyField(String fieldName, View view){
		EditText textBox = (EditText) view;
		if(textBox.getText().toString() == null || textBox.getText().toString().isEmpty()){
			showErrorPopup(fieldName+" "+context.getResources().getString(R.string.empty_field_error) , view);
			return false;
		}
		return true;
	}
	public void handleCancel() {
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
					showErrorPopup(context.getResources().getString(
							R.string.invalid_email),view);
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
					showErrorPopup(context.getResources().getString(
							R.string.invalid_first_name),view);
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

	public void handleTitleFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!validateName(nameText)) {
					showErrorPopup(context.getResources().getString(
							R.string.invalid_title),view);
				}
			}
		}

	}

	public void handlePasswordFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String nameText = textBox.getText().toString();
			if (nameText != null && !nameText.equals("")) {
				if (!Util.validateString(nameText, Util.PASSWORD)) {
					showErrorPopup(context.getResources().getString(
							R.string.invalid_password),view);
				}
			}
		}

	}

	public void handleConfirmPasswordFocusChange(View view, boolean hasFocus) {
		if (!hasFocus) {
			EditText textBox = (EditText) view;
			String confirmPassword = textBox.getText().toString();
			if (confirmPassword != null && !confirmPassword.equals("")) {
				EditText passwordBox = (EditText) signupView.getPasswordTextBox();
				String password = passwordBox.getText().toString();
				if (!confirmPassword.equals(password)) {
					showErrorPopup(context.getResources().getString(
							R.string.password_dont_match),view);
				}
			}
		}

	}
	private class PopupButtonOnlickListener implements DialogInterface.OnClickListener{
		private View view;
		public PopupButtonOnlickListener(View view) {
			this.view = view;
		}
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			signupView.resetFocus(view);

		}

	}
}
