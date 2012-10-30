package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.NotificationsController;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class NotificationsView extends Activity{

	private NotificationsController controller;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications_layout);
		controller = new NotificationsController(this);
	}
	public void backButtonClicked(View v){
		controller.handleBackButtonClicked();
	}
	public void overflowButtonClicked(View v){
		controller.handleOverflowButtonClicked();
	}
}
