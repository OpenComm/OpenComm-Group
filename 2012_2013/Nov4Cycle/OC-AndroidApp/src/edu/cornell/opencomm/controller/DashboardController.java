package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.MyProfileView;
import edu.cornell.opencomm.view.NotificationsView;

/**
 * Controller for Dashboard (DashboardView)
 * Functionality (handled by DashboardController).<br>
 * When corresponding buttons are clicked, different app features are launched:
 * <ul>
 * <li>Launches notification in action bar</li>
 * <li>Launches overflow in action bar</li>
 * <li>Launches conference info</li>
 * <li>Launches conference list</li>
 * <li>Launches contact list</li>
 * <li>Launches profile info</li>
 * </ul>
 *
 * Issues [TODO]
 * - [frontend] Implement notification functionality
 * - [backend] pull the most recent conference/current conf in session
 *
 * @author Risa Naka [frontend]
 * */
public class DashboardController {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = DashboardController.class.getSimpleName();

	private DashboardView dashboardView;

	/**
	 * DashboardController constructor. 
	 */
	public DashboardController(DashboardView view, Context context) {
		this.dashboardView = view;
	}

	/**
	 * Launches Conference Info
	 */
	public void handleConfInfoButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"ConfInfo clicked",duration);
    	send.show();
    	// TODO [backend] pull the most recent conference/current conf in session
    	// start conference view
    	/**Intent i = new Intent(this.dashboardView,ConferenceCardView.class);
    	this.dashboardView.startActivity(i);*/
	}

	/**
	 * Launches Conference List
	 */
	public void handleConferencesButtonClicked() {
     	// start conference view
    	Intent i = new Intent(this.dashboardView,ConferenceSchedulerView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * Launches Contact List
	 */
	public void handleContactsButtonClicked() {
    	Intent i = new Intent(this.dashboardView,ContactListView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * Launches Profile Button
	 */
	public void handleProfileButtonClicked() {
		Intent i = new Intent(this.dashboardView,MyProfileView.class);
    	this.dashboardView.startActivity(i);
	}

	public void handleNotificationClicked() {
		Intent i = new Intent(this.dashboardView, NotificationsView.class);
    	this.dashboardView.startActivity(i);	
	}

	/** 
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
    	if (this.dashboardView.getOverflowList().getVisibility() == View.INVISIBLE) {
    		this.dashboardView.getOverflowList().setVisibility(View.VISIBLE);
    	}
    	else {
    		this.dashboardView.getOverflowList().setVisibility(View.INVISIBLE);
    	}		
	}

	/** 
	 * Overflow option clicked:
	 * <ul>
	 * <li>log out: logs out, returns to the login page
	 * </ul>
	 * */
	public void handleOptionClick(View view) {
		String option = ((TextView) view.findViewById(R.id.overflow_itemtext)).getText().toString().trim();
		// if the user selects log out
		if (option.equals("logout")) {
			// log out
			NetworkService.getInstance().logout();
			// launch login page
			Intent i = new Intent(this.dashboardView, LoginView.class);
			this.dashboardView.startActivity(i);
		}		
	}
}
