package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

/**
 * 
 * Handles all operations associated with the dashboard view. NOTE: This list of
 * functions probably is not complete.
 * 
 */
public class DashboardController {

	private DashboardView dashboardView;

	/**
	 * TODO: DashboardController constructor. This should load the dashboard
	 * view.
	 */
	public DashboardController(DashboardView view, Context context) {
		this.dashboardView = view;
	}

	/**
	 * TODO: This should call a task when a user clicks the contact list button.
	 */
	public void handleContactListButtonClicked() {
		ContactListController.getInstance().updateContacts();
		this.dashboardView.findViewById(R.id.dashboardContactOverlay).setVisibility(View.VISIBLE);
    	// start contact view
    	Intent i = new Intent(this.dashboardView,ContactsView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * TODO: This should call a task when a user clicks the conferences button.
	 */
	public void handleConferenceButtonClicked() {
		this.dashboardView.findViewById(R.id.dashboardConfOverlay).setVisibility(View.VISIBLE);
    	// start conference view
    	Intent i = new Intent(this.dashboardView,ConferenceSchedulerView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * TODO: This should call a task when a user clicks the Account button.
	 */
	public void handleAccountButtonClicked() {
		this.dashboardView.findViewById(R.id.dashboardAccountOverlay).setVisibility(View.VISIBLE);
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Account clicked",duration);
    	send.show();
	}

	/**
	 * TODO: This should call a task when a user clicks the Logout button.
	 */
	public void handleLogoutButtonClicked() {
		if(NetworkService.getInstance().logout()){
		this.dashboardView.findViewById(R.id.dashboardLogoutOverlay).setVisibility(View.VISIBLE);
    	Intent i = new Intent(this.dashboardView,LoginView.class);
    	this.dashboardView.startActivity(i);
		}
		else{
			//TODO: Stop user from log out, ask Design.. Mention the scenarios
		}
	}
}
