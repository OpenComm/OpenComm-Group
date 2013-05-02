package edu.cornell.opencomm.controller;

import android.content.Intent;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.MyProfileView;

public class MyProfileController {

	private MyProfileView profileView;
	
	public MyProfileController(MyProfileView view) {
		this.profileView = view;
	}
	
	public void handleBackClicked() {
		Intent i = new Intent(this.profileView, DashboardView.class); 
		this.profileView.startActivity(i); 
	}
	
}
