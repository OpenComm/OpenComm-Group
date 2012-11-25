package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.ContactsView;
import edu.cornell.opencomm.view.MyAccountView;
import edu.cornell.opencomm.view.NotificationsView;

/**
 * Controller for Notifications (NotificationsView)
 * Functionality (handled by NotificationsController).<br>
 * When a notification is clicked, its corresponding conference card is loaded
 *
 * Issues [TODO]
 * - [frontend] Implement conference card launch
 * - [backend] push notifications
 * - [backend] retrieve conference info
 *
 * @author Risa Naka [frontend]
 * */
public class NotificationsController {

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
	private static final String TAG = NotificationsController.class
			.getSimpleName();
	private NotificationsView notificationsView;

	/**
	 * NotificationsController constructor.
	 */
	public NotificationsController(NotificationsView v) {
		this.notificationsView = v;
	}

	/** When the back arrow is pressed, go back to the previous activity */
	public void handleBackButtonClicked() {
		this.notificationsView.onBackPressed();
	}

	/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.notificationsView.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.notificationsView.getOverflowList()
					.setVisibility(View.VISIBLE);
		} else {
			this.notificationsView.getOverflowList().setVisibility(
					View.INVISIBLE);
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
		// if the user selects account
		if (option.equals("account")) {
			// launch profile page
			Intent i = new Intent(this.notificationsView, MyAccountView.class);
			this.notificationsView.startActivity(i);
		}
		// if the user selects contact
		else if (option.equals("contacts")) {
			// launch contacts page
			Intent i = new Intent(this.notificationsView, ContactsView.class);
			this.notificationsView.startActivity(i);
		}
	}

	/**
	 * When a notification is clicked, launch the corresponding conference's
	 * conference card
	 */
	public void handleNoteClick(View view) {
		String note = ((TextView) view
				.findViewById(R.id.notifications_itemtext)).getText()
				.toString().trim();
		Toast.makeText(this.notificationsView, note, Toast.LENGTH_SHORT).show();
		// TODO [frontend]: launch conference card
	}
}
