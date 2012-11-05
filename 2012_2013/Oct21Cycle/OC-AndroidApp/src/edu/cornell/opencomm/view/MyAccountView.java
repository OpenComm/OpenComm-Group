package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.controller.MyAccountController;

public class MyAccountView extends Activity{

	private static TextView name;
	private static ImageView icon;
	private static TextView email;
	private static TextView password;
	private static TextView title;
	
	private MyAccountController controller;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_layout);
		name = (TextView) findViewById(R.id.my_account_name);
		icon = (ImageView) findViewById(R.id.my_account_picture);
		email = (TextView) findViewById(R.id.my_account_email);
		password = (TextView) findViewById(R.id.my_account_password);
		title = (TextView) findViewById(R.id.my_account_title);
		controller = new MyAccountController(this);
		name.setText(UserManager.PRIMARY_USER.getNickname());
		email.setText(UserManager.PRIMARY_USER.getUsername());
	}
	
	public void backButtonClicked(View v){
		//TODO: call super.OnBackPressed()
		controller.handleBackButtonClicked();
	}
	public void editButtonClicked(View v){
		controller.handleEditButtonClicked();
	}
	public void overflowButtonClicked(View v){
		controller.handleOverflowButtonClicked();
	}

}
