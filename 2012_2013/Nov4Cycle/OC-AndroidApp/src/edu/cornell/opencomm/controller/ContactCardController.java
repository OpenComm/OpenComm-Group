package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PrivacyItem;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

import edu.cornell.opencomm.view.ContactCardView;
import edu.cornell.opencomm.view.ContactListView;

public class ContactCardController {

	private static final String TAG = "Controller.ContactCardController";
	private static final String BLOCK_LIST_NAME = "Blocked Users";

	private User contact;

	private ContactCardView view;

	public ContactCardController(ContactCardView v) {
		this.view = v;
		// need way of attaining contact
		contact = new User("default", "default", 0);
	}

	public void handleBackButtonClicked() {
		Intent intent = new Intent(this.view, ContactListView.class);
		this.view.startActivity(intent);
	}

	public void handleAddButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(),
				"Add Button Pressed", duration);
		toast.show();
		/*Log.v(TAG, "Adding user " + contact.getUsername() + " to contacts");
		ArrayList<User> contactList = UserManager.getContactList();
		contactList.add(contact);
		UserManager.updateContactList(contactList);*/
	}

	public void handleOverflowButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(),
				"Overflow Button Pressed", duration);
		toast.show();
	}

	public void handleBlockButtonClicked() {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(view.getApplicationContext(),
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
}
