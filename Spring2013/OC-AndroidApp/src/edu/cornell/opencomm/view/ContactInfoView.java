package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ContactInfoController;

public class ContactInfoView extends Activity {

	private ContactInfoController contactInfoController;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info_layout);
		contactInfoController =new ContactInfoController(this);
		//set font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
	}
	
}
