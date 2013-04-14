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
			//Implement here
			CheckEmail check = new CheckEmail(email);
			String jid = check.getJid();
			if (jid != "0"){
				HttpRequest req = new HttpRequest(jid,email);
				String response = req.getResponse();
				if (response != null) {
					System.out.println(response);
				} else {
					System.out.println("-1");
				}
			}
		}
		
}
	

