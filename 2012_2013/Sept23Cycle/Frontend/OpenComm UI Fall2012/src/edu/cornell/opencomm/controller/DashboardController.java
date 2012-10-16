package edu.cornell.opencomm.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.DashboardView;

/**
 * 
 * Handles all operations associated with the dashboard view. NOTE: This list of
 * functions probably is not complete.
 * 
 */
public class DashboardController {

	private DashboardView dashboardView;
	private Context context;

	/**
	 * TODO: DashboardController constructor. This should load the dashboard
	 * view.
	 */
	public DashboardController(DashboardView view, Context context) {
		this.dashboardView = view;
		this.context = context;
	}

	/**
	 * TODO: This should call a task when a user clicks the contact list button.
	 */
	public void handleContactListButtonClicked() {
		this.dashboardView.findViewById(R.id.dashboardContactOverlay).setVisibility(View.VISIBLE);
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Contact clicked",duration);
    	send.show();
	}

	/**
	 * TODO: This should call a task when a user clicks the conferences button.
	 */
	public void handleConferenceButtonClicked() {
		this.dashboardView.findViewById(R.id.dashboardConfOverlay).setVisibility(View.VISIBLE);
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Conference clicked",duration);
    	send.show();
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
		this.dashboardView.findViewById(R.id.dashboardLogoutOverlay).setVisibility(View.VISIBLE);
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Logout clicked",duration);
    	send.show();
	}
}
