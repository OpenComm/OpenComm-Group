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
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PopupNotificationView;
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
		Log.v("SpaceController", "addUser()");
		space.getAllNicknames().put(user.getNickname(), user);
		space.getAllParticipants().put(user.getUsername(), user);
		space.getAllOccupants().put(user.getUsername(),
				muc.getOccupant(userRoomInfo));
		int x = Values.staggeredAddStart + space.getAllParticipants().size()
				* (Values.userIconW / 5);
		int y = Values.staggeredAddStart + space.getAllParticipants().size()
				* (Values.userIconH / 5);
		UserView uv = new UserView(view.getContext(), user, user.getImage(),
				space, x, y);
		space.getAllIcons().add(uv);

		/*
		 * If you are currently in this space then refresh the the spaceview ui
		 */
		if (space == MainApplication.screen.getSpace()) {
			Log.v("SpaceController", "adding user to spaceView");
			view.getActivity().invalidateSpaceView();
		}

		// if(space != Space.getMainSpace()){
		// Null pointered for something - commented out atm...
		view.getActivity().invalidatePSIconView(psiv);
		// }
		view.getActivity().launchNotificationView(user, "adduser");
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
		space.getAllNicknames().remove(user.getNickname());
		space.getAllParticipants().remove(user.getUsername());
		space.getAllOccupants().remove(userRoomInfo);
		for (UserView uv : space.getAllIcons()) {
			if (uv.getPerson() == user)
				space.getAllIcons().remove(uv);
		}

		if (space != Space.getMainSpace()) {
			view.getActivity().invalidatePSIconView(psiv);
		}
		// this.psiv.invalidate();

		/*
		 * If you are currently in this space then refresh the the spaceview ui
		 */
		if (space == MainApplication.screen.getSpace()) {
			view.getActivity().invalidateSpaceView();
		}

		view.getActivity().launchNotificationView(user, "deleteuser");
	}

	/**
	 * If you are the Owner, deletes the space.
	 * 
	 * @throws XMPPException
	 */
	public void deleteSpace() {
		// TODO: need to pop-up a confirmation dialogue
		NetworkService.destroyMUC(muc);

		if (space.equals(Space.getMainSpace())) {
			Space.setMainSpace(null);
		}

		psiv.deleteThisPSView();
	} // end of deleteSpace method

	/**
	 * Adds a room thats already created (somewhere else) to the current user's
	 * spaces. Called in sidechat invitation. Can add to left or right. Will
	 * destroy either of the space before insertng the new space in the old
	 * location.
	 */
	public static Space addExistingSpace(Context context, boolean isMainSpace,
			String roomID, boolean isLeft) {
		Space space = null;
		try {
			space = new Space(context, isMainSpace, roomID, false/* owner */);

			if (isLeft) {// 0 = left private space
				// we have to shift our new PSIV into the leftmost slot, meaning
				// we have to temp. delete the center and right, make the left
				// and then re-add.

				// This removes the leftmost space from the all space reference,
				// and deletes the space on the network side as well as the
				// view.
				Space.allSpaces
						.remove(PrivateSpaceIconView.allPSIcons.get(0).space
								.getRoomID());
				PrivateSpaceIconView.allPSIcons.get(0).space
						.getSpaceController().deleteSpace();

				// Create temp PSIV to store the info before deleting them
				PrivateSpaceIconView tempCent = PrivateSpaceIconView.allPSIcons
						.get(0);

				PrivateSpaceIconView tempRight = PrivateSpaceIconView.allPSIcons
						.get(1);

				PrivateSpaceIconView.allPSIcons.get(1).deleteThisPSView();
				PrivateSpaceIconView.allPSIcons.get(0).deleteThisPSView();

				// Constructor adds to allPSIcons, so we just need to call them
				// from left to right
				PrivateSpaceIconView psIcon = new PrivateSpaceIconView(Space
						.getMainSpace().getContext(), space);

				PrivateSpaceIconView tempCentIcon = new PrivateSpaceIconView(
						Space.getMainSpace().getContext(), tempCent.space);

				PrivateSpaceIconView tempRightIcon = new PrivateSpaceIconView(
						Space.getMainSpace().getContext(), tempRight.space);

				// Sets controller
				space.getSpaceController().setPSIV(psIcon);
			} else {// 2 = right private space. Just delete right and re-add.
				// If right space, just delete the rightmost space and insert
				// our newly created PSIV there.

				Space.allSpaces
						.remove(PrivateSpaceIconView.allPSIcons.get(2).space
								.getRoomID());
				PrivateSpaceIconView.allPSIcons.get(2).space
						.getSpaceController().deleteSpace();
				PrivateSpaceIconView.allPSIcons.get(2).deleteThisPSView();

				PrivateSpaceIconView psIcon = new PrivateSpaceIconView(Space
						.getMainSpace().getContext(), space);

				space.getSpaceController().setPSIV(psIcon);
			}

			// MainApplication.screen.getActivity().invalidatePSIconView(psIcon);
			MainApplication.screen.getActivity().invalidateSpaceView();
			Log.v("SpaceController", "this space has "
					+ space.getAllParticipants().size() + " people");

		} catch (XMPPException e) {
			Log.v("SpaceController", "Could not make an existing space for you");
		}
		return space;
	}

	public static Space swapMainSpace(Context context, String roomID) {
		// Get rid of old main space.s
		Space.allSpaces.remove(Space.getMainSpace().getRoomID());

		Space.getMainSpace().getSpaceController().deleteSpace();
		Space space = null;
		try {
			space = new Space(context, true, roomID, false);
			// Nexss 2 lines makes PS icon from newly made space, sets it to our
			// allIcons at index 1 (center space)
			PrivateSpaceIconView temp = PrivateSpaceIconView.allPSIcons.get(1);// should
																				// be
																				// 2,
																				// but
																				// we
																				// just
																				// deleted
																				// the
																				// middle
			PrivateSpaceIconView.allPSIcons.get(1).deleteThisPSView();

			PrivateSpaceIconView psIcon = new PrivateSpaceIconView(Space
					.getMainSpace().getContext(), space);

			PrivateSpaceIconView temp2 = new PrivateSpaceIconView(Space
					.getMainSpace().getContext(), temp.space);

			// PrivateSpaceIconView.allPSIcons.get(1)
			space.getSpaceController().setPSIV(
					PrivateSpaceIconView.allPSIcons.get(1));
			Space.setMainSpace(space);
			MainApplication.screen.getSpaceViewController().changeSpace(space);
			MainApplication.screen.getActivity().invalidateSpaceView();

			Log.v("SpaceController", "this space has "
					+ space.getAllParticipants().size() + " people");
		} catch (XMPPException e) {
			Log.v("SpaceController", "Could not make an existing space for you");
		}
		return space;
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
		int spaceID = MainApplication.spaceCounter++;
		Space space = new Space(context, false, String.valueOf(spaceID), true/*
																			 * MainApplication
																			 * .
																			 * user_primary
																			 */);
		Space.allSpaces.put(space.getRoomID(), space);

		Log.v("C", "pop-up notification");
		// PopupNotificationView pnv = new PopupNotificationView(context, null,
		// "tip", context.getString(R.string.sidechat_tip), "", Values.tip);
		// pnv.createPopupWindow();

		if (D)
			Log.d(TAG, "Created a new space with ID:" + spaceID);
		return space;
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
		if (Space.getMainSpace() != null) {
			if (D)
				Log.d(TAG, "Tried to create main space when one already exists");
			return null;
		}
		int spaceID = MainApplication.spaceCounter++;
		Space mainSpace = new Space(context, true, String.valueOf(spaceID),
				true/* MainApplication.user_primary */);
		Space.setMainSpace(mainSpace);
		if (D)
			Log.d(TAG, "Created a new main space with ID:" + spaceID);
		return mainSpace;
	} // end of createMainSpace method

} // end Class SpaceController
