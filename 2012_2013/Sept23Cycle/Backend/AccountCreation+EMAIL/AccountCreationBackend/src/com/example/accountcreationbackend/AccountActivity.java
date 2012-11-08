package com.example.accountcreationbackend;

import android.os.Bundle;
import android.app.Activity;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AccountActivity extends Activity {

	public static final String TAG = "AccountActivity";

	private AccountCreation accountCreation;
	private Button create;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		create = (Button) findViewById(R.id.button1);

		accountCreation = new AccountCreation();

		create.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//accountCreation.creation();
				accountCreation.creation("kevin", "lei", "mr.lei", 
						"kevinleiopencomm", "kevinlei" , "cuair.business@gmail.com");
				
				
				
				
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_account, menu);
		return true;
	}
}
