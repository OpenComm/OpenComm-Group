package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.SpaceView;

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
	 * If you are the Owner, deletes the space.
	 * @throws XMPPException
	 */
	public void deleteSpace() throws XMPPException {
		if(MainApplication.user_primary.getUsername().equals(this.space.getOwner().getUsername()) && !this.space.isMainSpace()) {
			this.space.getMUC().destroy(null, null);
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
		Space.allSpaces.add(space);
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
