package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.LoginController;


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
	private static TextView loginText;
	private static ImageButton loginButton;


	 /* 
	 * /** Called when an activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		this.inflater = this.getLayoutInflater();
		usernameEdit = (EditText) findViewById(R.id.editTextUsername);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (TextView) findViewById(R.id.textLogin);
		loginButton = (ImageButton) findViewById(R.id.buttonLogin);
		initializeLoginButtonClickedEvent();
		loginController = new LoginController(this);
	} // end onCreate method

	private void initializeLoginButtonClickedEvent() {
		ImageButton loginButton = getLoginButton();
		if (loginButton != null) {
			loginButton.setOnClickListener(onLoginButtonClickedListener);
		}
		if (loginText != null) {
			loginText.setOnClickListener(onLoginButtonClickedListener);
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
