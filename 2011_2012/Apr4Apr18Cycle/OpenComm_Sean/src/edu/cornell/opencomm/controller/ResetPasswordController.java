/**
 * Reset the password
 *
 * Issues [TODO]
 * - Find the best way among the three to reset a password
 *
 * @author fl4v, cl587
 * */

package edu.cornell.opencomm.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import 	java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PopupNotificationView;
import edu.cornell.opencomm.view.ResetPasswordView;
import edu.cornell.opencomm.view.SignupView;
	
	public class ResetPasswordController {
	
	    private ResetPasswordView resetPasswordView;

	    // Debugging
	    private static final boolean D = Values.D;

	    // Logs
	    private static final String LOG_TAG = "ResetPasswordController";

	    
	    // Username string
	    private String username;
	    
	    // Network service and Connection
	    private NetworkService xmppService;
	    private XMPPConnection xmppConn;
		private String serverName = "199.167.198.149";


	   public ResetPasswordController(ResetPasswordView resetPasswordView) {
		    this.xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
		    this.xmppConn = xmppService.getXMPPConnection();
	        this.resetPasswordView = resetPasswordView;

	    }

	   /**Finds EditText from the view, writes the string rep of its contents to String uesrname */
	   public void findUsername(){
		   final EditText resetEditText = resetPasswordView.getResetUsername();
		   if (resetEditText != null){
	        username = resetEditText.getText().toString();
	        Log.d(LOG_TAG, "username input: " + username);}
	   }
	   
	   /**Dismisses the popup if email is in database and send a new email to the address. Otherwise, popup prompt clarifying the user's error */
	    public void handleResetButtonClick() {
	    	
	    	resetPasswordView.getResetOverlay().setVisibility(View.VISIBLE);
	    	findUsername();
	    	//Does 1 last local email check with Android matcher
	    	if(!handleTextChange(resetPasswordView.getResetUsername().getText())){
	    		resetPasswordView.getResetOverlay().setVisibility(View.INVISIBLE);
	    		return;
	    	}
	    	//Checks network for email validation
	    	if (validEmail(username)){
	     	NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	//Should use a string xml
	     	//popup.launch("User inputted valid email, password sent.","RED","WHITE", true);
	        // Dismisses the window for now
	     	 PopupNotificationView popupNotificationView = new PopupNotificationView(resetPasswordView.getContext(), null, "sent", "New password sent to email.", "", 1);
	     	 popupNotificationView.createPopupWindow();
	        resetPasswordView.getWindow().dismiss();}
	    	else{
	    		resetPasswordView.getResetOverlay().setVisibility(View.INVISIBLE);
	    		NotificationView popup1 = new NotificationView(resetPasswordView.getContext());
		    	//Should use a string xml
		     	popup1.launch("Username/email not found in database. Please try again.","RED", "WHITE",true);
	    		
	    	}
	    	Log.d(LOG_TAG, "reset password button clicked");
	    }
	    
	    /**Dismisses this popup and call JP's account creation popup */
	    public void handleSignUpButtonClick() {
	    	resetPasswordView.getSignUpOverlay().setVisibility(View.VISIBLE);
	    	findUsername();
            LayoutInflater ifl = (LayoutInflater) resetPasswordView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            SignupView suv= new SignupView(resetPasswordView.getContext());
            suv.launch();
	        // Dismisses the window for now
           // NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	//popup.launch("Sign up page here","RED","WHITE", true);
	        resetPasswordView.getWindow().dismiss();
	    	Log.d(LOG_TAG, "sign up button clicked");
	    }

	    /**Handler for when the textbox changes */
		public boolean handleTextChange(Editable s) {
			Log.d(LOG_TAG,"called handleTextChange");
			if(!isEmailPatternMatch(s)){
				//Strings have to be added to xml instead of hardcoded. 
				NotificationView popup = new NotificationView(resetPasswordView.getContext());
				popup.launch("Email input was not valid!","RED", "WHITE", true);
				return false;
			}
			return true;
		}
	
		/**Checks if email is a valid pattern match (Has an @, spaces, dots, no symbols etc)*/
		private boolean isEmailPatternMatch(CharSequence email) {
			   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
		
		/**checks of email is in the network (sends a new email to this if valid */
	
	private StringBuilder inputStreamToString(InputStream is) {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    // Read response until the end
	    try {
			while ((line = rd.readLine()) != null) { 
			    total.append(line); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG_TAG,"IO error");
		}
	    
	    // Return full string
	    return total;
	}
	
	private boolean sendEmail(String userEmail) {
		//Code from http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
		//and http://www.androidsnippets.com/get-the-content-from-a-httpresponse-or-any-inputstream-as-a-string
		//
		// Sends an email via a script on page mail.php on the server

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://"+serverName+"/mail.php");
        HttpResponse response=null;
        InputStream verificationCode = null;
        try {
            // Add your data
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userEmail", userEmail));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
			Log.e(LOG_TAG,"IO Protocol error");
			return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
			Log.e(LOG_TAG,"IO error");
			return false;
        }
        
		try {
			verificationCode = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG_TAG,"IllegalState error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG_TAG,"IO error");
		}
		
		String verificationString = inputStreamToString(verificationCode).toString();
		if(D) Log.v(LOG_TAG, verificationString);
		Log.v(LOG_TAG, "The string: "+verificationString);
		
		if(verificationString.equals("OK")) return true;
		else if(verificationString.equals("PB1")) {
			Log.e(LOG_TAG,"Problem sending the email");
			return false;
		}
		else if(verificationString.equals("PB2")) {
			Log.e(LOG_TAG,"The email address is not valid");
			return false;
		}
		else {
			Log.e(LOG_TAG,"Unknown error. String: "+ verificationString);
			return false;
		}
	}
		
	private boolean validEmail(String userEmail){		
		
		return sendEmail(userEmail);
			
			/* SEND AN HTTP REQUEST TO A REMOTE SERVER TO CHANGE THE PASSWORD FOR THE GIVEN USERNAME */
			/*
			//search for the user information in the database using an http post for a php script
			InputStream is=null;
			
			try{
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://example.com/phpscript&id="+userEmail);
			        HttpResponse response = httpclient.execute(httppost);
			        HttpEntity entity = response.getEntity();
			        is = entity.getContent();
			}catch(Exception e){
			        Log.e(LOG_TAG, "Error in http connection "+e.toString());
			        return false;
			}
			//convert the result to a string
			//the result is 1 for success, 0 in the other case
			try {
				if(is.read()==0) return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(LOG_TAG, "IO error: "+e.toString());
				return false;
			}
			*/
			
			//TODO: Server side in PHP: make the HTTP request. Ressource here: http://www.igniterealtime.org/projects/openfire/plugins/userservice/readme.html
			
		}
}
