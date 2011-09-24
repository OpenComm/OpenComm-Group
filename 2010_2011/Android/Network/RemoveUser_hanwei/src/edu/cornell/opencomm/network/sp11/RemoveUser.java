package edu.cornell.opencomm.network.sp11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

public class RemoveUser extends Activity {
	private EditText removeUsernameEdit;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        removeUsernameEdit = (EditText) findViewById(R.id.entry);
    }
    
    /** Attempts to remove users when OK button is clicked */
    public void handleRemoveUserToPS(View view) {
    	Intent i = new Intent(this, XMPPService.class);
    	i.putExtra(NetworkServices.KEY_REMOVEUSERNAME, removeUsernameEdit.getText().toString());
    	i.setAction(NetworkServices.ACTION_REMOVEUSER);
    	startService(i);
    }     
}