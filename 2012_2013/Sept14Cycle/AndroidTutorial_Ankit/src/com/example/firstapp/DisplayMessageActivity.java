package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
//
public class DisplayMessageActivity extends Activity {

	//All the subclasses of activity must implement the this method
	//the system calls this when creating a new instance of the activity 
	//It is where you must define the activity layout and where you should perform
	//the initial setup for the activity components
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the message from intent using the magicall getIntent()
        Intent intent = getIntent();
        String messageFromMain = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        
        //Creating a text view to display the message retrieved form the
        //intent
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(messageFromMain);
        
        //Now set text view as the activity layout 
        setContentView(textView);
        
    }


}
