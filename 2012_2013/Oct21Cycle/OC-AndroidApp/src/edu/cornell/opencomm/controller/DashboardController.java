package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceCardView;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.MyAccountView;

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
	public void handleConfInfoButtonClicked() {
		//ContactListController.getInstance().updateContacts();
		this.dashboardView.findViewById(R.id.dashboardConfInfoOverlay).setVisibility(View.VISIBLE);
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"ConfInfo clicked",duration);
    	send.show();
    	// start contact view
    	/**Intent i = new Intent(this.dashboardView,ConferenceCardView.class);
    	this.dashboardView.startActivity(i);*/
	}

	/**
	 * TODO: This should call a task when a user clicks the conferences button.
	 */
	public void handleConferencesButtonClicked() {
     	// start conference view
    	Intent i = new Intent(this.dashboardView,ConferenceSchedulerView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * TODO: This should call a task when a user clicks the Contacts button.
	 */
	public void handleContactsButtonClicked() {
		// start conference view
    	Intent i = new Intent(this.dashboardView,ContactsView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * TODO: This should call a task when a user clicks the Profile button.
	 */
	public void handleProfileButtonClicked() {
		this.dashboardView.findViewById(R.id.dashboardProfileOverlay).setVisibility(View.VISIBLE);
		/*int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Profile clicked",duration);
    	send.show();*/
		Intent i = new Intent(this.dashboardView,MyAccountView.class);
    	this.dashboardView.startActivity(i);
	}
}
