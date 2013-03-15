package com.example.demo2;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import model.ModelChatRoom;
import network.NetConnection;

public class ChatRoom  extends Activity  {
	MultiUserChat muc;
	NetConnection conn;
	ModelChatRoom cr;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatroom_layout);
//		Bundle extras = getIntent().getExtras();
//		conn = (NetConnection) extras.getSerializable("network.NetConnection");
//		System.out.println(conn.isConnected()+"////");
//		cr = new ModelChatRoom(conn.conn);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onSignInClick(View v)
	{
		System.out.println("aaaaaaaaaaaaaaaa");		
	}
	
	
	public void onInviteClick(){
		final Button button = (Button) findViewById(R.id.button_invite);
		button.setOnClickListener(new View.OnClickListener() 
		{	
			public void onClick(View v) {
			System.out.println("dd");
			cr.inviteUser("oc2testorg", "reason");
			}
		});
	}
	
	public void onKickoutClick(){
		final Button button = (Button) findViewById(R.id.button_kickout);
//		cr.banUser(jid, reason);
	}
	
	public void printOutParticipantsClick(){
		final Button button = (Button) findViewById(R.id.button_show_lists);
//		cr.printUserList();
	}

	public void sendMessagesClict(){
		final Button button = (Button) findViewById(R.id.send_messages);
//		cr.sendMessage(String mes);

	}
	
	public void printOutBuddyListClick(){
		final Button button = (Button) findViewById(R.id.button_buddylist);
		
	}
	
	public void onLogoutClict(){
		final Button button = (Button) findViewById(R.id.button_logout);
//		conn.logout();
	}
	
}
