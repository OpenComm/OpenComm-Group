package edu.cornell.opencomm.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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
		setUserInfo();
		setContentView(R.layout.my_profile_layout);
		myProfileController = new MyProfileController(this);
		//set font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
	}

	public void setUserInfo(){
		User user = UserManager.PRIMARY_USER;
		if (user == null) System.out.println("no user!");
		String name = user.getUsername();
		if (name == null) System.out.println("null!!!");
		//still need title and cell phone
		((TextView)findViewById(R.id.my_profile_name_label)).setText(name);
	}
	
	public void back() {
		myProfileController.handleBackClicked();
	}
}
