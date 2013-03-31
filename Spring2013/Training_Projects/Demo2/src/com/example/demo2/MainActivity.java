package com.example.demo2;
import java.io.IOException;
import java.io.Serializable;

import network.NetConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	NetConnection nc;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() 
		{			
					@Override
					public void onClick(View v) {
						try
						{
							nc = new NetConnection();
							nc.connect();
							nc.login();
							Intent intent = new Intent(MainActivity.this, ChatRoom.class);
						    startActivity(intent);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							System.out.println("sth is wrong");
						}
					}
				});
	}	
}
