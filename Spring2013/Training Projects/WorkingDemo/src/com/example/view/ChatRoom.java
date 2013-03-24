package com.example.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import com.example.guitest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChatRoom extends Activity {
	private EditText historyEdit;
	private EditText messageEdit;
	private EditText nameEdit;

	private Button sendButton;
	private Button leaveButton;
	private Button addButton;
	private Button delButton;
	private Button blButton;
	private Button chButton;
	// private ListView buddylist = (ListView) findViewById(R.id.BuddyListView);
	// private ListView chatlist = (ListView)
	// findViewById(R.id.ChattingListView);
	private boolean messagesAccessible = true;
	public static LinkedList<String> messages = new LinkedList<String>();
	private String tempUser;
	private String tempMess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		historyEdit = (EditText) findViewById(R.id.history);
		messageEdit = (EditText) findViewById(R.id.message);
		nameEdit = (EditText) findViewById(R.id.name);

		sendButton = (Button) findViewById(R.id.send);
		leaveButton = (Button) findViewById(R.id.leave);
		addButton = (Button) findViewById(R.id.add);
		delButton = (Button) findViewById(R.id.del);
		blButton = (Button) findViewById(R.id.buddyList);
		chButton = (Button) findViewById(R.id.chatList);

		sendButton.setOnClickListener(listener);
		leaveButton.setOnClickListener(listener);
		addButton.setOnClickListener(listener);
		delButton.setOnClickListener(listener);
		blButton.setOnClickListener(listener);
		chButton.setOnClickListener(listener);

		PollList pollList = new PollList();
		pollList.execute(new Object[3]);
		// buddylist.setAdapter((ListAdapter) new ArrayAdapter<String>(this,
		// android.R.layout.activity_chat_room,getData()));

	}

	public class PollList extends AsyncTask<Object, Object, Object> {
		@Override
		protected void onProgressUpdate(Object... values) {
			showMessage(tempUser, tempMess);
		}

		@Override
		protected Object doInBackground(Object... params) {
			while (messagesAccessible) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// thread interrupted
				}
				while (!messages.isEmpty()) {
						String message = messages.poll();
						int colon = message.indexOf(":");
						String user = message.substring(0, colon);
						String messageBody = message.substring(colon + 1);
						messageBody.trim();
						tempUser = user;
						tempMess = messageBody;
						this.publishProgress(new Object[3]);
				}
			}

			return null;
		}
	}

	private void logout() {
		showTost("Logging out");
		startActivity(new Intent(ChatRoom.this, Login.class));
	}

	private void showTost(String content) {
		Toast.makeText(ChatRoom.this, content, Toast.LENGTH_SHORT).show();
	}

	private void showList(ArrayList<String> list) {
		String Content = "";
		for (int n = 0; n < list.size(); n++) {
			Content += list.get(n) + "\n";
		}
		if (Content.equals("")) {
			showTost("There are no other participants in your chat room");
		} else {
			showTost(Content);
		}
	}

	View.OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.leave:
				Login.chat.logout();
				logout();
				break;

			case R.id.send:
				//
				String chat_txt;
				chat_txt = messageEdit.getText().toString().trim();
				if (chat_txt == null || chat_txt.equals("")) {
					showTost("No input text!");
					break;
				}
				Login.chat.send(chat_txt);
				messageEdit.setText("");
				break;

			case R.id.buddyList:
				showList(Login.chat.getRoster());
				break;

			case R.id.chatList:
				showList(Login.chat.getParticipants());
				break;

			case R.id.add:
				String name = nameEdit.getText().toString().trim();
				Login.chat.invite(name);
				showTost("done!");
				break;

			case R.id.del:
				String name2 = nameEdit.getText().toString().trim();
				Login.chat.kickout(name2);
				break;
			}
		}
	};

	private void showMessage(String name, String message) {
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		String nowStr = format.format(now);
		historyEdit.append(name + "(" + nowStr + "): " + message + "\n");
		messageEdit.setText("");
	}
}
