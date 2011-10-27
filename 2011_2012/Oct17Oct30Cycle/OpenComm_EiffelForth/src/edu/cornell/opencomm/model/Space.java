package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.controller.Login;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.network.Network;
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

	// The users who are in this Space
	ArrayList<User> allUsers = new ArrayList<User>();
	String spaceID; // the ID # that the network will use to identify this space
	boolean isMainSpace; // if true, this is a main space
	User owner; // the User who has the privilege to manage the Space

	// Network variables
	private MultiUserChat muc;
	private String roomID;


	/**
	 * CONSTRUCTOR: = a completely new Space. If a main space, also initializes
	 * allSpaces
	 * 
	 * @param context
	 * @param isMainSpace
	 *            - if true, this is a main space
	 * @param roomID
	 *            - ID the network will use to identify this Space
	 * @param owner
	 *            - user who is the administrator of this Space
	 * @param existingUsers
	 *            - users already in the Space. if isMainSpace, null
	 * @throws XMPPException
	 *             - thrown if the room cannot be created or configured
	 */
	public Space(boolean isMainSpace, String roomID, User owner,
			ArrayList<User> existingUsers) throws XMPPException {
		this.roomID = roomID; // probably more useful than the spaceID
		this.isMainSpace = isMainSpace;
		if (isMainSpace) {
			this.owner = MainApplication.user_you;
		} else {
			this.owner = owner;
		}
		this.muc = new MultiUserChat(Login.xmppService.getXMPPConnection(),
				this.roomID);
		this.muc.join(MainApplication.user_you.getNickname());
		if (isMainSpace) {
			this.muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			this.muc.addInvitationRejectionListener(new InvitationRejectionListener() {
				public void invitationDeclined(String arg0, String arg1) {
					// TODO
				}
			});
			this.addInvitationRequestListener();
			this.addInvitationAcceptanceListener();
			this.addKickoutRequestListener();
		} else {
			addManyUsers(existingUsers);
		}
		allSpaces.add(this);
	}

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

	/**
	 * Adds a User to this Space.
	 * 
	 * @param newUser
	 *            - the User to be added to the Space
	 */
	public void addUser(User newUser) {
		if (!allUsers.contains(newUser)) {

			// DEBUG
			if (D) {
				Log.d(TAG,
						"addUser: inviting user " + newUser.getUsername()
								+ " to room " + this.roomID);
			}
			allUsers.add(newUser);
			this.muc.invite(newUser.getUsername(), Network.DEFAULT_INVITE);
			return;
		}
		Log.i("User already present", "Check your code");
	}

	/**
	 * Adds many users to this Space.
	 * 
	 * @param users
	 *            - a collection of Users to be added to the Space
	 * */
	public void addManyUsers(Collection<User> users) {
		for (User user : users) {
			addUser(user);
		}
	}

	/**
	 * Removes a User from this Space.
	 * 
	 * @param username
	 *            - User to be removed from the Space
	 */
	public void deleteUser(User username) throws XMPPException {

		if (allUsers.contains(username)) {
			allUsers.remove(username);
			if (D) {
				Log.d(TAG, "Attempt to kick out " + username.getUsername());
			}
			this.muc.kickParticipant(username.getNickname(),
					Network.DEFAULT_KICKOUT);
			if (D) {
				Log.d(TAG, "Kicked out " + username.getUsername());
			}
		} else {
			Log.i("User not present", "Check your code!");
		}
	}

	/**
	 * Removes multiple users from the Space.
	 * 
	 * @param toDelete
	 *            - an ArrayList of Users to remove
	 */
	public void deleteManyUsers(ArrayList<User> toDelete) throws XMPPException {
		for (User user : toDelete)
			deleteUser(user);
	}

	public void addInvitationRequestListener() {
		// TODO
	}

	public void addKickoutRequestListener() {
		// TODO
	}

	public void addInvitationAcceptanceListener() {
		// TODO
	}

	/**
	 * Sets the moderator of this space. Cannot change the moderator from
	 * primary user if this Space is a main space
	 * 
	 * @param owner
	 *            - User to be set as moderator
	 */
	public void setOwner(User owner) throws XMPPException {
		if (!isMainSpace())
			this.owner = owner;
		this.muc.grantAdmin(owner.getUsername());
	}

	// GETTERS

	/** @return - the list of all the people who are in this space */
	public ArrayList<User> getAllUsers() {
		return allUsers;
	}

	/** @return - true if this Space is a main space, false otherwise */
	public boolean isMainSpace() {
		return isMainSpace;
	}

	/** @return - the id of this space */
	public String getRoomID() {
		return roomID;
	}

	/** @return - the moderator of this space. */
	public User getOwner() {
		return owner;
	}
	
	/** @return - the MultiUserChat room */
	public MultiUserChat getMUC() {
		return muc;
	}
}
