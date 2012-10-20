/**
 * An instance of this class is a space controller for each space (main space
 * or private space), such as adding/deleting people from a space.
 * @author vinaymaloo
 *
 */
package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.util.Values;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

public class SpaceController {

	// Debugging
	private static final String TAG = "SpaceController";
	private static final boolean D = true;

	// Model objects
	private Space space; // the space that is controlled

	// Network objects
	private MultiUserChat muc;

	// View objects
	SpaceView view;

	// associated privateSpaceIconView
	PrivateSpaceIconView psiv;

	static NotificationView notificationView;

	/**
	 * Constructor: a new instance of SpaceController that controls a specific
	 * space
	 * 
	 * @param space
	 *            - the space to be controlled
	 */
	public SpaceController(Space space, SpaceView view) {
		this.space = space;
		this.muc = this.space.getMUC();
		this.view = view;
		SpaceController.notificationView = new NotificationView(
				view.getContext());

	} // end SpaceController method

	/**
	 * Add a user to the space
	 * 
	 * @param userRoomInfo
	 *            - the user who joined the room (ex:
	 *            roomname@conference.jabber.org/nickname)
	 * @param user
	 *            - User object to add
	 */
	public void addUser(String userRoomInfo, User user) {
	}

	public PrivateSpaceIconView getPSIV() {
		return psiv;
	}

	/** set the associated PrivateSpaceIconView psIcon */
	public void setPSIV(PrivateSpaceIconView psIcon) {
		Log.v(TAG, "setPSIV");
		this.psiv = psIcon;
	}

	/**
	 * Delete a user from the space
	 * 
	 * @param userRoomInfo
	 *            - the user who joined the room (ex:
	 *            roomname@conference.jabber.org/nickname)
	 * @param user
	 *            - User object to delete
	 */
	public void deleteUser(String userRoomInfo, User user) {
	}

	/**
	 * TODO: If you are the Owner, deletes the space.
	 * 
	 * 
	 */
	public void deleteSpace() {
		
	} 

	/**
	 * Adds a room thats already created (somewhere else) to the current user's
	 * spaces. Called in sidechat invitation. Can add to left or right. Will
	 * destroy either of the space before insertng the new space in the old
	 * location.
	 */
	public static Space addExistingSpace(Context context, boolean isMainSpace,
			String roomID, boolean isLeft) {

		return null;
	}

	public static Space swapMainSpace(Context context, String roomID) {
		// Get rid of old main space.s
		return null;
	}

	/**
	 * Creates a new Space
	 * 
	 * @param context
	 *            The context this space belongs to (i.e. MainApplication)
	 * @return the newly created space
	 * @throws XMPPException
	 */
	public static Space addSpace(Context context) throws XMPPException {
		//FIXME
		return null;
	} // end of addSpace method

	/**
	 * Creates a new mainspace
	 * 
	 * @param context
	 *            The context this space belongs to (i.e. MainApplication)
	 * @return the newly created space
	 * @throws XMPPException
	 */
	public static Space createMainSpace(Context context) throws XMPPException {
		//FIX me
		return null;
	}
} // end Class SpaceController
