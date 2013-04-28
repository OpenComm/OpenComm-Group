package edu.cornell.opencomm.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.MyProfileController;

/**
 * View for my profile screen. 
 * @author Lu Yang[frontend]
 */

public class MyProfileView extends Activity{

	private MyProfileController myProfileController;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_layout);
		myProfileController=new MyProfileController(this);
		//set font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
	}
	
}
