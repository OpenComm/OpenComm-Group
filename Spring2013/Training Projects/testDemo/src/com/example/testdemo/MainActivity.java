package com.example.testdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static EditText editText;
	public EditText sendText;
	private final String SERVER_NAME = "cuopencomm.no-ip.org";
	private final int PORT = 5222;
	private final String USER_NAME = "oc3testorg";
	private final String PASSWORD = "opencomm2012";
	private ChatHandler chat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) this.findViewById(R.id.mainText);
		editText.setFocusable(false);
		chat = new ChatHandler();
		sendText = (EditText) this.findViewById(R.id.editText1);
		testChat();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void testChat() {
		chat.connect(SERVER_NAME, PORT);
		//editText.setText(editText.getText().toString() + "Connected:"
		//		+ connected);
		chat.login(USER_NAME, PASSWORD);
		//editText.setText(editText.getText().toString() + "\n" + "Logged In:"
		//		+ loggedIn);
		chat.createRoom();
	}

	public void invite(View view) {
		chat.invite("oc2testorg@cuopencomm");
	}

	public void send(View view) {
		chat.send(sendText.getText().toString());
		//editText.setText(editText.getText().toString() + "\n" + "sendFunction called");
	}
	
	public static void handleMessage(String from, String messageBody){
		editText.setText(editText.getText().toString() + from + ":  " + messageBody);
	}

}
