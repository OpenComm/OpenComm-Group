package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartAppActivity extends Activity {
	
	/* Input splash page here
	   After splash page shows, direct page to LoginView activity
	   To start this activity before the LoginView, change the launcher settings
	   in AndroidManifest.xml */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(StartAppActivity.this, LoginView.class);
        startActivity(intent);
    }

}
