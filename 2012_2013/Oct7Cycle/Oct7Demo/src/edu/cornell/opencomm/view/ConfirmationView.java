package edu.cornell.opencomm.view;
import edu.cornell.opencomm.R;
import android.os.Bundle;
import android.app.Activity;
//Author:Spandana Govindgari sg754

public class ConfirmationView extends Activity{

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.confirm_layout);
	    }
	    //TODO
	    //1.Handle accept - add to the chat room
	    //2. Handle cancel button - ignore this person (do not add to the chat room)
	    //3. Handle confirmation text 
}
