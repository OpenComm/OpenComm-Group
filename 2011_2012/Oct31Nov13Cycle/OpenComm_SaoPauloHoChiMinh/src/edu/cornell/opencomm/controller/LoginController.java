package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.LoginView;

public class LoginController {
	private LoginView loginView;

	// Instance of XMPP connection
	public static NetworkService xmppService;
	public static ConnectionConfiguration xmppConfiguration;
	public static XMPPConnection xmppConnection;

	public LoginController(LoginView loginView) {
		this.loginView = loginView;
	}

	public void handleLoginButtonClick() {
		// TODO Auto-generated method stub
		
	}

	/*public void handlePopupWindowClicked() {
		loginView.getWindow().dismiss();
	}*/
}
