package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

public class LoginController {
    private LoginView loginView;

    // Debugging
    private static final boolean D = true;

    // Logs
    private static final String LOG_TAG = "LoginController";
    
    // Check successful login
    private boolean islogin;

<<<<<<< HEAD
	// Debugging
	private static final boolean D = true;
	
	// Logs
	private static final String LOG_TAG = "LoginController";
=======
    // Instance of XMPP connection
    public static NetworkService xmppService;
    public static ConnectionConfiguration xmppConfiguration;
    public static XMPPConnection xmppConnection;
>>>>>>> cfd6609d1ff8bb0be7edc24bb146109b9d12501a

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void handleLoginButtonClick(EditText usernameEdit, EditText passwordEdit) {
        loginView.getLoginOverlay().setVisibility(View.VISIBLE);
        
        //[TODO] VINAY This does not end if the login is incorrect
        ProgressDialog.show(this.loginView, "", "Loading. Please wait...", true);

        if (D) {
        	Log.d(LOG_TAG, "handleLogin: Attempting to connect to the server");
        }
        
        if (xmppService == null) {
            try {
            	xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
                if (D){
                	Log.d(LOG_TAG, xmppService.toString());
                	Log.d(LOG_TAG, "onStart: Network service started");
                }
            } catch (XMPPException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "onStart: XMPP Connection not established");
                loginView.finish();
            }
        }
        
        try {
			if (D) {
                islogin = xmppService.login(Network.DEBUG_USERNAME, Network.DEBUG_PASSWORD);
            }
            else {
                // login to the server using username and password input by user
                islogin = xmppService.login(usernameEdit.getText().toString(), passwordEdit.getText().toString());
                
            }
            
			if (islogin){
				Intent i = new Intent(loginView, DashboardView.class);
	            i.putExtra(Network.KEY_USERNAME, (D ? Network.DEBUG_USERNAME : usernameEdit.getText().toString()));
	            i.setAction(Network.ACTION_LOGIN);

	            loginView.startActivity(i);
			}
            
			//[TODO] VINAY - Remove this code if you do not want it. 
            //dialog.dismiss();
            /*LayoutInflater inflater = (LayoutInflater) loginView
    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		DashboardView dashboardView = new DashboardView(inflater);
    		loginView.finish();
    		dashboardView.launch();*/
        }
        catch (XMPPException e) {
            Log.e(LOG_TAG, "handleLogin: Log in failed");
            return;
        }

        
    }
}
