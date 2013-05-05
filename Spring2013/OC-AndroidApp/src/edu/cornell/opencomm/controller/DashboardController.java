package edu.cornell.opencomm.controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.ContactListView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.MyProfileView;

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
	@SuppressWarnings("unused")
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
//		int duration = Toast.LENGTH_SHORT;
//		Toast send = Toast.makeText(this.dashboardView.getApplicationContext(),
//				"ConfInfo clicked", duration);
//		send.show();
		
		
		//ArrayList<User> results = SearchService.searchByName("*");
		//ArrayList<User> results2 = NetworkService.getInstance().getBuddies();
		//Log.v(TAG, "Search results found: " + results.size());
		//Log.v(TAG, "Buddies Found: " + results2.size());
		User user = SearchService.getUser("oc6testorg");
		Log.v(TAG, "user nickname: " + user.getNickname());
		Log.v(TAG, "user jid: " + user.getVCard().getJabberId());

		
//		testing search service
//		ArrayList<VCard> results = SearchService.searchByEmail("*");
//		for (VCard vCard : results) {
//			Log.v("found user:", vCard.getFirstName() + " " + vCard.getLastName());
//		}
		
//		testing getting user
//		String jid = NetworkService.getInstance().getConnection().getUser();
//		Log.v("user logged: ", jid);
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
	public void handleConferencesButtonClicked() {
		// TODO: go to a dummy conference!
		Intent i = new Intent(this.dashboardView, ConferenceView.class);
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
		Intent account = new Intent(this.dashboardView, MyProfileView.class);
		this.dashboardView.startActivity(account);
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
		String option = ((TextView) view.findViewById(R.id.overflow_itemtext))
				.getText().toString().trim();
		// if the user selects log out
		if (option.equals("logout")) {
			// log out
			NetworkService.getInstance().logout();
			// launch login page
			Intent i = new Intent(this.dashboardView, LoginView.class);
			this.dashboardView.startActivity(i);
			this.dashboardView.overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	}
}
