package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.XMPPConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.network.UserAccountManager;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for new user account creation.
 * @author jonathan, flav
 */
public class SignupController {

    private static String TAG = "SignupController";
    private static boolean D = Values.D;
    private Context context;
    private SignupView view;

	private NetworkService xmppService;
	private XMPPConnection xmppConn;
    private UserAccountManager userAccountManager;
    
    final public String INVALID_FIRST_NAME = "Invalid First Name";
    final public String INVALID_LAST_NAME = "Invalid Last Name";
    final public String INVALID_EMAIL = "Invalid Email";
    final public String PASSWORDS_DONT_MATCH = "Passwords don't match";


    public SignupController(SignupView view, Context context) {
        if (D) Log.d(TAG, "SignupController constructor called");
        this.view = view;
        this.context = context;
		this.xmppService = new NetworkService(Network.DEFAULT_HOST,
				Network.DEFAULT_PORT);
		this.xmppConn = xmppService.getXMPPConnection();
        this.userAccountManager = new UserAccountManager(xmppConn);
    }

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");
    public final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z-]+");

    public void handleSaveButtonClick() {
    	view.setCreateOverlay(true);
        if (D) Log.d(TAG, "handleSaveButtonClick called");
        String firstName = view.getFirstNameBox().getText().toString();
        String lastName = view.getLastNameBox().getText().toString();
        String email = view.getEmailBox().getText().toString();
        String password = view.getPasswordBox().getText().toString();
        String confirmPassword = view.getConfirmPasswordBox().getText().toString();
        if(!password.equals(confirmPassword)) {
            notify(PASSWORDS_DONT_MATCH);
            return;
        }
        if(!validateName(firstName)) {
            notify(INVALID_FIRST_NAME);
            return;
        }
        if(!validateName(lastName)) {
            notify(INVALID_LAST_NAME);
            return;
        }
        if(!validateEmail(email)) {
            notify(INVALID_EMAIL);
            return;
        }
        String title = view.getTitleBox().getText().toString();

        //Log into the server as admin to create a new account
        NetworkService xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
        xmppService.login(Network.DEBUG_USERNAME, Network.DEBUG_PASSWORD);
        UserAccountManager manager = new UserAccountManager(xmppService.getXMPPConnection());
        if (D) Log.d(TAG, "Email:"+email+"Password:"+password+"firstName"+firstName+"lastName"+lastName+"title"+title);
        manager.createUser(email, password, firstName, lastName, title);
        xmppService.disconnect();
        view.dismiss();
    }

    private void notify(String message) {
        view.setCreateOverlay(false);
        NotificationView notify = new NotificationView(context);
        notify.launch(message,"RED","WHITE", true);
    }

    public void handleFirstNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleFirstNameFocusChange called");
        if(!hasFocus) {
            String nameText = view.getFirstNameBox().getText().toString();
            if(nameText != null && !nameText.equals("")) {
                boolean valid = validateName(nameText);
                if(!valid) {
                    notify(INVALID_FIRST_NAME);
                }
            }
        }
    }

    public void handleLastNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleLastNameFocusChange called");
        if(!hasFocus) {
            String nameText = view.getLastNameBox().getText().toString();
            if(nameText != null && !nameText.equals("")) {
                boolean valid = validateName(nameText);
                if(!valid) {
                    notify(INVALID_LAST_NAME);
                }
            }
        }
    }

    public void handleEmailFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleEmailFocusChange called");
        if(!hasFocus) {
            String emailText = view.getEmailBox().getText().toString();
            if(emailText != null && !emailText.equals("")) {
                boolean valid = validateEmail(emailText);
                if(!valid) {
                    notify(INVALID_EMAIL);
                }
            }
        }

    }

    public void handleCancelButtonClick() {
        if (D) Log.d(TAG, "handleCancelButtonClick called");
        view.setCancelOverlay(true);
        view.dismiss();
    }

    private boolean validateEmail(String email) {
        if (D) Log.d(TAG, "validateEmail called");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean validateName(String name) {
        if (D) Log.d(TAG, "validateName called");
        return NAME_PATTERN.matcher(name).matches();
    }

    //main function to add a user; returns true if the operation has been successful
	@SuppressWarnings("unchecked")
	private boolean createUser(String userEmail, String password, String firstname, String lastname, 
			String title) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				4);
		nameValuePairs.add(new BasicNameValuePair("userEmail", userEmail));
		nameValuePairs.add(new BasicNameValuePair("id",title+"+"+firstname+"+"+lastname));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("action", "add"));
		
		try {
			AsyncTask<ArrayList<NameValuePair>, Void, Boolean> sent = new LongOperation();
			return sent.execute(nameValuePairs).get();
			
		} catch (InterruptedException e) {
			Log.e(TAG, e.toString());
		} catch (ExecutionException e) {
			Log.e(TAG, e.toString());
		}
		return false;

	}
	
  //As per tutorial on http://sankarganesh-info-exchange.blogspot.com/p/need-and-vital-role-of-asynctas-in.html
    private class LongOperation extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

    @Override
	    protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
	    	
	    	return userAccountManager.userChange(params[0]);
	    }
    }	
}
