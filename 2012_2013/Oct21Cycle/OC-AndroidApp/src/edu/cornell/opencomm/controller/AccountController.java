package edu.cornell.opencomm.controller;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;

import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

public final class AccountController {
	private AccountManager accountManager;
	private VCard vCard;
	
	private static final String TAG = "Controller.AccountController";

	public AccountController() {
		this.accountManager = NetworkService.getInstance().getAccountManager();
		if (UserManager.PRIMARY_USER != null) {
			try {
				vCard.load(NetworkService.getInstance().getConnection(),
						UserManager.PRIMARY_USER.getUsername());
			} catch (XMPPException e) {
				Log.v(TAG, "error getting primary user's VCard");
			}
		}
	}

	public void createAcccount(String username, String nickname, String email,
			String firstName, String lastName, String phoneNumber, InputStream photo, 
			String title, String password) {
		HashMap<String, String> attributes = new HashMap<String, String>();
		Collection<String> requiredAttributes = accountManager
				.getAccountAttributes();
		for (String attr : requiredAttributes) {
			attributes.put(attr, "");
		}
		attributes.put("first", firstName);
		attributes.put("last", lastName);
		attributes.put("email", email);
		attributes.put("phone", phoneNumber);
		try {
			accountManager.createAccount(username, password, attributes);
			// create new User to populate VCard and store on server
			@SuppressWarnings("unused")
			User u = new User(firstName, lastName, email, photo,
					title, username, nickname);
		} catch (XMPPException e) {
			Log.v(TAG, "Account creation failed");
		}
	}
	
	public void changeNickname(String nickname) {
		vCard.setNickName(nickname);
	}

	public void changePhoneNumber(String number) {
		vCard.setPhoneHome("VOICE", number);
	}

	public void changeEmail(String email) {
		vCard.setEmailHome(email);
	}

	public void changeImage(byte[] image) {
		vCard.setAvatar(image);
	}

}
