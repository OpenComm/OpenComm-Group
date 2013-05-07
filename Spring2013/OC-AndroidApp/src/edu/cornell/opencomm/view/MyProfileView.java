package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.MyProfileController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;

/**
 * View for my profile screen. 
 * @author Lu Yang[frontend]
 */

public class MyProfileView extends Activity{

	private MyProfileController myProfileController;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_layout);
		setUserInfo();
		myProfileController = new MyProfileController(this);
		//set font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
	}

	public void setUserInfo(){
		User user = UserManager.PRIMARY_USER;
		String name = user.getUsername();
		System.out.println(name);
		//still need title and cell phone
		((TextView)findViewById(R.id.my_profile_name_label)).setText(name);
	}
	
	public void edit(View v){

	}
	
	public void back(View v) {
		myProfileController.handleBackClicked();
	}
}
