
	package edu.cornell.opencomm.controller;

	import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
	import android.widget.EditText;
	import edu.cornell.opencomm.Values;
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


	   public ResetPasswordController(ResetPasswordView resetPasswordView) {
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
		public void handleTextChange(Editable s) {
			Log.d(LOG_TAG,"called handleTextChange");
			if(isEmailPatternMatch(s)==false){
				s.clear();
				//Strings have to be added to xml instead of hardcoded. 
				//BUG: Current popup is invisible - its behind the current window
				   NotificationView popup = new NotificationView(resetPasswordView.getContext());
			    	popup.launch("Wrong email!","RED", "WHITE", true);
			}
			
		}
	
		/**Checks if email is a valid pattern match (Has an @, spaces, dots, no symbols etc)*/
		private boolean isEmailPatternMatch(CharSequence email) {
			   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
		
		/**checks of email is in the network (sends a new email to this if valid */
		private boolean validEmail(String userEmail){
			//network things here
			return true;
		}

	
}
