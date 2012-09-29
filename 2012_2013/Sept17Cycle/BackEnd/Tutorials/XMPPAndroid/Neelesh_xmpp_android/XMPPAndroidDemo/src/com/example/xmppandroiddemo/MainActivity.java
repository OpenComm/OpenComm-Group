package com.example.xmppandroiddemo;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

	public static final String TAG = "DemoActivity";
	XMPPConnect xmppconnect;
	// These should probably not be access by anyone, so we keep them private
		private Button Connect;
		private Button MultiUserChat;
		private Button SendMessage;
		
		private Button BuddyList;
		private Button logout;

		 @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_main);
		        Connect = (Button) findViewById(R.id.connectbutton);
		        MultiUserChat = (Button) findViewById(R.id.muc);
		        SendMessage = (Button) findViewById(R.id.sendmessagebutton);
		        
		        BuddyList = (Button) findViewById(R.id.buddylistbutton);
		        logout = (Button) findViewById(R.id.logout);
		        
       xmppconnect= new XMPPConnect();
       Connect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				xmppconnect.connect();
			}
       	
       });

       MultiUserChat.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				xmppconnect.multiuserchat();
			}
       	
       });
       
       SendMessage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				xmppconnect.message();
			}
       	
       });
       BuddyList.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				xmppconnect.buddylist();
			}
       	
       });

       logout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				xmppconnect.logout();
			}
       	
       });
    
		 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
