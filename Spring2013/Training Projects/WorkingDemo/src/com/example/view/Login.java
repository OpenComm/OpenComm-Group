package com.example.view;

import com.example.guitest.R;
import com.example.view.ChatHandler;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class Login extends Activity {
	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private final String SERVER_NAME = "cuopencomm.no-ip.org";
	private final int PORT = 5222;
	static ChatHandler chat = new com.example.view.ChatHandler();
	private boolean connected;
	private boolean loggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		usernameEdit = (EditText) findViewById(R.id.tbx_usrn);
		passwordEdit = (EditText) findViewById(R.id.tbx_pwd);
		loginButton = (Button) findViewById(R.id.btn_signin);
		loginButton.setOnClickListener(listener);
		connected = chat.connect(SERVER_NAME, PORT);
	}

	View.OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_signin:
				String username,
				password;
				username = usernameEdit.getText().toString().trim();
				password = passwordEdit.getText().toString().trim();

				if (username == null || username.equals("") || password == null
						|| password.equals("")) {
					Toast.makeText(Login.this, "No input text!",
							Toast.LENGTH_SHORT).show();
					break;
				}
				// else:
				// get login time for future use
				/*
				 * Date now = new Date(System.currentTimeMillis());
				 * SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
				 * String nowStr = format.format(now);
				 */
				Toast.makeText(Login.this, "Logging in", Toast.LENGTH_SHORT)
						.show();
				System.out.println(connected);
				loggedIn = chat.login(username, password);
				if (loggedIn && connected) {
					Toast.makeText(Login.this, "Successful!",
							Toast.LENGTH_SHORT).show();
					chat.createRoom();
					startActivity(new Intent(Login.this, ChatRoom.class));
				}

				usernameEdit.setText("");
				passwordEdit.setText("");
				break;

			}
		}
	};
}
