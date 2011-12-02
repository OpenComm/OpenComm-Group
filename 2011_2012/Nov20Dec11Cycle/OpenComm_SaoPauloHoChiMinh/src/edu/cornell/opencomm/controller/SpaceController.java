package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.LinearLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

import edu.cornell.opencomm.view.PrivateSpaceIconView;

import edu.cornell.opencomm.view.NotificationView;

import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

/** An instance of this class is a space controller for each space (main space
 * or private space), such as adding/deleting people from a space.
 * @author OpenComm (cuopencomm@gmail.com)
 *
 */
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
	

	//associated privateSpaceIconView
	PrivateSpaceIconView psiv;

	static NotificationView notification_View;


	/** Constructor: a new instance of SpaceController that controls a specific
	 * space
	 * @param space - the space to be controlled */
	public SpaceController(Space space, SpaceView view) {
		this.space = space;
		this.muc = this.space.getMUC();
		this.view = view;
		SpaceController.notification_View = new NotificationView(view.getContext());

	} // end SpaceController method

	
		/** 
	 * Add a user to the space
	 * @param userRoomInfo - the user who joined the room
	 * (ex: roomname@conference.jabber.org/nickname)
	 * @param user - User object to add
	 */
	public void addUser(String userRoomInfo, User user){
		Log.v("SpaceController", "addUser()");
		space.getAllNicksnames().put(user.getNickname(), user);
		space.getAllParticipants().put(user.getUsername(), user);
		space.getAllOccupants().put(user.getUsername(), muc.getOccupant(userRoomInfo)); 
		int x = Values.staggeredAddStart + space.getAllParticipants().size()*(Values.userIconW/5);
		int y = Values.staggeredAddStart + space.getAllParticipants().size()*(Values.userIconH/5);
		UserView uv = new UserView(view.getContext(), user, user.getImage(), space, x, y);
		space.getAllIcons().add(uv);
		
		
		/* If you are currently in this space then refresh the 
		 * the spaceview ui
		 */
		if(space == MainApplication.screen.getSpace()){
			Log.v("SpaceController", "adding user to spaceView");
			view.getActivity().invalidateSpaceView();
			
		}
		if(space != Space.getMainSpace())
			view.getActivity().invalidatePSIconView(psiv);
		NotificationView notificationView = new NotificationView(view.getContext());
		notificationView.launch(user.getNickname(),"adduser");
	}
	/** set the associated PrivateSpaceIconView psIcon*/
	public void setPSIV( PrivateSpaceIconView psIcon) {
		Log.v(TAG, "setPSIV");
		this.psiv=psIcon;

		
	}
	/**
	 * Delete a user from the space
	 * @param userRoomInfo - the user who joined the room
	 * (ex: roomname@conference.jabber.org/nickname)
	 * @param user - User object to delete
	 */
	public void deleteUser(String userRoomInfo, User user){
		space.getAllNicksnames().remove(user.getNickname());
		space.getAllParticipants().remove(user.getUsername());
		space.getAllOccupants().remove(userRoomInfo);
		for(UserView uv : space.getAllIcons()){
			if(uv.getPerson()==user)
				space.getAllIcons().remove(uv);
		}
		
		this.psiv.invalidate();
		
		/* If you are currently in this space then refresh the 
		 * the spaceview ui
		 */
		if(space == MainApplication.screen.getSpace())
			view.getActivity().invalidateSpaceView();
		


		NotificationView notificationView = new NotificationView(view.getContext());
		notificationView.launch(user.getNickname(),"deleteuser");

		
	}
	
	/**
	 * If you are the Owner, deletes the space.
	 * @throws XMPPException
	 */
	public void deleteSpace() throws XMPPException {
			//TO-DO need to pop-up a confirmation dialogue
			Log.v(TAG, "Trying to destroy space " + space.getRoomID());
			this.space.getMUC().destroy(null, null);
			
			if(space.equals(Space.getMainSpace())){
				Space.setMainSpace(null);
			}
	} // end of deleteSpace method

	/**
	 * Creates a new Space
	 * @param context The context this space belongs to (i.e. MainApplication)
	 * @return the newly created space
	 * @throws XMPPException
	 */
	public static Space addSpace(Context context) throws XMPPException {
		int spaceID = MainApplication.space_counter++;
		Space space = new Space(context, false, String.valueOf(spaceID), MainApplication.user_primary);
		Space.allSpaces.put(space.getRoomID(), space);
		
		notification_View.launch("sidechat");
		
		if(D) Log.d(TAG, "Created a new space with ID:" + spaceID);
		return space;
	} // end of addSpace method
	
	/**
	 * Creates a new mainspace
	 * @param context The context this space belongs to (i.e. MainApplication)
	 * @return the newly created space
	 * @throws XMPPException
	 */
	public static Space createMainSpace(Context context) throws XMPPException {
		if(Space.getMainSpace() != null) {
			if(D) Log.d(TAG, "Tried to create main space when one already exists");
			return null;
		}
		int spaceID = MainApplication.space_counter++;
		Space mainSpace = new Space(context, true, String.valueOf(spaceID), MainApplication.user_primary);
		Space.setMainSpace(mainSpace);
		if(D) Log.d(TAG, "Created a new main space with ID:" + spaceID);
		return mainSpace;
	} // end of createMainSpace method

} // end Class SpaceController
