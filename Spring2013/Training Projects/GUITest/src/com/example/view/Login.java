package com.example.view;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.guitest.R;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class Login extends Activity 
{
	
	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		usernameEdit = (EditText)findViewById(R.id.tbx_usrn);
		passwordEdit = (EditText)findViewById(R.id.tbx_pwd);
		loginButton = (Button)findViewById(R.id.btn_signin);
		
		loginButton.setOnClickListener(listener);
	}

	
	View.OnClickListener listener=new OnClickListener() 
	{
		
		public void onClick(View v) {
			switch(v.getId())
			{
			//case R.id.:
				//
				//break;
				
			case R.id.btn_signin:
				//
				String username,password; 
				username = usernameEdit.getText().toString().trim();
				password = passwordEdit.getText().toString().trim();
				
				if(username == null || username.equals("")||password == null || password.equals(""))
				{
					Toast.makeText(Login.this, "No input text!", Toast.LENGTH_SHORT).show();
					break;
				}
				//else:
				//get login time for future use
				Date now=new Date(System.currentTimeMillis());
				SimpleDateFormat format=new SimpleDateFormat("hh:mm:ss");
				String nowStr=format.format(now);
				
				Toast.makeText(Login.this, "Loging in", Toast.LENGTH_SHORT).show();
				//TODO: ADD the test of user name & Password here
				Toast.makeText(Login.this, "Successful!", Toast.LENGTH_SHORT).show();
				
				startActivity(new Intent(Login.this, ChatRoom.class));
				
				
				usernameEdit.setText("");
				passwordEdit.setText("");
				break;
				
			//case R.id.leave:
			//	break;
			}
		}
	};
}

