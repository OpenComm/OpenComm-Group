package edu.cornell.opencomm.network.sp11;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class XMPPService extends Service {
	private static String LOGTAG = "XMPPService";
	private XMPPConnection conn;
	
	public int onStartCommand(Intent intent, int flags, int startId) {		
		// if connecting via XMPP to specific host and port
		if (Networks.ACTION_LOGIN.equals(intent.getAction())) {
			String host = intent.getStringExtra(Networks.KEY_HOST);
			int port = intent.getIntExtra(Networks.KEY_PORT, 5222);
			// Configure and create new XMPP Connection to the host server through
			// the port with stream compression and SASL Auth. enabled
			XMPPConnectConfig xmppConn =
				new XMPPConnectConfig(host, port, true, true);
			Log.i(XMPPService.LOGTAG, "XMPP Connection created:\n" + xmppConn);
			conn = xmppConn.getXmppConn();
			
			// Connect to the server
			try {
				conn.connect();
			} catch (XMPPException e) {
				Log.e(XMPPService.LOGTAG, "XMPPException error: " + 
						e.getXMPPError().getCode());
				Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
		                Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			// confirm that the connection has been established
			Log.i(XMPPService.LOGTAG, "XMPP Connection successfully connected? " + 
					(conn.isConnected() ? "yes" : "no"));
			
			// Log in using the specific username and password given
			String username = intent.getStringExtra(Networks.KEY_USERNAME);
			String password = intent.getStringExtra(Networks.KEY_PASSWORD);
			
			try {
				conn.login(username + "@" + host, password);
			} catch (XMPPException e) {
				Log.e(XMPPService.LOGTAG, "XMPPException error: " + 
						e.getXMPPError().getCode());
				Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
		                Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			// confirm that the connection has been established
			Log.i(XMPPService.LOGTAG, "\tUser " + username + " successfully logged in? " 
					+ (conn.isAuthenticated() ? "yes" : "no"));
			
			// start creating/removing MUC rooms and users
			Intent iMUC = new Intent(XMPPService.this, MUCGUI.class);
			iMUC.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			iMUC.putExtra(Networks.KEY_USERNAME, username);
			iMUC.setAction(Networks.ACTION_START_MUC);
			startActivity(iMUC);
		}	
		// as we want the service to keep running until we explicitly stop it
		return START_STICKY;
	} // end onStartCommand method
	
	/** Called by the system to notify a Service that it is no longer used and 
	 * is being removed. If there is a valid connection, it disconnects it */
	public void onDestroy() {
		Log.i(XMPPService.LOGTAG, "Destroying XMPP Service...");
			// check that the connection is still valid
			if (conn != null || conn.isConnected()) {
				// disconnect
				conn.disconnect();
				// confirm
				Log.i(XMPPService.LOGTAG,"XMPP Connection disconnected? " + (conn.isConnected() ? " no" : " yes"));
			}
		Log.i(XMPPService.LOGTAG, "XMPP Service destroyed.");
	} // end onDestroy method
		
	
	public IBinder onBind(Intent intent) {
		return mBinder;
	} // end onBind method
	
	private final IBinder mBinder = new XMPPBinder();
	
	public XMPPConnection getXMPPConnection() {
		return this.conn;
	}
	
    public class XMPPBinder extends Binder {
       	XMPPService getService() {
            return XMPPService.this;
        }
    }

}



