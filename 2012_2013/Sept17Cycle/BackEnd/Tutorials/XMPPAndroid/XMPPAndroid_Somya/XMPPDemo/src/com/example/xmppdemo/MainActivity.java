package com.example.xmppdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{
	
	public static final String TAG = "DemoActivity";

	
	
	// These should probably not be access by anyone, so we keep them private
	private Button login;
	private Button joinMUC;
	private Button inviteUser;
	private Button sendMessage;
	private Button printBuddyList;
	private Button logout;
	Network network;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login = (Button) findViewById(R.id.button1);
        joinMUC = (Button) findViewById(R.id.button2);
        inviteUser = (Button) findViewById(R.id.button3);
        sendMessage = (Button) findViewById(R.id.button4);
        printBuddyList = (Button) findViewById(R.id.button5);
        logout = (Button) findViewById(R.id.button6);
        
    network = new Network();
        
        login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				network.Login();
			}
        	
        });
        
        joinMUC.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				network.joinMUC();
			}
        	
        });
        
        inviteUser.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				 network.inviteUser();
			}
        	
        });
        
        sendMessage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				network.sendMessage();
			}
        	
        });
        
        printBuddyList.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				network.printBuddyList();
			}
        	
        });
        
        logout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				network.quit();
			}
        	
        });
    }
}
