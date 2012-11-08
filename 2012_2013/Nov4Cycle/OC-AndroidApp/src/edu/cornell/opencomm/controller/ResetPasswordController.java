package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.PopupView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;

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
 * <li>Move email validation (registered email?) to an AsyncTask</li>
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
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	private static final String TAG = ResetPasswordController.class
			.getSimpleName();

	// for loginView
	public static final String PWDRESET = "PasswordReset";

	// enum for email address check
	private enum EmailStatus {
		EXISTING_USER, NONEXISTING_USER, INVALID_EMAIL
	};

	private ResetPasswordView resetPasswordView;

	// Constructor - initialize required fields
	public ResetPasswordController(Context context, ResetPasswordView view) {
		this.resetPasswordView = view;
	}

	// Handle sign up button
	public void signUpPressed() {
		if (D)
			Log.v(TAG, "Sign up pressed");
		this.resetPasswordView.findViewById(R.id.signupOverlayReset)
				.setVisibility(View.VISIBLE);
		Intent click = new Intent(this.resetPasswordView, SignupView.class);
		this.resetPasswordView.startActivity(click);
	}

	// Handle the reset password button
	// This method should load the reset password page
	public void resetPasswordPressed(String emailEntered) {
		this.resetPasswordView.findViewById(R.id.resetPasswordOverlay)
				.setVisibility(View.VISIBLE);
		// if the email input is null/empty
		if (emailEntered == null || emailEntered.equals("")) {
			PopupView popup = new PopupView(resetPasswordView);
			popup.createPopup("Input Error", "Email cannot be empty");
			popup.createPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							resetPasswordView.findViewById(
									R.id.resetPasswordOverlay).setVisibility(
									View.INVISIBLE);
						}
					});
			popup.showPopup();
		} else {
			// check if the email input is valid
			EmailStatus eStatus = findEmail(emailEntered);
			switch (eStatus) {
			case EXISTING_USER: {
				EmailController pwrdController = new EmailController();
				pwrdController.resetPasword(emailEntered);
				try {
					pwrdController.send();
				} catch (Exception e) {
					Log.v(TAG, "Error sending password email");
					Log.v(TAG, e.getMessage());
				}
				/** TODO [backend] send dummy password to this user's email */
				// go back to login page
				Intent click = new Intent(this.resetPasswordView,
						LoginView.class);
				click.putExtra(ResetPasswordController.PWDRESET, true);
				this.resetPasswordView.startActivity(click);
				break;
			}
			case NONEXISTING_USER: {
				PopupView popup = new PopupView(resetPasswordView);
				popup.createPopup("Input Error", "Unregistered email");
				popup.createPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								resetPasswordView.findViewById(
										R.id.resetPasswordOverlay)
										.setVisibility(View.INVISIBLE);
							}
						});
				popup.showPopup();
				break;
			}
			case INVALID_EMAIL: {
				PopupView popup = new PopupView(resetPasswordView);
				popup.createPopup("Input Error", "Invalid email");
				popup.createPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								resetPasswordView.findViewById(
										R.id.resetPasswordOverlay)
										.setVisibility(View.INVISIBLE);
							}
						});
				popup.showPopup();
				break;
			}
			}
		}
	}

	// Back button on Android pressed
	public void onBackPressed() {
		// go back to login page
		Intent click = new Intent(this.resetPasswordView, LoginView.class);
		this.resetPasswordView.startActivity(click);
	}

	/**
	 * Checks if the given email is valid and is in the server
	 * 
	 * @param email
	 * @return
	 */
	public EmailStatus findEmail(String email) {
		// if it's a valid email
		if (Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN)) {
			// check if this email exists in the server
			return (isEmailInNetwork(email) ? EmailStatus.EXISTING_USER
					: EmailStatus.NONEXISTING_USER);
		}
		return EmailStatus.INVALID_EMAIL;
	}

	// Checks if the email if already in the network

	private boolean isEmailInNetwork(String email) {
		// TODO[backend] check if the email is registered
		return true;
	}
}
