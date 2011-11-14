package edu.cornell.opencomm.view;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConfirmationController;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.network.NetworkService;

public class LoginView extends Activity {
	private static String LOG_TAG = "OC_LoginView"; // for error checking
	private Context context;
	private LayoutInflater inflater = null;
	// private PopupWindow window = null;
	private LoginController loginController = null;
	private View loginLayout = null;

	// Debugging
	// private static final String TAG = "Controller.Login";
	private static final boolean D = true;

	// Layout Views
	private static EditText usernameEdit;
	private static EditText passwordEdit;
	private static ImageButton loginButton;


	 /* 
	 * /** Called when an activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		usernameEdit = (EditText) findViewById(R.id.editTextUsername);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);

		loginButton = (ImageButton) findViewById(R.id.buttonLogin);
		initializeLoginButtonClickedEvent();
		loginController = new LoginController(this);
	} // end onCreate method

	private void initializeLoginButtonClickedEvent() {
		ImageButton loginButton = getLoginButton();
		if (loginButton != null) {
			loginButton.setOnClickListener(onLoginButtonClickedListener);
		}
	}

	public ImageButton getLoginButton() {
		return loginButton;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	private View.OnClickListener onLoginButtonClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			loginController.handleLoginButtonClick(usernameEdit, passwordEdit);
		}
	};
}
