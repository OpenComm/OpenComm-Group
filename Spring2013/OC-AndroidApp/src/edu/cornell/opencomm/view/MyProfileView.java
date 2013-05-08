package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
	
	private static TextView name;
	private static ImageView icon;
	private static TextView email;
	private static TextView phoneNumber;
	private static TextView title; 
	
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
		
		((TextView)findViewById(R.id.my_profile_name_label)).setText(name);
		icon = (ImageView) findViewById(R.id.my_profile_photo); 
		icon.setBackgroundResource(user.getImage());
		title = (TextView) findViewById(R.id.my_profile_title_label); 
		title.setText(user.getTitle()); 
		phoneNumber = (TextView) findViewById(R.id.my_profile_phone_label); 
		String number = user.getVCard().getPhoneWork(user.getVCard().getJabberId()); 
		if (number == null){
			phoneNumber.setText("");
		} else phoneNumber.setText(number); 
		email = (TextView) findViewById(R.id.my_profile_email_label);
		String mail = user.getVCard().getEmailHome();  
		if (mail == null) {email.setText("");}
		else email.setText(mail); 
	}
	
	public void edit(View v){

	}
	
	public void back(View v) {
		myProfileController.handleBackClicked();
	}
}
