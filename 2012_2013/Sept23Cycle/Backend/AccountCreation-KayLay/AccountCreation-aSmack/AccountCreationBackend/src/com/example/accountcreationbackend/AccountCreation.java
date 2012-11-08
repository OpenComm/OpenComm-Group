package com.example.accountcreationbackend;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;

//import com.example.xmpp_demo_android.Exception;
//import com.example.xmpp_demo_android.String;

import android.util.Log;

public class AccountCreation {

	public static final String TAG = "Network";
	XMPPConnection connection = null;
	AccountManager accountManager = null;

	public AccountCreation() {
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		accountManager = new AccountManager(connection);
		connect();
	}

	public void connect() {
		try {
			connection.connect();

		} catch (XMPPException e) {
			Log.v(TAG,
					"There was an error in the connect process of the account creation");
		}
	}

	//CREATE AN ACCOUNT
	public void creation(String username, String password) {
		try {
			if (accountManager.supportsAccountCreation()) {
				accountManager.createAccount(username, password);
				Log.v(TAG, "Successful account creation");
			}
			else {
				Log.v(TAG, "Account Creation is not supported");
				Log.v(TAG,
						"Account Instructions: "
								+ accountManager.getAccountInstructions());
			}
		} catch (Exception e) {
			Log.v(TAG, "Error in account creation");
		}
	}
	
	public void creation(){
		if(accountManager.supportsAccountCreation()){
		Log.v(TAG, "This server supports account creation");
		}
		
		try {
			accountManager.createAccount("kevinlei00001", "kevinlei");
		} catch (XMPPException e) {
			Log.v(TAG, "problem in 'creation()'");
		}
		
		
	}
	
	//Message to be printed after successful creation
	public void accountMessage(){
		Log.v(TAG, "I am finshed creating the account");
	}

}
