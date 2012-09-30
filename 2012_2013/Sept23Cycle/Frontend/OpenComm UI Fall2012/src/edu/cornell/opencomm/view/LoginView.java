package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


public class LoginView extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
	}
	
  /*  private static String LOG_TAG = "OC_LoginView"; // for error checking
    private Context context;
    private LayoutInflater inflater = null;
    private LoginController loginController = null;
    private static Context loginContext = null;

    // Layout Views
    private static EditText usernameEdit;
    private static EditText passwordEdit;
    private static Button loginText;
    private static ImageButton loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginContext=(Context)this;
        //ContextTracker.setContext(this);
        setContentView(R.layout.login_layout);

        this.inflater = this.getLayoutInflater();
        TextView textLabelUsername = (TextView) findViewById(R.id.textLabelEmail);
        TextView textLabelPassword = (TextView) findViewById(R.id.textLabelPassword);

        usernameEdit = (EditText) findViewById(R.id.editTextEmail);
        passwordEdit = (EditText) findViewById(R.id.editTextPassword);
        loginText = (Button) findViewById(R.id.loginText);
        loginButton = (ImageButton) findViewById(R.id.loginButton);

        initializeLoginButtonClickedEvent();
        loginController = new LoginController(this);
        Log.v(LOG_TAG, "LoginView generated");
    }

    private void initializeLoginButtonClickedEvent() {
        ImageButton loginButton = getLoginButton();
        if (loginButton != null) {
            loginButton.setOnClickListener(onLoginButtonClickedListener);
        }
        if (loginText != null) {
            loginText.setOnClickListener(onLoginButtonClickedListener);
        }
    }
    /**Jump to the account creation page when sign-up button is clicked*/
  /*  public void createAccount(View v){
    	Log.v("Crystal", "create Account");
    	SignupView account=new SignupView(this);
    	account.launch();
    }
    /**Jump to the Reset Password page when forgot-password is clicked*/
 /*   public void retrievePassword(View v){
    	Log.v("Crystal", "retrievePassword");
    	ResetPasswordView account=new ResetPasswordView(inflater, this);
    	account.launch();

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
    }; */
}

