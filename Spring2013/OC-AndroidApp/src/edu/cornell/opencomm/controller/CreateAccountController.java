package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.CreateAccountReturnState;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.view.DashboardView;

/**
 * @author Scarlet Yang (back-end) last edit: 3/25
 * Last Edited: Antoine Chkaiban (back-end) last edit 04/22/2013
 * */
public class CreateAccountController {
	private static final String TAG = CreateAccountController.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The View
	 */
	private CreateAccountView createAccountView;
	
	/**
	 * The constructor
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		this.createAccountView = view;
	}
	
	private ProgressDialog loginProgress;

	/*
	 * starts Async Task to create User on server, and the VCard.
	 * Pops a Toast onPostExecute, and goes to Dashboard only if successful.
	 */
	public void handleSave(String fName, String lName, String email, String username, String pwd) {
		
		Log.v(TAG, fName+" "+lName+" "+email+" "+username+" "+pwd);
		
		//Start Async Task
		String[] userInfo = {username, email, fName, lName, pwd};
		new CreateUser().execute(userInfo);
		
	}
	
	/** 
	 * Creates user based on given inputs
	 * @param String[] userInfo
	 **/
	//TO DO: ADD DIALOG
	private class CreateUser extends AsyncTask<String, Void, CreateAccountReturnState> {
		
		@Override
		protected void onPreExecute() {
			loginProgress = new ProgressDialog(createAccountView);
			loginProgress.setIcon(createAccountView.getResources().getDrawable(
					R.drawable.icon));
			loginProgress.setTitle("Attempting Account Creation");
			loginProgress.setMessage("Please wait...");
			loginProgress.show();
		}
		

		@Override
		protected CreateAccountReturnState doInBackground(String... params) {
			if (SearchService.emailAlreadyExists(params[3])) {
				return CreateAccountReturnState.EMAIL_EXISTS;
			}
			else if (SearchService.jidAlreadyExists(params[0])) {
				return CreateAccountReturnState.JID_EXISTS;
			}
			else {
				return EnhancedAccountManager.createAccount(params[0], params[1], params[2],params[3], params[4]); 
			}
							
		}

		// TODO launch the next activity
		@Override
		protected void onPostExecute(CreateAccountReturnState returnState) {
			loginProgress.dismiss();
			switch (returnState) {
				case SUCCEEDED:
					Toast.makeText(createAccountView.getApplicationContext(), "Account created successfuly", Toast.LENGTH_SHORT).show();
					Intent click = new Intent(createAccountView, DashboardView.class);
					createAccountView.startActivity(click);
					break;
				case EMAIL_EXISTS:
					Toast.makeText(createAccountView.getApplicationContext(), "Email address already registered with OpenComm", Toast.LENGTH_SHORT).show();
					break;
				case JID_EXISTS:
					Toast.makeText(createAccountView.getApplicationContext(), "Username already registered with OpenComm", Toast.LENGTH_SHORT).show();
					break;
				case SERVER_ERROR:
					Toast.makeText(createAccountView.getApplicationContext(), "Server error during account creation, please try again later", Toast.LENGTH_SHORT).show();
					break;
			}
			
			
		}
	}
}
