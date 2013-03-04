package com.example.testdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {


	private EditText maintext;
	private Tester tester;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maintext = (EditText) this.findViewById(R.id.mainText);
        maintext.setFocusable(false);
        ChatHandler chat = new ChatHandler(getBaseContext());
        EditText sendText = (EditText) this.findViewById(R.id.sendText);
        Button inviteButton = (Button) this.findViewById(R.id.inviteButton);
      	tester = new Tester(chat, this.maintext, sendText);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tester.invite();
            }
        });
        Button sendButton = (Button) this.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tester.sendMessage();
            }
        });

        tester.testChat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
