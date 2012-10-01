package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ReturnState;

public class StartAppActivity extends Activity {
	 private static final String TAG = StartAppActivity.class.getSimpleName();	
	/* Input splash page here
	   After splash page shows, direct page to LoginView activity
	   To start this activity before the LoginView, change the launcher settings
	   in AndroidManifest.xml */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_view);
		connect();
      
    }
    /**
     * TODO: Use Network Service to connect to the server 
     * Note: Netowrk service is a AsyncTask so there is no need to creat another. Just Saying :)
     * 
     */
    private void connect(){
    	//TODO : Fix it Conncetion code here
    	ReturnState returnState = ReturnState.SUCEEDED;
    	if(returnState == ReturnState.SUCEEDED){
    		launchLoginView();
    	}
    }
    private void launchLoginView(){
    	Intent intent = new Intent(StartAppActivity.this, LoginView.class);
    	startActivity(intent);
    }
    private void onConnectionError(){
    	
    }
}
