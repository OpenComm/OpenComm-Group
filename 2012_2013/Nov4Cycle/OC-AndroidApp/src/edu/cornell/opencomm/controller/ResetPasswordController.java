package edu.cornell.opencomm.controller;

import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.ResetPasswordView;

/**
 * Controller for reset password screen. Functionality:
 * <ul>
 * <li>When the user inputs an email, checks for a valid email, and sends an
 * reset password email</li>
 * <li>When the user opts to sign up instead, launches SignupView</li>
 * </ul>
 * 
 * Issues [TODO]:
 * <ul>
 * <li>Send reset password email to the given email</li>
 * <li>For any other issues search for string "TODO"</li>
 * </ul>
 * 
 * @author Spandana Govindgari[frontend], Risa Naka[frontend]
 * */

public class ResetPasswordController {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = ResetPasswordController.class
			.getSimpleName();

	// tag for the InputExtra to send to LoginView to indicate whether the password has been reset
	public static final String PWDRESET = "PasswordReset";

	// enum for email address check
	private enum ReturnState {
		EXISTING_USER, NONEXISTING_USER
	};

	private ProgressDialog findEmailProgress;

	private ResetPasswordView resetPasswordView;

	// Constructor - initialize required fields
	public ResetPasswordController(Context context, ResetPasswordView view) {
		this.resetPasswordView = view;
	}

	// Handle the reset password button
	// This method should load the reset password page
	public void resetPasswordPressed(String email) {
		this.resetPasswordView.getResetPwdOverlay().setVisibility(View.VISIBLE);
		// check that the inputs are not empty and in valid formats
		boolean isEmptyEmail = (email == null || email.equals(""));
		boolean isInvalidEmail = !Util.validateString(email,
				Util.EMAIL_ADDRESS_PATTERN);
		// if the email input is null/empty
		// if there are any errors with the inputs
				if (isEmptyEmail || isInvalidEmail) {
					StringBuilder errorText = new StringBuilder();
					errorText.append("Error:\n");
					if (isEmptyEmail) {
						errorText.append("\tEmail is required\n");
					}
					else if (isInvalidEmail) {
						errorText.append("\tInvalid email\n");
					}
					errorText.append("Please try again.");
					// show a toast describing the error
					Toast.makeText(this.resetPasswordView.getApplicationContext(), 
							errorText.toString(), Toast.LENGTH_SHORT).show();
					this.resetPasswordView.getResetPwdOverlay().setVisibility(View.INVISIBLE);
				}
				else {
					// attempt to find email in the network
					try {
						ReturnState rs = new FindEmailTask().execute(email).get();
						switch (rs) {
						case NONEXISTING_USER:
							resetPasswordView.getResetPwdOverlay().setVisibility(View.INVISIBLE);
							// let user know that we could not establish connection w/ server
				        	Toast.makeText(resetPasswordView.getApplicationContext(),
				        			"Could not connect to server. Please try again.", Toast.LENGTH_SHORT).show();
							break;
						case EXISTING_USER:
							// launch Login page
							Intent click = new Intent(this.resetPasswordView,
									LoginView.class);
							click.putExtra(ResetPasswordController.PWDRESET, true);
							this.resetPasswordView.startActivity(click);
							break;
						}
					} catch (InterruptedException e) {
						resetPasswordView.getResetPwdOverlay().setVisibility(View.INVISIBLE);
			        	Toast.makeText(resetPasswordView.getApplicationContext(),
			        			"Could not complete email search. Please try again.", Toast.LENGTH_SHORT).show();
					} catch (ExecutionException e) {
						resetPasswordView.getResetPwdOverlay().setVisibility(View.INVISIBLE);
			        	Toast.makeText(resetPasswordView.getApplicationContext(),
			        			"Could not complete email search. Please try again.", Toast.LENGTH_SHORT).show();
					}
				}
	}

	// Back button on Android pressed
	public void onBackPressed() {
		// go back to login page
		Intent click = new Intent(this.resetPasswordView, LoginView.class);
		this.resetPasswordView.startActivity(click);
	}

	private class FindEmailTask extends AsyncTask<String, Void, ReturnState> {
		@Override
		protected void onPreExecute() {
			findEmailProgress = new ProgressDialog(resetPasswordView);
			findEmailProgress.setIcon(resetPasswordView.getResources()
					.getDrawable(R.drawable.icon));
			findEmailProgress.setTitle("Searching for User");
			findEmailProgress.setMessage("Please wait...");
			findEmailProgress.show();
		}

		@Override
		protected ReturnState doInBackground(String... params) {
			String email = params[0];
			// TODO [backend] look for user using the given email in the network
			// TODO [backend] if use rnot in network, return ReturnState.NONEXISTING_USER
			return ReturnState.EXISTING_USER;
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			findEmailProgress.dismiss();
			if (state == ReturnState.EXISTING_USER) {
				// TODO [backend] send new dummy password to user
			}
		}
	}
}
