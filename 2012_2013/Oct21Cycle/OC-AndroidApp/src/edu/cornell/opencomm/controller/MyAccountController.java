package edu.cornell.opencomm.controller;

import android.widget.Toast;

import edu.cornell.opencomm.view.MyAccountView;


public class MyAccountController {

	private MyAccountView view;
	
	public MyAccountController (MyAccountView v) {
		this.view = v;
	}
	//TODO: Remove this method, let the activity/view handle this
	public void handleBackButtonClicked() {
		this.view.onBackPressed();	
	}
	
	public void handleEditButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(), "Edit Button Pressed", duration);
		toast.show();	
	}
	
	public void handleOverflowButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(), "Overflow Button Pressed", duration);
		toast.show();	
	}
}