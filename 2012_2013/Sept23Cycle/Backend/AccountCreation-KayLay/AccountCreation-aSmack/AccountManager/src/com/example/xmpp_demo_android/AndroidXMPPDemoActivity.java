package com.example.xmpp_demo_android;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AndroidXMPPDemoActivity extends Activity {
	
	public static final String TAG = "DemoActivity";

	Network network;
	private Button Create;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Create = (Button) findViewById(R.id.button1);

        
      //  network = new Network();
        
        Create.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			network= new Network();
				
			}
        	
        });
        
    }
}
