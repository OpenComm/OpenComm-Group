package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.ContactInfoView;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

/**
 * Controller for Dashboard (DashboardView) Functionality (handled by
 * DashboardController).<br>
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
 * Issues [TODO] - [frontend] Implement notification functionality - [backend]
 * pull the most recent conference/current conf in session
 * 
 * @author Risa Naka [frontend]
 * */
public class DashboardController {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	private static final String TAG = DashboardController.class.getSimpleName();

	private DashboardView dashboardView;
	private ContactListView contactsView; 

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

//		Just making toast
//		int duration = Toast.LENGTH_SHORT;
//		Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),
//				"ConfInfo clicked", duration);
//		send.show();
		
		
//		Testing searching by Name
//		ArrayList<User> results = SearchService.searchByName("*");
//		Log.v(TAG, "Search results found: " + results.size());
//		for (User user : results) {
//			Log.v("found user: ", user.getNickname());
//		}
		
//		Testing getting buddies and presence of the first one
		ArrayList<User> results2 = NetworkService.getInstance().getBuddies();
		Log.v(TAG, "Buddies Found: " + results2.size());
		for (User user : results2) {
			Log.v("found user: ", user.getNickname() + " and his status is: " + user.getPresence());	
		}
		
		//Testing getting primary user info
		//Log.v(TAG, "Primary user's nickname: " + UserManager.PRIMARY_USER.getUsername());
		
//		Testing getUser
//		User user = SearchService.getUser("oc6testorg");
//		Log.v(TAG, "user nickname: " + user.getNickname());
//		Log.v(TAG, "user jid: " + user.getUsername());
		
		
//		testing Account Manager
//		EnhancedAccountManager.changeName("Antoine Chkaiban");
//		VCard vCard = new VCard();
//		try {
//			vCard.load(NetworkService.getInstance().getConnection());
//			String name = vCard.getFirstName()+" "+vCard.getLastName();
//			Log.v("name found: ", name);
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
		
//		testing addUserToBuddyList
//		NetworkService.getInstance().getConnection()
		
	}

	/**
	 * Launches Conference List
	 */
	public void handleConferencesButtonClicked(String roomID) {
		// TODO: go to a dummy conference!
		Intent i = new Intent(this.dashboardView, ConferenceView.class);
		i.putExtra("room_id", roomID);
		this.dashboardView.startActivity(i);
	}

	/**
	 * Launches Contact List
	 */
	public void handleContactsButtonClicked() {
		Intent i = new Intent(this.dashboardView, ContactListView.class); 
		this.dashboardView.startActivity(i);
	}

	/**
	 * Launches Profile Button
	 */
	public void handleProfileButtonClicked() {
		//int duration = Toast.LENGTH_SHORT;
		//Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),
		//		"Profile Button clicked", duration);
		//send.show();
		// launch my profile page
		Intent i = new Intent(this.dashboardView, ContactInfoView.class);
		i.putExtra(ContactInfoView.contactCardKey, UserManager.PRIMARY_USER.getUsername());
		this.dashboardView.startActivity(i);
	}

	public void handleNotificationClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),
				"Notification Button clicked", duration);
		send.show();
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.dashboardView.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.dashboardView.getOverflowList().setVisibility(View.VISIBLE);
		} else {
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
//		String option = ((TextView) view.findViewById(R.id.overflow_itemtext))
//				.getText().toString().trim();
		// if the user selects log out
		//if (option.equals("logout")) {
			// log out
			NetworkService.getInstance().logout();
			// launch login page
			Intent i = new Intent(this.dashboardView, LoginView.class);
			this.dashboardView.startActivity(i);
			/*
			this.dashboardView.overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);*/
		//}
	}

}
