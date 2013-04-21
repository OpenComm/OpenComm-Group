package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.ForgotPsdView;


/**
  * Controller for forgot password screen. 
  * @author Lu Yang[frontend]
  */

public class ForgotPsdController {
	
	private ForgotPsdView forgotPsdView;
	
	/** The constructor */
	public ForgotPsdController(ForgotPsdView view){
		this.forgotPsdView = view;
	}
	
	public void handleSendEmailClick(String email){
		String response = "-1";
		String jid = CheckEmail.getJid(email);
		if (jid != "0"){
			response = HttpRequest.forgotPassword(jid, email);
			System.out.println(response);
		}
		//TO DO (Front-end) :
		//if response = -1 : pop error: email not found
		//if response = 0 : pop error: email found but error changing password in the database
		//if response = 1 : pop toast : password reset successfully and sent to your email, then go back to login view
	}	
}
	

