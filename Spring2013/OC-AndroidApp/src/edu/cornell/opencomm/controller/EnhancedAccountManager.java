package edu.cornell.opencomm.controller;

import java.io.IOException;

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
import edu.cornell.opencomm.model.CreateAccountReturnState;
import edu.cornell.opencomm.model.ResetPasswordReturnState;
import edu.cornell.opencomm.model.User;
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
		HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/createAccount.php?jid="+username+"&email="+email+"&password="+password);
		ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
		
		try {
			httpClient = new DefaultHttpClient();
			reponse = httpClient.execute(httpget, gestionnaire_reponse).toString();
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		Log.v(TAG, reponse);
		
		if (reponse.equals("1")) {
			UserManager.PRIMARY_USER = new User(username, firstName + " " + lastName, 0);
			VCard vCard = new VCard();
			vCard.setFirstName(firstName);
			vCard.setLastName(lastName);
			//vCard.setEmailHome(email);
			vCard.setJabberId(username);
			vCard.setNickName(firstName + " " + lastName);
			try {
				NetworkService.getInstance().getConnection().login(username, password);
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
		if (UserManager.PRIMARY_USER != null) {
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection(), UserManager.PRIMARY_USER.getUsername());
				vCard.setNickName(nickname);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating nickname");
			}
		}
	}

	/**
	 * @param String - the user's new phone number
	 * only call this method when authenticated
	 */
	public void changePhoneNumber(String number) {
		
		if (UserManager.PRIMARY_USER != null) {
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection(), UserManager.PRIMARY_USER.getUsername());
				vCard.setPhoneHome("VOICE", number);
				vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error in updating phone number");
			}
		}
	}

//	/**
//	 * @param String - the user's new nickname
//	 * only call this method when authenticated
//	 */
//	public static void changeEmail(String email) {
//		/*
//		 * vCard.setEmailHome(email); try {
//		 * vCard.save(NetworkService.getInstance().getConnection()); } catch
//		 * (XMPPException e) { Log.v(TAG, "error in updating email"); }
//		 */
//		try {
//
//		} catch (Exception e) {
//			Log.v(TAG, "server cannot update email");
//		}
//	}

	
	/**
	 * @param String - the user's new photo
	 * only call this method when authenticated
	 */
	public void changeImage(byte[] image) {
		
		if (UserManager.PRIMARY_USER != null) {
			VCard vCard = new VCard();
			try {
				vCard.load(NetworkService.getInstance().getConnection(), UserManager.PRIMARY_USER.getUsername());
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