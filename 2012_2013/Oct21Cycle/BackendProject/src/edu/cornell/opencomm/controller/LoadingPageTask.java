package edu.cornell.opencomm.controller;

import android.os.AsyncTask;

/**
 * 
 * AsyncTask associated with loading the application.  The parameter types may be altered for implementation.
 *
 */
public class LoadingPageTask extends AsyncTask<Integer,Integer,Integer> {

	/**
	 * TODO: connect to XMPP
	 */
	@Override
	protected Integer doInBackground(Integer... arg0) {
		return null;
	}

	/**
	 * TODO: Start login activity (if error, ask design team)
	 */
	@Override
	protected void onPostExecute(Integer result){
	}
}
