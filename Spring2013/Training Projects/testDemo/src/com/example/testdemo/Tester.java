package com.example.testdemo;

import android.widget.EditText;

public class Tester {

	private static EditText editText;
	private EditText sendText;
	private final String SERVER_NAME = "cuopencomm.no-ip.org";
	private final int PORT = 5222;
	private final String USER_NAME = "oc3testorg";
	private final String PASSWORD = "opencomm2012";
	private ChatHandler chat;
	private static boolean chatStarted = false;
	
	public Tester(ChatHandler chat, EditText editText, EditText sendText){
		Tester.editText = editText;
		this.chat = chat;
	}
	
	public void testChat(){
		boolean connected = chat.connect(SERVER_NAME,PORT);
		boolean loggedIn =  chat.login(USER_NAME, PASSWORD);
		boolean createdRoom = chat.createRoom();
		editText.setText(editText.getText().toString() + "Connected:"+ connected);
		editText.setText(editText.getText().toString() + "\n" + "Logged In:" + loggedIn);
		editText.setText(editText.getText().toString() + "\n" + "Created Room:" + createdRoom);
		if(!connected && loggedIn && createdRoom){
			editText.setText(editText.getText().toString() + "\n" + "Not Capable of Inviting, Kicking Out, or Sending Messages");
		}
		else{
			editText.setText(editText.getText().toString() + "\n");
		}
		invite();
		chat.send("Hi!");
	}

	public void invite(){
		chat.invite("oc2testorg");
		chat.invite("oc2testorg@cuopencomm.no-ip.org");
		chat.invite("oc2testorg@conference.cuopencomm);");
		chat.invite("OC Test2");
	}
	
	public void sendMessage(){
		chat.send(sendText.getText().toString());
	}
	public static void handleMessage(String from, String message){
		if(!chatStarted){
			editText.setText(from + ":  " + message);
		}
		else{
			editText.setText(editText.getText().toString() + "\n" + from + ":  " + message);
		}
	}
}

