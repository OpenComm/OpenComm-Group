package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class SendEmailView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendemail_layout);
    }

    public void cancel(View v){
    	CharSequence text = "The cancel button has been pressed";
    	int duration = Toast.LENGTH_SHORT;
    	Toast cancelled = Toast.makeText(getApplicationContext(),text,duration);
    	cancelled.show();
    }
    public void sendEmail(View v){
    	CharSequence text = "Send an email";
    	int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(getApplicationContext(),text,duration);
    	send.show();
    }
}
