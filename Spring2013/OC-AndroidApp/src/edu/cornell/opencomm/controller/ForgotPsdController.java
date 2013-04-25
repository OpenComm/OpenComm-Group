package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.EnhancedAccountManager;
import edu.cornell.opencomm.model.ResetPasswordReturnState;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.util.Util;
import edu.cornell.opencomm.view.ForgotPsdView;
import edu.cornell.opencomm.view.LoginView;


/**
  * Controller for forgot password screen. 
  * @author Lu Yang[frontend]
  */

public class ForgotPsdController {
	
	/**
	 * The View
	 */
	private ForgotPsdView forgotPsdView;
	
	/**
	 * The constructor
	 **/
	public ForgotPsdController(ForgotPsdView view){
		this.forgotPsdView = view;
	}
	
	private ProgressDialog loginProgress;
	
	public void handleSendEmailClick(String email){
		//check validity of input
		boolean emptyEmail = (email == null || email.length() == 0);
		boolean invalidEmail = !Util.validateString(email, Util.EMAIL_ADDRESS_PATTERN);
		if (emptyEmail || invalidEmail) {
			StringBuilder errorText = new StringBuilder();
			errorText.append("Error:\n");
			if (emptyEmail) errorText.append("\tEmail is required\n");
			else if (invalidEmail) errorText.append("\tInvalid email\n");
			errorText.append("Please try again.");
			
			// show a toast describing the error
			Toast.makeText(this.forgotPsdView.getApplicationContext(),errorText.toString(), Toast.LENGTH_SHORT).show();
		} else {
			//Start Async Task
			new ResetPassword().execute(email);
		}
	}

	
	private class ResetPassword extends AsyncTask<String, Void, ResetPasswordReturnState> {
		
		@Override
		protected void onPreExecute() {
			loginProgress = new ProgressDialog(forgotPsdView);
			loginProgress.setIcon(forgotPsdView.getResources().getDrawable(
					R.drawable.icon));
			loginProgress.setTitle("Attempting to reset password");
			loginProgress.setMessage("Please wait...");
			loginProgress.show();
		}

		@Override
		protected ResetPasswordReturnState doInBackground(String... params) {
			String jid = SearchService.getJid(params[0]);
			if (jid == "0"){
				return ResetPasswordReturnState.NO_EMAIL;
			} else {
				return EnhancedAccountManager.resetPassword(jid, params[0]);
			}
		}
		
		protected void onPostExecute(ResetPasswordReturnState returnState) {
			loginProgress.dismiss();
			switch (returnState) {
			case SUCCEEDED:
				Toast.makeText(forgotPsdView.getApplicationContext(), "Password reset successfuly and sent to you email", Toast.LENGTH_SHORT).show();
				Intent click = new Intent(forgotPsdView, LoginView.class);
				forgotPsdView.startActivity(click);
				break;
			case NO_EMAIL:
				Toast.makeText(forgotPsdView.getApplicationContext(), "Email address not found", Toast.LENGTH_SHORT).show();
				break;
			case SERVER_ERROR:
				Toast.makeText(forgotPsdView.getApplicationContext(), "Server error, please try again later", Toast.LENGTH_SHORT).show();
				break;
		}
		}
		
	}
}
	

