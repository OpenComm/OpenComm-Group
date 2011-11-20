package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
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

	/** Constructor: a new instance of SpaceController that controls a specific
	 * space
	 * @param space - the space to be controlled */
	public SpaceController(Space space, SpaceView view) {
		this.space = space;
		this.muc = this.space.getMUC();
		this.view = view;

	} // end SpaceController method

	/** 
	 * Add a user to the space
	 * @param userRoomInfo - the user who joined the room
	 * (ex: roomname@conference.jabber.org/nickname)
	 * @param user - User object to add
	 */
	public void addUser(String userRoomInfo, User user){
		space.getAllNicksnames().put(user.getNickname(), user);
		space.getAllParticipants().put(user.getUsername(), user);
		space.getAllOccupants().put(user.getUsername(), muc.getOccupant(userRoomInfo)); // ask
		UserView uv = new UserView(view.getContext(), user, user.getImage(), space);
		space.getAllIcons().add(uv);
		/* If you are currently in this space then refresh the 
		 * the spaceview ui
		 */
		if(space == MainApplication.screen.getSpace()){
			MainApplication m = (MainApplication)space.getContext();
			m.invalidateSpaceView();
		}
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
		/* If you are currently in this space then refresh the 
		 * the spaceview ui
		 */
		if(space == MainApplication.screen.getSpace()){
			MainApplication m = (MainApplication)space.getContext();
			m.invalidateSpaceView();
		}
		
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
