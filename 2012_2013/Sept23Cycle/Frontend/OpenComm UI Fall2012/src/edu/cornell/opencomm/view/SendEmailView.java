package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SendEmailView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendemail_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login_view, menu);
        return true;
    }
}
