package com.androidxmpp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText connectionText;
	private EditText userNameText;
	private EditText passwordText;
	private EditText inviteUserText;
	private EditText sendMessageText;
	private Toast toast;
	private Context context;
	private Controller controller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionText = (EditText) this.findViewById(R.id.editText1);
        userNameText = (EditText) this.findViewById(R.id.username);
        passwordText = (EditText) this.findViewById(R.id.password);
        inviteUserText = (EditText) this.findViewById(R.id.inviteUser);
        sendMessageText = (EditText) this.findViewById(R.id.sendmessage);
        context = getBaseContext();
        controller = new Controller(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
        
    public void terminate(View view){
    	controller.disconnect();
    	this.finish();
    }

    public void connect(View view){
    	String server = connectionText.getText().toString();
    	controller.establishConnection(server);
    }
    
    public void login(View view){
    	String text1 = "Authenticating your information...\n\nPlease do not touch anything...";
		toast = Toast.makeText(context, text1, Toast.LENGTH_LONG);
		toast.show();
    	String userName = userNameText.getText().toString();
    	String password = passwordText.getText().toString();
    	controller.login(userName, password);
    }
    
    public void invite(View view){
    	String userToInvite = inviteUserText.getText().toString();
    	controller.invite(userToInvite);
    }


    public void sendMessage(View view){
    		String message = sendMessageText.getText().toString();
    		controller.sendMessage(message);
    	
    	
    	
    }
}
