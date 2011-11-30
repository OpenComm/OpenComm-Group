package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;

/** An instance of this class controls participants (users) in a specific space */
public class ParticipantController {

	// Debugging
	public static final String TAG = "Controller.ParticipantController";
	public static final boolean D = true;

	// View variables

	// Model variables
	private Space mSpace;

	/** CONSTRUCTOR: a new participant controller for a specific space */
	public ParticipantController(Space mSpace) {
		this.mSpace = mSpace;
	}

	/**
	 * Removes the primary user from the Space associated with this controller.
	 * If the primary user is this Space's owner, they will be asked if they
	 * want to destroy the chat or pass ownership. If they choose to pass
	 * ownership, then they will be asked to select who to pass ownership to. If
	 * this is the conference, then the view updates to the splash screen. If
	 * this is a side chat, then the user is returned to the conference.
	 */
	public void leaveSpace(boolean inMainSpace) {
		if (inMainSpace) {
			Intent i = new Intent(mSpace.getContext(), DashboardView.class);
			mSpace.getContext().startActivity(i);
		} else {
			// TODO: update view to Conference
		}
		mSpace.getMUC().leave();
	}

	/**
	 * Grants ownership of a Space.
	 * 
	 * @param newOwner
	 *            - the JID of the User to be granted ownership
	 */
	public void grantOwnership(String newOwner, boolean isLeaving) {
		try {
			this.mSpace.getMUC().grantAdmin(newOwner);
		} catch (XMPPException e) {
			Log.d(TAG, "XMPP Exception: " + NetworkService.printXMPPError(e));
			Log.v(TAG, "Could not grant ownership to: " + newOwner);
			Log.v(TAG, "exception: " + e.getMessage());
			return;
		}

		if (isLeaving) {
			leaveSpace(mSpace.equals(Space.getMainSpace()));
		}
	}
} // end Class ParticipantController
