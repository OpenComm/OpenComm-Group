package edu.cornell.opencomm.controller;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

/**
 * Controller class responsible for account creation and editing the primary
 * user's account info
 * 
 * @author Kris Kooi, Brian O'Conner
 * 
 */
public class AccountController {
	private AccountManager accountManager;
	private VCard vCard;

	private static final String TAG = "Controller.AccountController";
	
	private static final String USERSERVICE_URL = "http://cuopencomm.no-ip.org/userService/userservice?";

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

	public static void createAcccount(String username, String nickname, String email,
			String firstName, String lastName, String phoneNumber,
			InputStream photo, String title, String password) {
		/* HashMap<String, String> attributes = new HashMap<String, String>();
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
			User u = new User(firstName, lastName, email, photo, title,
					username, nickname);
			this.vCard = u.getVCard();
			//TODO: store VCard on server
		} catch (XMPPException e) {
			Log.v(TAG, "Account creation failed");
		} */
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=add&username=" + username + "&password=" + password;
			requestURL += "&name=" + nickname + "&email=" + email;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		}
		catch (Exception e)
		{
			Log.v(TAG, "server cannot create user");
		}
	}

	public static void changeNickname(String nickname) {
		/* vCard.setNickName(nickname);
		try {
			vCard.save(NetworkService.getInstance().getConnection());
		} catch (XMPPException e) {
			Log.v(TAG, "error in updating nickname");
		} */
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=update&name=" + nickname;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		}
		catch (Exception e)
		{
			Log.v(TAG, "server cannot update nickname");
		}
	}

	public void changePhoneNumber(String number) {
		vCard.setPhoneHome("VOICE", number);
		try {
			vCard.save(NetworkService.getInstance().getConnection());
		} catch (XMPPException e) {
			Log.v(TAG, "error in updating phone number");
		}
	}

	public static void changeEmail(String email) {
		/* vCard.setEmailHome(email);
		try {
			vCard.save(NetworkService.getInstance().getConnection());
		} catch (XMPPException e) {
			Log.v(TAG, "error in updating email");
		} */
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=update&email=" + email;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		}
		catch (Exception e)
		{
			Log.v(TAG, "server cannot update email");
		}
	}

	public void changeImage(byte[] image) {
		vCard.setAvatar(image);
		try {
			vCard.save(NetworkService.getInstance().getConnection());
		} catch (XMPPException e) {
			Log.v(TAG, "error in updating image");
		}
	}

	public void changePassword(String password) {
		/* try {
			accountManager.changePassword(password);
		} catch (XMPPException e) {
			Log.v(TAG, "error changing password");
		} */
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=update&password=" + password;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		} catch (Exception e) {
			Log.v(TAG, "server cannot update password");
		}
	}
	
	public static void deleteUser(String username)	{
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=delete&username=" + username;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		} catch (Exception e) {
			Log.v(TAG, "server cannot delete user");
		}
	}
	
	public static void disableUser(String username) {
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=disable&username=" + username;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		} catch (Exception e) {
			Log.v(TAG, "server cannot disable user");
		}
	}
	
	public static void enableUser(String username) {
		try {
			String requestURL = USERSERVICE_URL;
			requestURL += "type=enable&username=" + username;
			URL url = new URL(requestURL);
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
		} catch (Exception e) {
			Log.v(TAG, "server cannot enable user");
		}
	}
}
