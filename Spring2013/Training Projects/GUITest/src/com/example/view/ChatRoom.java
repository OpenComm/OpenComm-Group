package com.example.view;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.guitest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class ChatRoom extends Activity 
{
	private EditText historyEdit;
	private EditText messageEdit;
	private EditText nameEdit;
	
	private Button sendButton;
	private Button leaveButton;
	private Button addButton;
	private Button delButton;
	private Button blButton;
	private Button chButton;
	//private ListView buddylist = (ListView) findViewById(R.id.BuddyListView);
	//private ListView chatlist = (ListView) findViewById(R.id.ChattingListView);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
        historyEdit=(EditText)findViewById(R.id.history);
        messageEdit=(EditText)findViewById(R.id.message);
        nameEdit = (EditText)findViewById(R.id.name);
        
        sendButton=(Button)findViewById(R.id.send);
        leaveButton = (Button)findViewById(R.id.leave);
        addButton = (Button)findViewById(R.id.add);
        delButton = (Button)findViewById(R.id.del);
        blButton = (Button)findViewById(R.id.buddyList);
        chButton = (Button)findViewById(R.id.chatList);
        
        sendButton.setOnClickListener(listener);
        leaveButton.setOnClickListener(listener);
        addButton.setOnClickListener(listener);
        delButton.setOnClickListener(listener);
        blButton.setOnClickListener(listener);
        chButton.setOnClickListener(listener);

       // buddylist.setAdapter((ListAdapter) new ArrayAdapter<String>(this, android.R.layout.activity_chat_room,getData()));
    	
	}
	
	
	
	
	private void logout()
	{
		showTost("Loging off");
		startActivity(new Intent(ChatRoom.this, Login.class));
	}

	private void showTost(String content)
	{
		Toast.makeText(ChatRoom.this, content, Toast.LENGTH_SHORT).show();
	}
	
	private void showList(ArrayList<String> list)
	{
		String Content = "";
		for(int n = 0 ; n < list.size()-1; n++)
		{
			Content += list.get(n)+"\n";
		}
		Content += list.get(list.size()-1);
		showTost(Content);
	}
	
	View.OnClickListener listener=new OnClickListener() 
	{
		
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.leave:
				//TODO: add log-off function here
				logout();
				break;
				
			case R.id.send:
				//
				String chat_txt; 
				chat_txt=messageEdit.getText().toString().trim();
				if(chat_txt == null || chat_txt.equals(""))
				{
					showTost("No input text!");
					break;
				}
				//else:
				
				//TODO: add send message functions here
				showMessage("localhost",chat_txt);
			break;
		
			case R.id.buddyList:
				// TODO: get the buddylist(online list) in ArrayList<String>
				// and show them up with :  showList(ArrayList<String> list) here
				//showList(list);
			break;
			
			case R.id.chatList:
				// TODO: get the chatlist in ArrayList<String>
				// and show them up with : showList(ArrayList<String> list) here
				//showList(list);
			break;
			
			
			case R.id.add:
				// TODO: add some one to chat list (invite somebody to chat)
				//String name = nameEdit.getText().toString().trim();
				// add name
				//showTost("done!");
			break;
			
			case R.id.del:
				// TODO: delete some one to chat list (kick out somebody)
				//String name = nameEdit.getText().toString().trim();
				//delete name
				//showTost("done!");
			break;
			}
		}
	};
	
	
	//TODO: Show messages. When catching a coming message
	// you can use it to show in the GUI
	private void showMessage(String name, String message)
	{
		Date now=new Date(System.currentTimeMillis());
		SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
		String nowStr=format.format(now);
		historyEdit.append(name+"("+nowStr+"): "+ message +"\n");
		messageEdit.setText("");
	}
}
