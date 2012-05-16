package edu.cornell.opencomm.controller;

import java.util.regex.Pattern;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.EmailView;
import edu.cornell.opencomm.view.NotificationView;
import android.content.Context;
import android.util.Log;

public class EmailController {

    private static String TAG = "EmailController";
    private static boolean D = Values.D;
    private Context context;
    private EmailView view;
    
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");
	
	public EmailController(EmailView emailView, Context context) {
        if (D) Log.d(TAG, "EmailController constructor called");
		this.view = emailView;
		this.context = context;
	}

	public void handleEmailFocusChange(boolean hasFocus) {
		if (D) Log.d(TAG, "handleEmailFocusChange called");
        if(!hasFocus) {
            boolean valid = validateEmail(view.getEmailBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid Email","RED","WHITE", true);
            }
        }
	}

    private boolean validateEmail(String email) {
        if (D) Log.d(TAG, "validateEmail called");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

	public void handleCancelButtonClick() {
		view.dismiss();
	}

	public void handleSendButtonClick() {
        String email = view.getEmailBox().getText().toString();
		//CALL BACKEND FUNCTION
		
	}
}


