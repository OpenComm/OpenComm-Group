/**
 * Controller called from LoginView. Takes care of login functionality.
 * It connects to the server and returns true if the login is a success.
 * If Login is successful it calls DashboardView.
 *
 * Issues [TODO]
 * - No login incorrect message on screen
 * - ProgressDialog is visible even if login is incorrect
 * - For any other issues search for string "TODO"
 *
 * @author rahularora[hcisec], vinaymaloo[ui]
 * */

package edu.cornell.opencomm.controller;

import java.util.concurrent.locks.ReentrantLock;

import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.NotificationView;

public class LoginController {
	private LoginView loginView;

	// Debugging
	private static final boolean D = Values.D;

	// Logs
	private static final String LOG_TAG = "LoginController";

	// Check successful login
	private boolean islogin;

	private enum ReturnState{SUCEEDED, COULDNT_CONNECT, WRONG_PASSWORD, ALREADY_CLICKED};

	// Username and password strings
	private String username;
	private String password;

	private ReentrantLock loginLock = new ReentrantLock();

	// Instance of XMPP connection
	public static NetworkService xmppService;

	public LoginController(LoginView loginView) {
		this.loginView = loginView;
	}

	public void handleLoginButtonClick(final EditText usernameEdit, final EditText passwordEdit) {
		new LoginTask().execute(usernameEdit.getText().toString(), passwordEdit.getText().toString());

	}

	private class LoginTask extends AsyncTask<String, Void, ReturnState> {

		@Override
		protected ReturnState doInBackground(String... strings) {
			if(loginLock.isLocked()) return ReturnState.ALREADY_CLICKED;
			loginLock.lock();
			try{
				if (D) {
					Log.d(LOG_TAG, "Android app is attempting to connect to the server");
					username = Network.DEBUG_USERNAME;
					password = Network.DEBUG_PASSWORD;
				}
				else{
					//username = strings[0];
					
					String[] temp = strings[0].split("@");
					try{
						username = temp[0]+temp[1];
					}
					catch (ArrayIndexOutOfBoundsException e) {
						username = temp[0];
					}
					password = strings[1];
				}

				if (D) Log.d(LOG_TAG, "Got Here1");

				xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
				if (xmppService.isConnected()){
					if (D) {
						Log.d(LOG_TAG, xmppService.toString());
						Log.d(LOG_TAG, "XMPP Connection established");

					}
				}
				else {
					return ReturnState.COULDNT_CONNECT;
				}

				/** Check whether the login is successful or not
				 * In case it is, start DashboardView using Intent else, [TODO]
                 / @author: rahularora, vinaymaloo **/
				if (D) Log.d(LOG_TAG, "Got Here2");
				try {
					islogin = xmppService.login(username, password);
				} catch(XMPPException e) {
					return ReturnState.WRONG_PASSWORD;
				}

				if (D) Log.d(LOG_TAG, "Got Here3");
				if (islogin){
					Intent i = new Intent(loginView, DashboardView.class);
					i.putExtra(Network.KEY_USERNAME, username);
					i.setAction(Network.ACTION_LOGIN);

					loginView.startActivity(i);
					loginView.finish();
					return ReturnState.SUCEEDED;
				}

				else{
					return ReturnState.WRONG_PASSWORD;
				}
			}
			catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				loginLock.unlock();
			}
			if (D) Log.d(LOG_TAG, "Got Here5");
			return ReturnState.COULDNT_CONNECT;
		}

		@Override
		protected void onPostExecute(ReturnState state) {
			if(state == ReturnState.WRONG_PASSWORD) {
				NotificationView nv=new NotificationView(loginView);
				nv.launch("incorrect username or password","RED", "WHITE", true);
				Log.v(LOG_TAG, "Login failed for username "+username+" failed");
			} else if(state == ReturnState.COULDNT_CONNECT) {
				NotificationView nv=new NotificationView(loginView);
				nv.launch("Could not connect to server","RED", "WHITE", true);
				Log.v(LOG_TAG, "Could not connect to server.");
			}
		}
	}
}
