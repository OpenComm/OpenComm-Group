package edu.cornell.opencomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

/* In the XML file, make sure the Login screen has:
 * 1) Have username slot to type in
 * 2) Have password slot to type in
 * 3) Have a clickable submit button
 */
public class Login extends Activity {
    private static String LOG_TAG = "OC_Login"; // for error checking
    private EditText usernameEdit;
    private EditText passwordEdit;
    /* TODO: Will not be using these in real application, but just for now
     * will use this "correct" username and password to just be consistent.
     * In Risa's code, these two are in the Network class -NORA
     */
    private String correct_username="opencommss"; 
    private String correct_password="ssopencomm";
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameEdit = (EditText)findViewById(R.id.input_username);
        passwordEdit = (EditText)findViewById(R.id.input_password);
        
        Button login_button = (Button)findViewById(R.id.login_button);
    }
    
    public void handleLogin(View view){
    	Intent i = new Intent(Login.this, MainApplication.class);

    	/* TODO: Will remove this for now so that I can concentrate on GUI -NORA
    	 * 
    	 * i.putExtra(Networks.KEY_USERNAME, usernameEdit.getText().toString());
    	 * i.putExtra(Networks.KEY_PASSWORD, passwordEdit.getText().toString());
    	 * i.setAction(MainApplication.ACTION_CREAT_MAIN);
    	 * 
    	 * Instead will use this:
    	 */
    	i.putExtra("username", correct_username);
    	i.putExtra("password", correct_password);
    	startActivity(i);
    }
    
    /* 
     * To Login (through connection):
     * 
     * 1) Need to connect to internet
     *  - Manually change AndroidManifest.xml file
     * 2) Connect to network
     *  - Start the NetworkGUI.java activity
     *  - This should start a chain of activating the Network,
     *  XMPP service, and starting up a MUCroom
     * 
     */
    
   
}