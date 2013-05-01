package edu.cornell.opencomm.model;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

import android.util.Log;

/**
* @author Antoine Chkaiban
* Singleton class to implement Account Management:
* Reset Password and Create Account are done through http request with webservice
* Updating password, contact photo, telephone number, handled with AccountManager
*/
public class EnhancedAccountManager extends AccountManager {
	
	private static volatile EnhancedAccountManager instance = null;
	
	/**
	 * Object constructor
	 */
	private EnhancedAccountManager(Connection connection) {
		super(connection);
	}

	/**
	 * Method allowing to return an instance of the InvitationsList class
	 * @return Returns instance of InvitationsList
	 */
	public final static EnhancedAccountManager getInstance() {

		if (EnhancedAccountManager.instance == null) {

			synchronized (EnhancedAccountManager.class) {
				if (EnhancedAccountManager.instance == null) {
					EnhancedAccountManager.instance = new EnhancedAccountManager(NetworkService.getInstance().getConnection());
				}
			}
		}
		return EnhancedAccountManager.instance;
	}

	private static final String TAG = EnhancedAccountManager.class.getSimpleName();
	private static HttpClient httpClient;
	
	/**
	* @param String jid, String email - the email address and jid of the user whom password must be reset and sent to
	* @return ResetPasswordReturnState - look up Enum in model package
	**/
	public static ResetPasswordReturnState resetPassword(String jid, String email){
		
		String reponse = null;
		HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/forgotPassword.php?jid="+jid+"&email="+email);
		ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
		
		try {
			httpClient = new DefaultHttpClient();
			reponse = httpClient.execute(httpget, gestionnaire_reponse).toString();
		} catch (ClientProtocolException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			httpClient.getConnectionManager().shutdown();
        }
		if (reponse.equals("1")) {
			return ResetPasswordReturnState.SUCCEEDED;
		}
		return ResetPasswordReturnState.SERVER_ERROR;
	}
	
	/**
	* @param String username, String email, String firstName, string lastName, String password
	* of the user we wish to add to the server and create VCard
	* @return CreateAccountReturnState - look up Enum in model package
	**/
	public static CreateAccountReturnState createAccount(String username, String email, String firstName, String lastName, String password) {
		/*
		 * In Case more attributes need to be stored in the VCard:
		 * HashMap<String, String> attributes = new HashMap<String, String>(); }
		 */
		
		String reponse = null;
		HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/createAccount.php?jid="+username+"&email="+email+"&password="+password+"&name="+URLEncoder.encode(firstName+" "+lastName));
		ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
		
		try {
			httpClient = new DefaultHttpClient();
			reponse = httpClient.execute(httpget, gestionnaire_reponse).toString();
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		Log.v(TAG, reponse);
		
		if (reponse.equals("1")) {
			VCard vCard = new VCard();
			vCard.setFirstName(firstName);
			vCard.setLastName(lastName);
			vCard.setEmailHome(email);
			vCard.setJabberId(username);
			//Sets Nickname by default to firstName lastName
			vCard.setNickName(firstName + " " + lastName);
			try {
				NetworkService.getInstance().getConnection().login(username, password);
				UserManager.PRIMARY_USER = new User(vCard);
				vCard.save(NetworkService.getInstance().getConnection());
				Log.v(TAG, "user logged in, and vCard created and saved successfully");
				return CreateAccountReturnState.SUCCEEDED;
			} catch (XMPPException e) {
				Log.v(TAG, "error in saving VCard");
			}
		}
		return CreateAccountReturnState.SERVER_ERROR;
		
	}
	
	/**
	 * @param String - the user's new nickname
	 * only call this method when authenticated
	 */
	public static void changeNickname(String nickname) {
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//change Nickname on VCard
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection());
				vCard.setNickName(nickname);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating nickname");
			}
		}
	}
	
	/**
	 * @param String - the user's new nickname
	 * only call this method when authenticated
	 */
	public static void changeName(String name) {
		//split first and last names
		String[] firstLastName = name.split(" ");
		String lastName = "";
		for (int i = 1; i < firstLastName.length; i++) {
			lastName += firstLastName[i];
			if (i != firstLastName.length - 1) {
				lastName += " ";
			}
		}
		
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//get jid
			String jid = NetworkService.getInstance().getConnection().getUser();
			jid = jid.split("@")[0];
			//change Name on Server
			String reponse = null;
			HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/changeName.php?jid="+jid+"&name="+URLEncoder.encode(name));
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			try {
				httpClient = new DefaultHttpClient();
				reponse = httpClient.execute(httpget, gestionnaire_reponse).toString();
				httpClient.getConnectionManager().shutdown();
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
			if (reponse.equals("1")) {
				//change first and last names on VCard
				VCard vCard = new VCard();
				try {
					vCard.load(NetworkService.getInstance().getConnection());
					vCard.setFirstName(firstLastName[0]);
					vCard.setLastName(lastName);
					vCard.save(NetworkService.getInstance().getConnection());
				} catch (XMPPException e) {
					Log.v(TAG, "error in updating name on VCard");
				}
			}
		}	
	}
	
	/**
	 * @param String - the user's new Home Address
	 * only call this method when authenticated
	 */
	public static void changeHomeAddress(String homeAddress) {
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//change home address on VCard
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection());
				vCard.setAddressFieldHome("POSTAL", homeAddress);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating phone number");
			}
		}
	}

	/**
	 * @param String - the user's new phone number
	 * only call this method when authenticated
	 */
	public void changePhoneNumber(String number) {
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//Change Phone number on VCard
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection());
				vCard.setPhoneHome("VOICE", number);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating phone number");
			}
		}
	}

	/**
	 * @param String - the user's new email
	 * only call this method when authenticated
	 */
	public static void changeEmail(String email) {
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//get jid
			String jid = NetworkService.getInstance().getConnection().getUser();
			jid = jid.split("@")[0];
			//change email on server
			String reponse = null;
			HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/changeEmail.php?jid="+jid+"&email="+email);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			try {
				httpClient = new DefaultHttpClient();
				reponse = httpClient.execute(httpget, gestionnaire_reponse).toString();
				httpClient.getConnectionManager().shutdown();
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
			if (reponse.equals("1")) {
				//change email on VCard
				VCard vCard = new VCard();
				try {
					vCard.load(NetworkService.getInstance().getConnection());
					vCard.setEmailHome(email);
					vCard.save(NetworkService.getInstance().getConnection());
				} catch (XMPPException e) {
					Log.v(TAG, "error in updating name on VCard");
				}
			}
		}
	}

	
	/**
	 * @param String - the user's new photo
	 * only call this method when authenticated
	 */
	public void changeImage(byte[] image) {
		if (NetworkService.getInstance().getConnection().isAuthenticated()) {
			//Change image on VCard
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection());
				vCard.setAvatar(image);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating image");
			}
		}
	}

	
	/**
	 * @param String - the user's new password
	 * only call this method when authenticated
	 */
	public void changePassword(String password) {
		//change password on server
		try { 
			super.changePassword(password);
		} catch (XMPPException e) { 
			Log.v(TAG, "error changing password");
		}
	}

	/**
	 * @param String - the jid of the user we wish to delete
	 * only call this method when authenticated
	 */
	public void deleteAccount() {
		try {
			super.deleteAccount();
		} catch (Exception e) {
			Log.v(TAG, "server cannot delete user");
		}
	}
	
}