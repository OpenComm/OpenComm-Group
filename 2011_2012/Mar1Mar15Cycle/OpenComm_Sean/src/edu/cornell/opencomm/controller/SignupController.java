package edu.cornell.opencomm.controller;

import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Account;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for new user account creation.
 * @author jonathan
 */
public class SignupController {

    private static String TAG = "SignupController";
    private static boolean D = Values.D;
    private Context context;
    private SignupView view;

    public SignupController(SignupView view, Context context) {
        if (D) Log.d(TAG, "SignupController constructor called");
        this.view = view;
        this.context = context;
    }

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
            "(" +
            "." +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
            ")+"
        );
    public final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]*");

    public void handleSaveButtonClick() {
        if (D) Log.d(TAG, "handleSaveButtonClick called");
        String username = view.getFirstNameBox().getText().toString();
        String password = view.getLastNameBox().getText().toString();
        String title = view.getTitleBox().getText().toString();
        Account account = new Account(username, password, title);
        //TODO: Do something with the account
    }

    public void handleFirstNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleFirstNameFocusChange called");
        if(!hasFocus) {
            boolean valid = validateName(view.getFirstNameBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid First Name (no spaces allowed)","RED","WHITE", true);
            }
        }
    }

    public void handleLastNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleLastNameFocusChange called");
        if(!hasFocus) {
            boolean valid = validateName(view.getLastNameBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid Last Name (no spaces allowed)","RED","WHITE", true);
            }
        }
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

    public void handleCancelButtonClick() {
        if (D) Log.d(TAG, "handleCancelButtonClick called");
        view.dismiss();
    }

    private boolean validateEmail(String email) {
        if (D) Log.d(TAG, "validateEmail called");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean validateName(String name) {
        if (D) Log.d(TAG, "validateName called");
        return NAME_PATTERN.matcher(name).matches();
    }


}
