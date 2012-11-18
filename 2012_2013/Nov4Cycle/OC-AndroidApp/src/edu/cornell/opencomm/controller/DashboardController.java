package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.MyAccountView;

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
 * - [frontend] Implement Action Bar functionality
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
		// start conference view
    	Intent i = new Intent(this.dashboardView,ContactsView.class);
    	this.dashboardView.startActivity(i);
	}

	/**
	 * Launches Profile Button
	 */
	public void handleProfileButtonClicked() {
		Intent i = new Intent(this.dashboardView,MyAccountView.class);
    	this.dashboardView.startActivity(i);
	}

	public void handleNotificationClicked() {
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Action Bar: Notification clicked",duration);
    	send.show();
    	// TODO launch notification overlay
		
	}

	public void handleOverflowClicked() {
		int duration = Toast.LENGTH_SHORT;
    	Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),"Action Bar: Overflow clicked",duration);
    	send.show();
    	// TODO launch notification overlay
		
	}
}
