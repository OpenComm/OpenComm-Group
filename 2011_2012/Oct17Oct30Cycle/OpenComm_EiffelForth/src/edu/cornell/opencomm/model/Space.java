package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.pubsub.Affiliation;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.Login;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.SpaceController;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

/** TODO Kris - fix the model class to reflect not actions taking w/ user input
 * but simply the information of the model object itself in accordance to the MVC model
 * A Space is a chat room that holds a group of people who can talk to one
 * another. There are two types of Spaces: a single main space and many private
 * spaces. The main space is the default space controlled by the primary user.
 * Private spaces are controlled by their owner.
 */
public class Space {
	// Debugging
	private static String TAG = "Model.Space"; // for error checking
	private static boolean D = true;

	// All the Spaces in use
	public static ArrayList<Space> allSpaces = new ArrayList<Space>();
	private static Space mainSpace;

	// The users who are in this Space
	private HashMap<String, User> allParticipants = new HashMap<String, User>();
	String spaceID; // the ID # that the network will use to identify this space
	boolean isMainSpace; // if true, this is a main space
	User owner; // the User who has the privilege to manage the Space

	// TODO UI Team: is this still necessary?
	// Not sure these are actually part of model
	Context context;
	boolean screen_on;
	LinkedList<UserView> allIcons = new LinkedList<UserView>();
	
	// View variables
	private SpaceView spaceView;
	
	// Network variables
	private MultiUserChat muc;
	private String roomID;
	private SpaceController spaceCtrl;


	/** CONSTRUCTOR: new space. Creates the SpaceController and, either creates or
	 * identifies the SpaceView associated with this space.
	 * @param context
	 * @param isMainSpace - if true, this instance is a main space
	 * <ul>
	 * <li>If the space is being created:
	 * @param roomID - ID the network uses to identify this space<br>
	 * (ex: "roomID")
	 * @param owner - the primary user and owner of the space
	 * </li>
	 * <li>If the space was created by another use and the primary user is joining it:
	 * @param roomID - ID the network uses to identify this space<br>
	 * (ex: "roomID@conference.jabber.org")
	 * @param owner - the owner of the space
	 * </li>
	 * </ul>
	 * @throws XMPPException - thrown if the room cannot be created, or configured
	 */
	public Space(Context context, boolean isMainSpace, String roomID, User owner) throws XMPPException {
		// If the primary user is creating the space
		if (MainApplication.user_primary.equals(owner)) {
			this.roomID = Network.ROOM_NAME + roomID + "@conference.jabber.org";
			this.muc = new MultiUserChat(Login.xmppService.getXMPPConnection(),
					this.roomID);
			this.muc.join(owner.getNickname());
			this.muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		}
		// otherwise
		else {
			this.roomID = roomID;
			this.muc = new MultiUserChat(Login.xmppService.getXMPPConnection(),
					this.roomID);
			this.muc.join(MainApplication.user_primary.getNickname());
		}
		this.owner = owner;
		this.spaceCtrl = new SpaceController(this);
		// Create and instantiate all existing users
		Iterator<String> occItr = this.muc.getOccupants();
		while (occItr.hasNext()) {
			String occJID = occItr.next().split("@jabber.org/")[0] + "@jabber.org";
			// if there is an instance of User already created
			if (User.getAllUsers().get(occJID) != null) {
				this.allParticipants.put(occJID, (User.getAllUsers().get(occJID)));
			}
			else {
				this.allParticipants.put(occJID, new User(occJID, 
					occJID.split("@")[0], R.drawable.question));
			}
		}
		// if this is a main space
		if (isMainSpace()) {
			// set spaceview to the one precreated by the XML file
			this.spaceView = (SpaceView)((Activity) context).findViewById(R.id.space_view);
			Space.mainSpace = this;
		}
		// otherwise
		else {
			this.spaceView = new SpaceView(context, Space.mainSpace);
		}
		allSpaces.add(this);
	} // end Space method


	/* TODO this should be in 
	 * Deletes this private space from the static list of existing Spaces
	 * (allSpaces) unless the Space is a main space. Also calls network to
	 * destroy the associated MultiUserChat.
	 */
	public void deletePrivateSpace() throws XMPPException {
		if (!isMainSpace) {
			allSpaces.remove(this);
			this.muc.destroy(null, null);
		}
	}

	// GETTERS

	/** @return - hashmap of all of the users who are in this space */
	public HashMap<String, User> getAllParticipants() {
		return allParticipants;
	} // end getAllParticipants method
	
	/** @return - the id of this space */
	public String getRoomID() {
		return roomID;
	} // end getRoomID method

	/** @return - true if this Space is a main space, false otherwise */
	public boolean isMainSpace() {
		return isMainSpace;
	} // end isMainSpace method

	/** @return - the owner of this space. We assume there is only one owner 
	 * and that there is no discrepancy of information between network and local */
	public User getOwner() {
		return this.owner;
	} // end getOwner method
	
	/** @return - the MultiUserChat room */
	public MultiUserChat getMUC() {
		return muc;
	}
	
	/** @return - the Space Controller */
	public SpaceController getSpaceController() {
		return this.spaceCtrl;
	} // end getSpaceController method
	
	//not sure these are in model
	public boolean isScreenOn(){
		return screen_on;
	}
	
	public void setScreenOn(boolean isIt){
		screen_on = isIt;
	}
	
	public LinkedList<UserView> getAllIcons(){
		return allIcons;
	}
}
