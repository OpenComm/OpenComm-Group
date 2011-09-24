package edu.cornell.opencomm.network.sp11;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;

public class NetworkGUI extends Activity {
	private static String LOGTAG = "NetworkGUI";
	private String host = "jabber.org";
	private int port = 5222;
	private Intent iLogin;
	private String username = "opencommss";
	private String password = "ssopencomm";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // connect via XMPP to host server and port of choice
        Log.i(NetworkGUI.LOGTAG, "Establishing connection via XMPP:\n" + 
        		"host server: " + this.host + "\nport: " + this.port);
        Log.i(NetworkGUI.LOGTAG, "Log in:\n" + 
        		"username: " + this.username + "@" + this.host + "\npassword: " + this.password);
        Intent iLogin = new Intent(this, XMPPService.class);
        iLogin.putExtra(Networks.KEY_HOST, this.host);
        iLogin.putExtra(Networks.KEY_PORT, this.port);
        iLogin.putExtra(Networks.KEY_USERNAME, this.username);
        iLogin.putExtra(Networks.KEY_PASSWORD, this.password);
        iLogin.setAction(Networks.ACTION_LOGIN);
        startService(iLogin);

    }
    
    public void onDestroy() {
    	// Log out of the account and disconnect from the host server
        stopService(iLogin);
        Log.i(NetworkGUI.LOGTAG, "Disconnected from host server");
    }
}