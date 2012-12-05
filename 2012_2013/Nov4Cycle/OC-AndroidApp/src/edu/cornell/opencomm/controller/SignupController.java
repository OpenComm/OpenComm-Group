package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
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
	@SuppressWarnings("unused")
	private static final String TAG = SignupController.class.getSimpleName();
	@SuppressWarnings("unused")
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
		new AccountController();
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
			String userName = email.replaceAll("[^a-zA-Z0-9]", "");
			String[] userInfo = {userName, fName, lName, email, title, pwd};
			new CreateUser().execute(userInfo);
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
			AsyncTask<String, Void, Boolean> {

		// TODO send the request to the server to create a new user
		// see if you can or should reuse UserManager
		@Override
		protected Boolean doInBackground(String... params) {
			// {userName, fName, lName, email, title, pwd};
			AccountController.createAcccount(params[0], params[1], params[3],
							params[1], params[2], "0", null, params[4], params[5]);
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
