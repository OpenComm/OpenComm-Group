/**
 * Reset the password
 *
 * Issues [TODO]
 * - Find the best way among the three to reset a password
 *
 * @author fl4v, cl587
 * */

package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.XMPPConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.network.UserAccountManager;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PopupNotificationView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;

public class ResetPasswordController {

	private ResetPasswordView resetPasswordView;

	// Logs
	private static final String LOG_TAG = "ResetPasswordController";

	// Username string
	private String username;

	// Network service and Connection
	private NetworkService xmppService;
	private XMPPConnection xmppConn;
	
	private UserAccountManager userAccountManager;

	public ResetPasswordController(ResetPasswordView resetPasswordView) {
		this.xmppService = new NetworkService(Network.DEFAULT_HOST,
				Network.DEFAULT_PORT);
		this.xmppConn = xmppService.getXMPPConnection();
		this.resetPasswordView = resetPasswordView;
		this.userAccountManager = new UserAccountManager(xmppConn);
	}

	/**
	 * Finds EditText from the view, writes the string rep of its contents to
	 * String uesrname
	 */
	public void findUsername() {
		final EditText resetEditText = resetPasswordView.getResetUsername();
		if (resetEditText != null) {
			username = resetEditText.getText().toString();
			Log.d(LOG_TAG, "username input: " + username);
		}
	}

	/**
	 * Dismisses the popup if email is in database and send a new email to the
	 * address. Otherwise, popup prompt clarifying the user's error
	 */
	public void handleResetButtonClick() {

		resetPasswordView.getResetOverlay().setVisibility(View.VISIBLE);
		findUsername();
		// Does 1 last local email check with Android matcher
		if (!handleTextChange(resetPasswordView.getResetUsername().getText())) {
			resetPasswordView.getResetOverlay().setVisibility(View.INVISIBLE);
			return;
		}
		// Checks network for email validation
		if (validEmail(username)) {
			NotificationView popup = new NotificationView(
					resetPasswordView.getContext());
			// Should use a string xml
			// popup.launch("User inputted valid email, password sent.","RED","WHITE",
			// true);
			// Dismisses the window for now
			PopupNotificationView popupNotificationView = new PopupNotificationView(
					resetPasswordView.getContext(), null, "sent",
					"New password sent to email.", "", 1);
			popupNotificationView.createPopupWindow();
			resetPasswordView.getWindow().dismiss();
		} else {
			resetPasswordView.getResetOverlay().setVisibility(View.INVISIBLE);
			NotificationView popup1 = new NotificationView(
					resetPasswordView.getContext());
			// Should use a string xml
			popup1.launch(
					"Username/email not found in database. Please try again.",
					"RED", "WHITE", true);

		}
		Log.d(LOG_TAG, "reset password button clicked");
	}

	/** Dismisses this popup and call JP's account creation popup */
	public void handleSignUpButtonClick() {
		resetPasswordView.getSignUpOverlay().setVisibility(View.VISIBLE);
		findUsername();
		LayoutInflater ifl = (LayoutInflater) resetPasswordView.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SignupView suv = new SignupView(resetPasswordView.getContext());
		suv.launch();
		// Dismisses the window for now
		// NotificationView popup = new
		// NotificationView(resetPasswordView.getContext());
		// popup.launch("Sign up page here","RED","WHITE", true);
		resetPasswordView.getWindow().dismiss();
		Log.d(LOG_TAG, "sign up button clicked");
	}

	/** Handler for when the textbox changes */
	public boolean handleTextChange(Editable s) {
		Log.d(LOG_TAG, "called handleTextChange");
		if (!isEmailPatternMatch(s)) {
			// Strings have to be added to xml instead of hardcoded.
			NotificationView popup = new NotificationView(
					resetPasswordView.getContext());
			popup.launch("Email input was not valid!", "RED", "WHITE", true);
			return false;
		}
		return true;
	}

	/**
	 * Checks if email is a valid pattern match (Has an @, spaces, dots, no
	 * symbols etc)
	 */
	private boolean isEmailPatternMatch(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	/** checks of email is in the network (sends a new email to this if valid */

	@SuppressWarnings("unchecked")
	private boolean validEmail(String userEmail) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				2);
		nameValuePairs.add(new BasicNameValuePair("userEmail", userEmail));
		nameValuePairs.add(new BasicNameValuePair("action", "forgot"));
		
		try {
			AsyncTask<ArrayList<NameValuePair>, Void, Boolean> sent = new LongOperation();
			return sent.execute(nameValuePairs).get();
			
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, e.toString());
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return false;

	}
	
//As per tutorial on http://sankarganesh-info-exchange.blogspot.com/p/need-and-vital-role-of-asynctas-in.html
	private class LongOperation extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {
	
		@Override
		protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
			return userAccountManager.userChange(params[0]);
		}
	}	
}
