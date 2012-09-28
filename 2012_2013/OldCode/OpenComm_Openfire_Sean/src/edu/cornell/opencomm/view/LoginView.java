package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.LoginController;


public class LoginView extends Activity {
	private static String LOG_TAG = "OC_LoginView"; // for error checking
	private Context context;
	private LayoutInflater inflater = null;
	// private PopupWindow window = null;
	private LoginController loginController = null;

	/* Debugging
	private static final String TAG = "Controller.Login";
	private static final boolean D = true;*/

	//font
	private Typeface font;
	
	// Layout Views
	private static EditText usernameEdit;
	private static EditText passwordEdit;
	private static Button loginText;
	private static ImageButton loginButton;
	private static ImageView loginOverlay;


	 /* 
	 * /** Called when an activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		this.inflater = this.getLayoutInflater();
		TextView textLabelUsername = (TextView) findViewById(R.id.textLabelUsername);
		TextView textLabelPassword = (TextView) findViewById(R.id.textLabelPassword);
	    font = Typeface.createFromAsset(getAssets(), Values.font);
		
		usernameEdit = (EditText) findViewById(R.id.editTextUsername);
		passwordEdit = (EditText) findViewById(R.id.editTextPassword);
		loginText = (Button) findViewById(R.id.textLogin);
		loginButton = (ImageButton) findViewById(R.id.buttonLogin);
		loginOverlay = (ImageView) findViewById(R.id.loginOverlay);
		
		textLabelUsername.setTypeface(font);
	    textLabelPassword.setTypeface(font);
	    usernameEdit.setTypeface(font);
	    passwordEdit.setTypeface(font);
	    loginText.setTypeface(font);
	    
		initializeLoginButtonClickedEvent();
		loginController = new LoginController(this);
		Log.v(LOG_TAG, "LoginView generated");
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

	public ImageView getLoginOverlay() {
		return loginOverlay;
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
