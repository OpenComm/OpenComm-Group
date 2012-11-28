package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.jivesoftware.smack.AccountManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for Sign up page (SignupView) Functionality:
 * <ul>
 * <li>When the user has provided valid registration inputs, creates account to
 * database, and email with new dummy password sent.</li>
 * <li>When the user provides invalid inputs, returns toasts with errors.</li>
 * <li>When the user opts out by cancel button or back button, returns to
 * previous activity.</li>
 * </ul>
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Ankit Singh [frontend], Risa Naka [frontend]
 * */
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
	 * The constructor
	 * 
	 * @param view
	 */
	public SignupController(SignupView view) {
		this.signupView = view;
	}

	/** Save button has been clicked: checks validity of inputs and sends error toasts if invalid 
	 * or generates user in server if valid
	 * @param fName
	 * @param lName
	 * @param email
	 * @param title
	 * @param pwd
	 * @param confirmPwd
	 */
	public void handleSave(String fName, String lName, String email,
			String title, String pwd, String confirmPwd) {
		this.signupView.findViewById(R.id.signup_acceptOverlay).setVisibility(View.VISIBLE);
		// check validity of input
		boolean emptyFName = (fName == null || fName.length() == 0);
		boolean invalidFName = !Util.validateString(fName, Util.NAME_PATTERN_SAVE);
		boolean emptyLName = (lName == null || lName.length() == 0);
		boolean invalidLName = !Util.validateString(lName, Util.NAME_PATTERN_SAVE);
		boolean emptyEmail = (email == null || email.length() == 0);
		boolean invalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		boolean emptyPwd = (pwd == null || pwd.length() == 0);
		boolean invalidPwd = !Util.validateString(pwd, Util.PASSWORD);
		boolean emptyCPwd = (confirmPwd == null || confirmPwd.length() == 0);
		boolean mismatchPwd = !(pwd.equals(confirmPwd));
		if (emptyFName || invalidFName || emptyLName || invalidLName
				|| emptyEmail || invalidEmail || emptyPwd || invalidPwd
				|| emptyCPwd || mismatchPwd) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (emptyFName) errorText.append("\tFirst name is required\n");
			else if (invalidFName) errorText.append("\tInvalid first name\n");
			if (emptyLName) errorText.append("\tLast name is required\n");
			else if (invalidLName) errorText.append("\tInvalid last name\n");
			if (emptyEmail) errorText.append("\tEmail is required\n");
			else if (invalidEmail) errorText.append("\tInvalid email\n");
			if (emptyPwd) errorText.append("\tPassword is required\n");
			else if (invalidPwd) errorText.append("\tInvalid password (req: 10 - 30 chars)\n");
			if (emptyCPwd) errorText.append("\tPassword confirmation is required\n");
			else if (mismatchPwd) errorText.append("\tPassword confirmation does not match\n");
			errorText.append("Please try again.");
			// show a toast describing the error
			Toast.makeText(this.signupView.getApplicationContext(),
					errorText.toString(), Toast.LENGTH_SHORT).show();
			this.signupView.findViewById(R.id.signup_acceptOverlay).setVisibility(View.INVISIBLE);
		} else {
			// TODO [backend] create a new user
			new CreateUser().execute();
			Intent click = new Intent(this.signupView, DashboardView.class);
			this.signupView.startActivity(click);
		}
	}
	
	/** Photo input button clicked
	 * TODO [frontend/Ankit] empty method 
	 * TODO[Backend/Kris] BE should send the user vcard from the user object to server
	 * */
	
	public void handlePhotoButtonClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/** Creates user based on given inputs
	 * TODO [backend] what error results are outputted for each case (creation fail, already 
	 * registered emails, etc) */
	private class CreateUser extends
			AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

		// TODO send the request to the server to create a new user
		// see if you can or should reuse UserManager
		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
			AccountManager accountManager = NetworkService.getInstance()
					.getConnection().getAccountManager();
			try {
				if (accountManager.supportsAccountCreation()) {
					// TODO [backend] use input from user
					accountManager.createAccount("Test007", "Skyfall");
					if (D)
						Log.d(TAG, "Successful account creation");
				} else {
					if (D)
						Log.d(TAG, "Account Creation is not supported");
					if (D)
						Log.d(TAG,
								"Account Instructions: "
										+ accountManager
												.getAccountInstructions());

				}
			} catch (Exception e) {
				if (D)
					Log.d(TAG, "Error in account creation:" + e.getMessage());
				e.printStackTrace();
			}
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
