package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Account;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for new user account creation.
 * @author jonathan
 */
public class SignupController {

    private static String TAG = "SignupController";
    private static boolean D = Values.D;
    private SignupView view;


    public void handleSaveButtonClick() {
        String username = view.getFirstNameBox().getText().toString();
        String password = view.getlastNameBox().getText().toString();
        String title = view.getTitleBox().getText().toString();
        Account account = new Account(username, password, title);
        //TODO: Do something with the account
    }

    public void handleCancelButtonClick() {
        view.dismiss();
    }
}
