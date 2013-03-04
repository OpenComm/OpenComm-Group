package com.example.testdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {


	private EditText maintext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maintext = (EditText) this.findViewById(R.id.mainText);
        maintext.setFocusable(false);
        ChatHandler chat = new ChatHandler(getBaseContext());
        Tester tester = new Tester(chat, this.maintext);
        tester.run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
