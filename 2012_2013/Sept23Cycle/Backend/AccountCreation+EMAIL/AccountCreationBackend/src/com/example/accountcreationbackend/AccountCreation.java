package com.example.accountcreationbackend;

import java.util.HashMap;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

public class AccountCreation {
	
	private String first;
	private String last;
	private String username;
	private String password;
	private String title;
	private String email;

	public static final String TAG = "AccountCreation";
	XMPPConnection connection = null;
	AccountManager accountManager = null;

	public AccountCreation() {
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		// accountManager = new AccountManager(connection);
		accountManager = connection.getAccountManager();
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

	// CREATE AN ACCOUNT
	public void creation(String first, String last, String title, String username, String password, String email) {
		this.first=first;
		this.last=last;
		this.username=username;
		this.password=password;
		this.email=email;
		
		
		try {
			if (accountManager.supportsAccountCreation()) {
				
				HashMap<String, String> attr = new HashMap<String, String>();
				attr.put("firstname", first);
				attr.put("lastname", last);
				attr.put("title", title);
				//accountManager.createAccount(username, password);
				
				GMailerClass m= new GMailerClass();
				
				m.enableAccount(this.email, this.username, this.password);
				
				//m.enableAccount(userEmail, createAccountPassword, userName)
				
				Log.v(TAG, "Successful account creation");
			} else {
				Log.v(TAG, "Account Creation is not supported");
				Log.v(TAG,
						"Account Instructions: "
								+ accountManager.getAccountInstructions());

			}
		} catch (Exception e) {
			Log.v(TAG, "Error in account creation");
		}

	}

	//HARDCODED CREATION TOY
	public void creation() {
		if (accountManager.supportsAccountCreation()) {
			Log.v(TAG, "This server supports account creation");
		}

		try {

			HashMap<String, String> attr = new HashMap<String, String>();
			attr.put("firstname", "kevin");
			attr.put("lastname", "lei");
			attr.put("title", "mr.kevinlei");

			accountManager.createAccount("kevinlei000001", "kevinlei");
		} catch (XMPPException e) {
			Log.v(TAG, "problem in 'creation()'");
			Log.v(TAG, e.getMessage());
		}

	}

	// Message to be printed after successful creation
	public void accountMessage() {
		Log.v(TAG, "I am finshed creating the account");
	}
	
	//getters 
	
	public String getFirst(){
		return first;
	}
	
	public String getLast(){
		return last;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getEmail(){
		return email;
	}

}
