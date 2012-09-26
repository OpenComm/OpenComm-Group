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
	//private static EditText onlineText;
	//private static EditText offlineText;
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
      //  onlineText = (EditText) this.findViewById(R.id.online);
    	//offlineText = (EditText) this.findViewById(R.id.offline);
        context = getBaseContext();
        controller = new Controller();
        controller.setContext(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public static void writeBuddyList(String onlineInRoom, String online, String offline){
    	//onlineText.setText(onlineInRoom + "\n" + online);
    	//offlineText.setText(offline);
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
    
    public void inviteUser(View view){
    	String userToInvite = inviteUserText.getText().toString();
    	controller.invite(userToInvite);
    }

    /*
    public void sendMessage(View view){
    	if(chatRoomGenerated){
    		String message = sendMessageText.getText().toString();
    		controller.sendMessage(groupChat, message);
    	}
    	else{
    		String text = "Must generate a chat room before sending messages\nTry inviting another user!";
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			toast.show();
    	}
    	
    	
    }
    */
    
}
