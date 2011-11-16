package edu.cornell.opencomm.controller;

import java.util.Collection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
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
	
	/** Removes the primary user from the Space associated with this controller.
	 * If the primary user is this Space's owner, they will be asked if they 
	 * want to destroy the chat or pass ownership. If they choose to pass 
	 * ownership, then they will be asked to select who to pass ownership to.
	 * If this is the conference, then the view updates to the splash screen. 
	 * If this is a side chat, then the user is returned to the conference.
	 */
	public void leaveSpace(){
		if (MainApplication.user_primary.equals(mSpace.getOwner())){
			//TODO: give prompt to destroy or pass ownership
			boolean destroy = false;
			if (destroy){
				try {
					mSpace.getSpaceController().deleteSpace();
				} catch (XMPPException e) {
					Log.v(TAG, "Can't destroy space!");
				}
			} else{
				//TODO: update View with a prompt asking for new Owner by nickname.
				//TODO: Set new Owner nickname to newOwnerNick
				String newOwnerNick = "opencommsec"; 
				String newOwner = "";
				Collection<User> inSpace = mSpace.getAllParticipants().values();
				for (User u : inSpace){
					if (u.getNickname().equals(newOwnerNick)){
						newOwner = u.getUsername();
					}
				}
				grantOwnership(newOwner);
				mSpace.getMUC().leave();
			}
		}
		if (mSpace.equals(Space.getMainSpace())) {
			//TODO: update View to splash screen
		}
		else {
			//TODO: update View to conference
		}
	}
	
	/** Grants ownership of a Space.
	 * @param newOwner - the JID of the User to be granted ownership
	 */
	public void grantOwnership(String newOwner){
		try {
			this.mSpace.getMUC().grantOwnership(newOwner);
		} catch (XMPPException e) {
			Log.v(TAG, "Could not grant ownership to: " + newOwner);
			Log.v(TAG, "exception: " + e.getMessage());
		}
	}
} // end Class ParticipantController
