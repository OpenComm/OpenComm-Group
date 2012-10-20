package edu.cornell.opencomm.model;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.view.UserView;

/**
 * A Space is a chat room that holds a group of people who can talk to one
 * another. There are two types of Spaces: a single main space and many private
 * spaces. The main space is the default space controlled by the primary user.
 * Private spaces are controlled by their owner.
 */
public class Space {
	private static boolean D = false;
	private static String TAG = "Space";

	// All the Spaces in use (roomID -> Space)
	public static HashMap<String, Space> allSpaces = new HashMap<String, Space>();
	private static Space mainSpace; // the primary user's main space

	// The users who are in this Space, <JID, User>
	private HashMap<String, User> allParticipants = new HashMap<String, User>();
	private HashMap<String, User> allNicks = new HashMap<String, User>();
	boolean isMainSpace; // if true, this is a main space
	User owner; // the User who has the privilege to manage the Space

	LinkedList<UserView> allIcons = new LinkedList<UserView>();

	/**
	 * CONSTRUCTOR: new space. Creates the SpaceController and, either creates
	 * or identifies the SpaceView associated with this space.
	 * 
	 * @param context
	 * @param isMainSpace
	 *            - if true, this instance is a main space
	 *            <ul>
	 *            <li>If the space is being created:
	 * @param roomID
	 *            - ID the network uses to identify this space<br>
	 *            (ex: "roomID")
	 * @param owner
	 *            - the primary user and owner of the space </li> <li>If the
	 *            space was created by another use and the primary user is
	 *            joining it:
	 * @param roomID
	 *            - ID the network uses to identify this space<br>
	 *            (ex: "roomID@conference.jabber.org")
	 * @param owner
	 *            - the owner of the space </li>
	 *            </ul>
	 * @throws XMPPException
	 *             - thrown if the room cannot be created, or configured
	 */
	public Space(Context context, boolean isMainSpace, String roomID,
			boolean selfCreated) {
		if (D)
			Log.d(TAG, "Space constructor called");
		// this.context = context;

	} // end Space constructor

	/**
	 * @return - the main space associated with this instance of the application
	 * */
	public static Space getMainSpace() {
		return mainSpace;
	}

	/**
	 * Sets the main space.
	 * */
	public static void setMainSpace(Space main) {
		mainSpace = main;
	}

	/**
	 * @return - all participants in Space, maps JID to User
	 * */
	public HashMap<String, User> getAllParticipants() {
		return allParticipants;
	} // end getAllParticipants method

	/**
	 * @return - true if this Space is a main space, false otherwise
	 * */
	public boolean isMainSpace() {
		return isMainSpace;
	} // end isMainSpace method

	/**
	 * @return - the owner of this space. We assume there is only one owner and
	 *         that there is no discrepancy of information between network and
	 *         local
	 */
	public User getOwner() {
		return this.owner;
	} // end getOwner method

	/**
	 * @param owner
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return allIcons
	 */
	public LinkedList<UserView> getAllIcons() {
		return allIcons;
	}

	/**
	 * @return allNicknames
	 */
	public HashMap<String, User> getAllNicknames() {
		return allNicks;
	}

	/**
	 * @return allSpaces
	 */
	public static HashMap<String, Space> getAllSpaces() {
		return allSpaces;
	}

} // end Class Space