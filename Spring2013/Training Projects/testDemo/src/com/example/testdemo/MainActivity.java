package com.example.testdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText editText;
	private EditText sendText;
	private final String SERVER_NAME = "cuopencomm.no-ip.org";
	private final int PORT = 5222;
	private final String USER_NAME = "oc3testorg";
	private final String PASSWORD = "opencomm2012";
	private ChatHandler chat;
	private static boolean chatStarted = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) this.findViewById(R.id.mainText);
		editText.setFocusable(false);
		chat = new ChatHandler(editText);
		sendText = (EditText) this.findViewById(R.id.editText1);
		final Button inviteButton = (Button) this.findViewById(R.id.inviteButton);
		inviteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				invite();
			}
		});
		final Button sendButton = (Button) this.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendMessage();
			}
		});
		testChat();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void testChat() {
		boolean connected = chat.connect(SERVER_NAME, PORT);
		boolean loggedIn = chat.login(USER_NAME, PASSWORD);
		boolean createdRoom = chat.createRoom();
		editText.setText(editText.getText().toString() + "Connected:"
				+ connected);
		editText.setText(editText.getText().toString() + "\n" + "Logged In:"
				+ loggedIn);
		editText.setText(editText.getText().toString() + "\n" + "Created Room:"
				+ createdRoom);

	}

	public void invite() {
		chat.invite("oc2testorg");
		editText.setText(editText.getText().toString() + "\n" + "inviteFunction called");
	}

	public void sendMessage() {
		chat.send(sendText.getText().toString());
		editText.setText(editText.getText().toString() + "\n" + "sendFunction called");
	}

	public void handleMessage(String from, String message) {
		from = from.replace("octestroom@conference.cuopencomm/", "");
		if (!chatStarted) {
			editText.setText(from + ":  " + message);
			chatStarted = true;
		} else {
			editText.setText(editText.getText().toString() + "\n" + from
					+ ":  " + message);
		}
	}
}
