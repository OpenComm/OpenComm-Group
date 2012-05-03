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
import android.view.View;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.network.UserAccountManager;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.SettingsView;
import edu.cornell.opencomm.view.SignupView;

/**
 * Controller for account settings.
 * @author joey
 */
public class SettingsController {

    private static String TAG = "SettingsController";
    private static boolean D = Values.D;
    private Context context;
    private SettingsView view;
    
	private NetworkService xmppService;
	private XMPPConnection xmppConn;
    private UserAccountManager userAccountManager;

    public SettingsController(SettingsView settingsView, Context context) {
        if (D) Log.d(TAG, "SettingsController constructor called");
        this.view = settingsView;
        this.context = context;
		this.xmppService = new NetworkService(Network.DEFAULT_HOST,
				Network.DEFAULT_PORT);
		this.xmppConn = xmppService.getXMPPConnection();
        this.userAccountManager = new UserAccountManager(xmppConn);
    }

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4}$");
    public final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z-]+");

    public void handleSaveButtonClick() {
        if (D) Log.d(TAG, "handleSaveButtonClick called");
        view.setCreateOverlay(true);
        String firstName = view.getFirstNameBox().getText().toString();
        String lastName = view.getLastNameBox().getText().toString();
        String email = view.getEmailBox().getText().toString();
        if(validateName(firstName) && validateName(lastName) && validateEmail(email)) {
            String title = view.getTitleBox().getText().toString();
            String password = view.getPasswordBox().getText().toString();
            editUser(email, password, firstName, lastName, title);
            //UPDATE TO EDIT USER FUNCTIONS
            /*NetworkService xmppService = new NetworkService(Network.DEFAULT_HOST, Network.DEFAULT_PORT);
            xmppService.login(Network.DEBUG_USERNAME, Network.DEBUG_PASSWORD);
            UserAccountManager manager = new UserAccountManager(xmppService.getXMPPConnection());
            manager.createUser(email, password, firstName, lastName, title);
            xmppService.disconnect();*/
            view.dismiss();
        } else {
        	view.setCreateOverlay(false);
            NotificationView notify = new NotificationView(context);
            notify.launch("Please fix the errors above","RED","WHITE", true);
        }
    }

    public void handleFirstNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleFirstNameFocusChange called");
        if(!hasFocus) {
            boolean valid = validateName(view.getFirstNameBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid First Name","RED","WHITE", true);
            }
        }
    }

    public void handleLastNameFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleLastNameFocusChange called");
        if(!hasFocus) {
            boolean valid = validateName(view.getLastNameBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid Last Name","RED","WHITE", true);
            }
        }
    }

    public void handleEmailFocusChange(boolean hasFocus) {
        if (D) Log.d(TAG, "handleEmailFocusChange called");
        if(!hasFocus) {
            boolean valid = validateEmail(view.getEmailBox().getText().toString());
            if(!valid) {
                NotificationView notify = new NotificationView(context);
                notify.launch("Invalid Email","RED","WHITE", true);
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
	private boolean editUser(String userEmail, String password, String firstname, String lastname,
			String title) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				4);
		nameValuePairs.add(new BasicNameValuePair("userEmail", userEmail));
		nameValuePairs.add(new BasicNameValuePair("id",title+"+"+firstname+"+"+lastname));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("action", "edit"));

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
