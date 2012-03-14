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

import android.app.ProgressDialog;
import android.content.Intent;
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
    
    // Username and password strings
    private String username;
    private String password;

    // Instance of XMPP connection
    public static NetworkService xmppService;

   public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void handleLoginButtonClick(EditText usernameEdit, EditText passwordEdit) {
        loginView.getLoginOverlay().setVisibility(View.VISIBLE);
        
        //ProgressDialog.show(this.loginView, "", "Loading. Please wait...", true);

        /** Take care of all Debug related things
         */
        if (D) {
        	Log.d(LOG_TAG, "Android app is attempting to connect to the server");
        	username = Network.DEBUG_USERNAME;
        	password = Network.DEBUG_PASSWORD;
        }
        else{
        	username = usernameEdit.getText().toString();
        	password = passwordEdit.getText().toString();
        }
        
        /** Check whether the XMPP connection is already established or not
        / @author: rahularora **/
        if (xmppService == null) {
        	xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
        	if (!xmppService.isConnected()){
        		loginView.finish();
        	}
            if (D){
              	Log.d(LOG_TAG, xmppService.toString());
               	Log.d(LOG_TAG, "XMPP Connection established");
            }
        }
        else{
        	if (D) {
            	Log.d(LOG_TAG, "XMPP Connection already established. No need to connect to the server.");
            }
        }
        
        /** Check whether the login is successful or not
         * In case it is, start DashboardView using Intent else, [TODO]
        / @author: rahularora, vinaymaloo **/
        
		islogin = xmppService.login(username, password);
            
		if (islogin){
			//Start DashboardView when the login is successful
			Intent i = new Intent(loginView, DashboardView.class);
			i.putExtra(Network.KEY_USERNAME, username);
	        i.setAction(Network.ACTION_LOGIN);

	        loginView.startActivity(i);
		}
			
		else{
			//[TODO] Do something to remove ProgressDialog here.
			NotificationView nv=new NotificationView(loginView);
			nv.launch("incorrect username or password","RED", "WHITE", true);
			Log.v(LOG_TAG, "Login failed for username "+username+" failed");
		}
            
		//[TODO] VINAY - Remove this code if you do not want it. 
        //dialog.dismiss();
        /*LayoutInflater inflater = (LayoutInflater) loginView .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	DashboardView dashboardView = new DashboardView(inflater);
    	loginView.finish();
    	dashboardView.launch();*/
        
    }
}
