package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
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
		setContentView(R.layout.contact_card_layout);
		name = (TextView) findViewById(R.id.my_account_name);
		icon = (ImageView) findViewById(R.id.my_account_picture);
		email = (TextView) findViewById(R.id.my_account_email);
		password = (TextView) findViewById(R.id.my_account_password);
		title = (TextView) findViewById(R.id.my_account_title);
		controller = new MyAccountController(this);
	}
	
	public void backButtonClicked(View v){
		controller.handleBackButtonClicked();
	}
	public void editButtonClicked(View v){
		controller.handleEditButtonClicked();
	}
	public void overflowButtonClicked(View v){
		controller.handleOverflowButtonClicked();
	}
	@Override
    public void onBackPressed() {
    	// back button disabled
    }

}
