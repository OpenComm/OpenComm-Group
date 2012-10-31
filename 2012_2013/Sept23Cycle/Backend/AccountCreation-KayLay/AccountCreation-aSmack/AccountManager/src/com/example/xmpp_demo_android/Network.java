package com.example.xmpp_demo_android;

import android.util.Log;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

/**
 * TODO: Fill in the methods below with whatever code you need them to do. Add
 * parameters to the function headers as needed. You code should be triggered
 * when you click on the UI elements, but its output should all go to logcat. If
 * you have any questions, please email Kris ASAP.
 * 
 * @author Kris
 * 
 */
public class Network {

	public static final String TAG = "Network";
	XMPPConnection connection=null;
	AccountManager accountManager=null;
	
	public Network(){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		accountManager= new AccountManager(connection);
		connect();
		
		if(accountManager.supportsAccountCreation()){
		
		Log.i(TAG, "supports Account creation");
		creation("kevinleitest9812", "kevinlei");
		
		}
		
		else{
			Log.v(TAG, "no support!");
		}
		
	}
	
	//Creates account
	public Network(String username, String password){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		accountManager= new AccountManager(connection);
		connect();
		creation(username, password);
	}
	
	public void connect(){
		try {
			connection.connect();
			
		} catch (XMPPException e) {
			Log.v(TAG, "There was an error in the connect process of the account creation");
		}
	}
	
	
	public void creation(String username, String password){
		
		try {
			if (accountManager.supportsAccountCreation()){
			accountManager.createAccount(username, password);
			Log.v(TAG,"Successful account creation");
			}
			
			else{
				Log.v(TAG, "Account Creation is not supported");
				Log.v(TAG, "Account Instructions: " + accountManager.getAccountInstructions());
			}
			
		} catch (Exception e) {
			Log.v(TAG, "Error in account creation");
		}	
	}
	
	//DO NOT USE DELETE ACCOUNT FUNCTIONALITY AS SERVER DOES NOT SUPPORT IT!
	public void deleteAccount(String user, String pw){
		connection = new XMPPConnection("cuopencomm.no-ip.org");
		try {
			connection.connect();
			connection.login(user, pw);
			while (connection.isAuthenticated() ){
				accountManager.deleteAccount();
			}
			
			
		} catch (XMPPException e) {
			Log.v(TAG, "Error occurs deleting account");
			
		}
		catch (IllegalStateException e) {
			Log.v(TAG, "not currently logged into server");

		}
		
		
	}
	
	public void accountMessage(){
		Log.v(TAG, "I am finshed creating the account");
	}

}
