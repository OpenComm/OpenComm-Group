package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

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
	 *
	 */
	public void addSpace() {
		if(MainApplication.user_primary.getUsername().equals(this.space.getOwner().getUsername())) {
			//TODO: What do I do here? :X
		}
	} // end of addSpace method

} // end Class SpaceController
