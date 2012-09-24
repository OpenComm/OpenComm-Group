package edu.cornell.opencomm.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;
import edu.cornell.opencomm.Values;

public class UserAccountManager {
	// Debugging
	private static final String TAG = "UserAccountManager";
	private static final boolean D = Values.D;
	private String serverName = "199.167.198.149";
	private static AccountManager accountManager;
	
	public UserAccountManager(){
		
		} 
	/* 
	 * Constructor: Account manager for creating editing 
	 * and deleting accounts
	 */
	public UserAccountManager(XMPPConnection xmppConnection){
		accountManager = xmppConnection.getAccountManager();
	} 
	
	//Deprecated
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
	
	//Deprecated
	public boolean deleteUser(String username){
		return false;
	}

	public boolean userChange(ArrayList<NameValuePair> nameValuePairs) {
		// Code from
		// http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
		// and
		// http://www.androidsnippets.com/get-the-content-from-a-httpresponse-or-any-inputstream-as-a-string
		//
		// Sends an email via a script on page mail.php on the server

		Log.v(TAG, "Send " + nameValuePairs.toString());
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://" + serverName + "/userchange.php");
		HttpResponse response = null;
		InputStream verificationCode = null;
		try {
			// Add data
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			Log.v(TAG, "Request sent to "+"http://" + serverName + "/userchange.php");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IO Protocol error"+e.toString());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IO error: "+e.toString());
			return false;
		}

		try {
			verificationCode = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "IllegalState error");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "IO error");
			return false;
		}

		String verificationString = inputStreamToString(verificationCode).toString();
		
		if (verificationString.equals("ok"))
			return true;
		else if (verificationString.equals("PB1")) {
			Log.e(TAG, "Problem sending the email");
			return false;
		} 
		else if (verificationString.equals("PB2")) {
			Log.e(TAG, "Connection error to open fire");
			return false;
		}
		else if (verificationString.equals("PB3")) {
			Log.e(TAG, "Invalid response from the server");
			return false;
		}
		else if (verificationString.equals("PB4")) {
			Log.e(TAG, "Invalid action parameter in the POST");
			return false;
		}
		else if (verificationString.equals("PB5")) {
			Log.e(TAG, "The email address is not valid");
			return false;
		}
		else {
			Log.e(TAG, "Openfire error. String: " + verificationString);
			return false;
		}
	}
	
	private StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		
		Log.v(TAG,"convertion: "+ is.toString());
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "Conversion error");
		}

		// Return full string
		return total;
	}
}
