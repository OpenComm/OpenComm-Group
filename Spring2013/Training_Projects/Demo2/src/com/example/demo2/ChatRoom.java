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
	ModelChatRoom cr;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatroom_layout);
		cr = new ModelChatRoom();
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
		System.out.println("AAAAAAAAAA");
		button.setOnClickListener(new View.OnClickListener() 
		{	
			public void onClick(View v) {
			System.out.println("dd");
			cr.inviteUser("oc2testorg", "reason");
			}
		});
	}
	
	public void onKickoutClick(){
		System.out.println("onKickout Clicked");
		final Button button = (Button) findViewById(R.id.button_kickout);
		cr.banUser("oc2testorg", "reason");
	}
	
	public void printOutParticipantsClick(){
		System.out.println("onParticipate Clicked");
		final Button button = (Button) findViewById(R.id.button_show_lists);
		cr.printUserList();
	}

	public void sendMessagesClict(){
		System.out.println("send message Clicked");
		final Button button = (Button) findViewById(R.id.send_messages);
		cr.sendMessage("hardcoded message");

	}
	
	public void printOutBuddyListClick(){
		final Button button = (Button) findViewById(R.id.button_buddylist);
		
	}
	
	public void onLogoutClict(){
		final Button button = (Button) findViewById(R.id.button_logout);
		NetConnection.logout();
	}
	
}
