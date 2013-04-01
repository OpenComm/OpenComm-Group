package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.MyProfileView;

public class ContactCardController {
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
	private static final String TAG = ContactCardController.class.getSimpleName();
	private static final String BLOCK_LIST_NAME = "Blocked Users";

	private ContactCardView contactCardView;

	public ContactCardController(ContactCardView v) {
		this.contactCardView = v;
	}

	/** Back button clicked: launch contactlistview */
	public void handleBackButtonClicked() {
		this.contactCardView.onBackPressed();
	}

	/** Add button clicked: if the user is not yet a friend, make it a friend and update add icon; 
	 * otherwise, display toast indicating that it's already a friend */
	public void handleAddButtonClicked(boolean isFriend) {
		if (!isFriend) {
			// TODO [backend] add as a friend and update contactlist
			// update add icon to check if successful
			ImageView addIcon = (ImageView) this.contactCardView.findViewById(R.id.contact_card_addIcon);
			addIcon.setImageDrawable(this.contactCardView.getResources().getDrawable(R.drawable.action_check));
		}
		else {
			Toast.makeText(this.contactCardView, "Contact already added", Toast.LENGTH_SHORT).show();
		}
	}

	public void handleBlockButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(contactCardView.getApplicationContext(),
				"Block Button Pressed", duration);
		toast.show();
		/*Log.v(TAG, "Blocking user " + contact.getUsername());
		PrivacyItem item = new PrivacyItem(PrivacyItem.Type.jid.name(), false,
				1);
		item.setValue(contact.getUsername());
		XMPPConnection currentConnection = NetworkService.getInstance()
				.getConnection();
		PrivacyListManager privacyManager = PrivacyListManager
				.getInstanceFor(currentConnection);
		List<PrivacyItem> privacyList;
		try {
			// Gets list of blocked users for current connection
			privacyList = NetworkService.getInstance().getBlockList()
					.getItems();
			int loc = privacyList.indexOf(item);
			if (loc == -1) {
				// Blocks user represented by this contact card from
				// communicating with currently connected user
				privacyList.add(item);
			} else {
				// Removes communication restrictions if button is clicked for
				// an already-blocked user
				privacyList.remove(loc);
			}
			privacyManager.updatePrivacyList(BLOCK_LIST_NAME, privacyList);
		} catch (XMPPException e) {
			Log.e(TAG, "Unable to attain block list");
		}*/
	}

		/**
	 * Shows/hides Overflow
	 */
	public void handleOverflowButtonClicked() {
		if (this.contactCardView.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.contactCardView.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.contactCardView.getOverflowList()
					.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Overflow option clicked:
	 * <ul>
	 * <li>conferences: launches conferences page</li>
	 * <li>account: launches profile page</li>
	 * </ul>
	 * */
	public void handleOptionClick(View view) {
		String option = ((TextView) view.findViewById(R.id.overflow_itemtext))
				.getText().toString().trim();
		// if the user selects conferences
		if (option.equals("conferences")) {
			// launch conferences page
			Intent i = new Intent(this.contactCardView,
					ConferenceSchedulerView.class);
			this.contactCardView.startActivity(i);
		}
		// if the user selects account
		else if (option.equals("profile")) {
			// launch my profile page
			Intent i = new Intent(this.contactCardView, MyProfileView.class);
			this.contactCardView.startActivity(i);
		}
	}
}
