package edu.cornell.opencomm.controller;

import android.widget.Toast;

import edu.cornell.opencomm.view.MyAccountView;


public class MyAccountController {

	private MyAccountView view;
	
	public MyAccountController (MyAccountView v) {
		this.view = v;
	}
	
	public void handleBackButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(), "Back Button Pressed", duration);
		toast.show();	
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