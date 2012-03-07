
	package edu.cornell.opencomm.controller;

	import android.util.Log;
	import android.widget.EditText;
	import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.ResetPasswordView;
	
	public class ResetPasswordController {
	
	    private ResetPasswordView resetPasswordView;

	    // Debugging
	    private static final boolean D = Values.D;

	    // Logs
	    private static final String LOG_TAG = "ResetPasswordController";
	    

	    
	    // Username and password strings
	    private String username;


	   public ResetPasswordController(ResetPasswordView resetPasswordView) {
	        this.resetPasswordView = resetPasswordView;

	    }

	   public void findUsername(){
	        username = resetPasswordView.getResetUsername().getText().toString();
	        Log.d(LOG_TAG, "username input: " + username);
	   }
	    public void handleResetButtonClick() {
	    	NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	popup.launch("LOLOLOL TROLLLED","RED", true);
	    	findUsername();
	    	//TODO: Populate this thing
	        // Dismisses the window for now
	        resetPasswordView.getWindow().dismiss();
	    	Log.d(LOG_TAG, "reset password button clicked");
	    }
	    
	    public void handleSignUpButtonClick() {
	    	NotificationView popup = new NotificationView(resetPasswordView.getContext());
	    	popup.launch("LOLOLOL TROLLLED","RED", true);
	    	findUsername();
	    	//TODO: Populate this thing
	        // Dismisses the window for now
	        resetPasswordView.getWindow().dismiss();
	    	Log.d(LOG_TAG, "sign up button clicked");
	    }
	

	
}
