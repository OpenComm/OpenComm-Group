package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.app.Activity;
import android.os.Bundle;

public class LoginView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
    }
    //TODO Implement the the following functionality
    //1. Handle Login button- Delegate the control to controller
    //2. Handle reset password- Launch reset password activity 
    //3. Handle signup - launch the signup activity
}

