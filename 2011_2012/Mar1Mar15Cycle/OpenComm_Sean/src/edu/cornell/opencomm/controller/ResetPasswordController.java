/**
 * Reset the password
 *
 * Issues [TODO]
 * - Find the best way among the three to reset a password
 *
 * @author fl4v, cl587
 * */

package edu.cornell.opencomm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jivesoftware.smack.XMPPConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.NotificationView;
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
	    	findUsername();
	    	//Checks network for email validation
	    	if (validEmail(username)){
	     	NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	//Should use a string xml
	     	popup.launch("User inputted valid email, password sent.","RED","WHITE", true);
	        // Dismisses the window for now
	    	
	        resetPasswordView.getWindow().dismiss();}
	    	else{
	    		NotificationView popup1 = new NotificationView(resetPasswordView.getContext());
		    	//Should use a string xml
		     	popup1.launch("Username/email not found in database. Please try again.","RED", "WHITE",true);
	    		
	    	}
	    	Log.d(LOG_TAG, "reset password button clicked");
	    }
	    
	    /**Dismisses this popup and call JP's account creation popup */
	    public void handleSignUpButtonClick() {
	    	//Network email check not enforced - I think its redundent if this will be enforced in JP's screen anyhow */
	    	findUsername();
            LayoutInflater ifl = (LayoutInflater) resetPasswordView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            SignupView suv= new SignupView(resetPasswordView.getContext());
            suv.launch();
	        // Dismisses the window for now
            NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	popup.launch("Sign up page here","RED","WHITE", true);
	        resetPasswordView.getWindow().dismiss();
	    	Log.d(LOG_TAG, "sign up button clicked");
	    }

	    /**Handler for when the textbox changes */
		public boolean handleTextChange(Editable s) {
			Log.d(LOG_TAG,"called handleTextChange");
			if(!isEmailPatternMatch(s)){
				s.clear();
				//Strings have to be added to xml instead of hardcoded. 
				//BUG: Current popup is invisible - its behind the current window
				NotificationView popup = new NotificationView(resetPasswordView.getContext());
				popup.launch("Wrong email!","RED", "WHITE", true);
				return false;
			}
			return true;
		}
	
		/**Checks if email is a valid pattern match (Has an @, spaces, dots, no symbols etc)*/
		private boolean isEmailPatternMatch(CharSequence email) {
			   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
		
		/**checks of email is in the network (sends a new email to this if valid */
	private boolean validEmail(String userEmail){
			/* FIRST WAY TO GO */
			
			//Open a web browser and passing it the email
			//String url = "http://(website).com&id="+userEmail;
			String url = "http://128.84.18.99/test.php";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			resetPasswordView.getContext().startActivity(i);			
			
			/* SECOND WAY */
			/*
			//search for the user information in the database using an http post for a php script
			InputStream is=null;
			
			try{
			        HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://example.com/(website).phpscript&id="+userEmail);
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
			/* LAST WAY */ //to do it would be with the openfire API in my opinion but we have to look into that
			
			//TODO: Server side in PHP: send email, generate new password, change it
			
			return true;
		}
}
