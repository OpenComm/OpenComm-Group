package com.example.testdemo;

import android.widget.EditText;

public class Tester {

	private EditText editText;
	private final String SERVER_NAME = "cuopencomm.no-ip.org";
	private final int PORT = 5222;
	private final String USER_NAME = "oc3testorg";
	private final String PASSWORD = "opencomm2012";
	private ChatHandler chat;
	
	public Tester(ChatHandler chat, EditText editText){
		this.editText = editText;
		this.chat = chat;
	}
	
	public void run(){
		boolean connected = chat.connect(SERVER_NAME,PORT);
		boolean loggedIn =  chat.login(USER_NAME, PASSWORD);
		boolean createdRoom = chat.createRoom();
		editText.setText(editText.getText().toString() + "Connected:"+ connected);
		editText.setText(editText.getText().toString() + "\n" + "Logged In:" + loggedIn);
		editText.setText(editText.getText().toString() + "\n" + "Created Room:" + createdRoom);
		if(connected && loggedIn && createdRoom){
			editText.setText(editText.getText().toString() + "\n" + "Not Capable of Inviting, Kicking Out, or Sending Messages");
		}
		else{
			editText.setText(editText.getText().toString() + "\n");
		}
	}

}

