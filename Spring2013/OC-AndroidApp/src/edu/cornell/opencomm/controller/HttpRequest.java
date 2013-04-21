package edu.cornell.opencomm.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

import android.util.Log;
 
public abstract class HttpRequest {
	
	private static final String TAG = "HttpRequest";
	private static VCard vCard;
	private static HttpClient httpClient;
	
	public static String forgotPassword(String jid, String email){
		String reponse = null;
		try {
			HttpGet httpget = new HttpGet("http://cuopencomm.no-ip.org/forgotPassword.php?jid="+jid+"&email="+email);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			try {
				httpClient = new DefaultHttpClient();
				reponse = httpClient.execute(httpget, gestionnaire_reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
		return reponse;
	}
	
	public static boolean createAccount(String username, String nickname,
			String email, String firstName, String lastName,
			String phoneNumber, InputStream photo, String title, String password) {
		/*
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
		System.out.println(reponse);
		if (reponse.equals("1")) {
			UserManager.PRIMARY_USER = new User(username, firstName + " " + lastName, 0);
			vCard = new VCard();
			vCard.setFirstName(firstName);
			vCard.setLastName(lastName);
			vCard.setEmailHome(email);
			vCard.setJabberId(username);
			vCard.setNickName(firstName + " " + lastName);
			try {
				NetworkService.getInstance().getConnection().login(username, password);
				vCard.save(NetworkService.getInstance().getConnection());
				System.out.println("user logged in, and vCard created and saved successfully");
				return true;
			} catch (XMPPException e) {
				Log.v(TAG, "error in saving VCard");
			}
		}
		return false;
		
	}
	
	public static void changeNickname(String nickname) {
		//TO DO: update user on server first
		if (UserManager.PRIMARY_USER != null) {
			try {
				vCard.load(NetworkService.getInstance().getConnection(), UserManager.PRIMARY_USER.getUsername());
			} catch (XMPPException e) {
				Log.v(TAG, "error getting primary user's VCard");
			}
		}
		 vCard.setNickName(nickname);
		 try {
			 vCard.save(NetworkService.getInstance().getConnection());
		 } catch (XMPPException e) {
			 Log.v(TAG, "error in updating nickname");
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
		/*
		 * vCard.setEmailHome(email); try {
		 * vCard.save(NetworkService.getInstance().getConnection()); } catch
		 * (XMPPException e) { Log.v(TAG, "error in updating email"); }
		 */
		try {

		} catch (Exception e) {
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
		/*
		 * try { accountManager.changePassword(password); } catch (XMPPException
		 * e) { Log.v(TAG, "error changing password"); }
		 */
		try {

		} catch (Exception e) {
			Log.v(TAG, "server cannot update password");
		}
	}

	public static void deleteUser(String username) {
		try {

		} catch (Exception e) {
			Log.v(TAG, "server cannot delete user");
		}
	}

	public static void disableUser(String username) {
		try {

		} catch (Exception e) {
			Log.v(TAG, "server cannot disable user");
		}
	}

	public static void enableUser(String username) {
		try {

		} catch (Exception e) {
			Log.v(TAG, "server cannot enable user");
		}
	}
	
}