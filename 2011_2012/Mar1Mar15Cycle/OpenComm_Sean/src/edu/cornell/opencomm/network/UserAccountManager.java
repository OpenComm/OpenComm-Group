package edu.cornell.opencomm.network;

import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;
import edu.cornell.opencomm.Values;

public class UserAccountManager {
	// Debugging
	private static final String TAG = "NetworkService";
	private static final boolean D = Values.D;
	
	private static AccountManager accountManager;
	
	/* 
	 * Constructor: Account manager for creating editing 
	 * and deleting accounts
	 */
	public UserAccountManager(XMPPConnection xmppConnection){
		accountManager = xmppConnection.getAccountManager();
	} 
	
	public boolean createUser(String email,String password, String firstname, String lastname, 
								 String title){
		try {
			HashMap<String, String> attr = new HashMap<String, String>(); 
			attr.put("firstname", firstname); 
			attr.put("lastname", lastname); 
			attr.put("title", title); 

			accountManager.createAccount(email, password, attr);
			if (D){
				Log.d(TAG, "Create user done");
			}
			return true;
		} catch (XMPPException e) {
			if (D){
				Log.d(TAG, "Create user failed");
				Log.d(TAG,e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
		     
	}
	
	public boolean deleteUser(String username){
		return false;
	}

}
