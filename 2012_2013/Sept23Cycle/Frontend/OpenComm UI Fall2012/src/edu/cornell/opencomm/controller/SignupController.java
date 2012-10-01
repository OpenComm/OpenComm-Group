package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public class SignupController {
	
	//TODO
	//1. Handle save button
	//2. Handle focus change on the textbox- guard condition to regain focus
	//3. Handle cancel button
	//4. Handle the android back button, and consume everthing else

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
