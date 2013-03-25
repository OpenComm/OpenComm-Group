package edu.cornell.opencomm.controller;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import edu.cornell.opencomm.view.CreateAccountView;
import edu.cornell.opencomm.controller.AccountController;

/**
 * Author: Scarlet Yang (back-end) last edit: 3/25
 * */
public class CreateAccountController {
	@SuppressWarnings("unused")
	private static final String TAG = CreateAccountController.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean D = true;
	/**
	 * The View
	 */
	private CreateAccountView createAccountView;
	private AccountController accountController;
	/**
	 * The constructor
	 * 
	 * @param view
	 */
	public CreateAccountController(CreateAccountView view) {
		this.accountController = new AccountController();
		this.createAccountView = view;
	}	
	
	/*
	 * Pops a toast when the button 
	 *  creates the user by using {@link edu.cornell.opencomm.controller.AccountController}
	 *  uses the values from the view using its getters.
	 *  returns true if account is successfully created
	 *  and false if it fails
	 */
	public boolean handleSignUp(){
		/*AccountController takes: String username, String nickname, String email,
			String firstName, String lastName, String phoneNumber,
			InputStream photo, String title, String password*/
		try{
			String username = createAccountView.getUsernameTextBox().getText().toString();
			String nickname = createAccountView.getFirstNameTextBox().getText().toString();
			String email = createAccountView.getEmailTextBox().getText().toString();
			String firstName = nickname;
			String password = createAccountView.getPasswordTextBox().getText().toString();
//			System.out.println("success");
			AccountController.createAcccount(username, nickname, email, firstName, 
					"", "", null, "", password);
//			System.out.println("success");
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
