package edu.cornell.opencomm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.InvitationController;
import edu.cornell.opencomm.controller.KickoutController;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.MessageController;
import edu.cornell.opencomm.controller.ParticipantController;
import edu.cornell.opencomm.controller.ParticipantStatusController;
import edu.cornell.opencomm.controller.SpaceController;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

/**
 * A Space is a chat room that holds a group of people who can talk to one
 * another. There are two types of Spaces: a single main space and many private
 * spaces. The main space is the default space controlled by the primary user.
 * Private spaces are controlled by their owner.
 */
public class Space {
	// Debugging
	private static String TAG = "Model.Space"; // for error checking
	private static boolean D = true;

	// All the Spaces in use (roomID -> Space)
	public static HashMap<String,Space> allSpaces = new HashMap<String,Space>();
	private static Space mainSpace; // the primary user's main space

	// The users who are in this Space, <JID, User>
	private HashMap<String, User> allParticipants = new HashMap<String, User>();
	// Occupant objects for all Users in Space
	private HashMap<String, Occupant> allOccupants = new HashMap<String, Occupant>();
	private HashMap<String, User> allNicks = new HashMap<String, User>();
	boolean isMainSpace; // if true, this is a main space
	User owner; // the User who has the privilege to manage the Space

	// TODO UI Team: is this still necessary? Where does it go?
	Context context;
	boolean screen_on;
	LinkedList<UserView> allIcons = new LinkedList<UserView>();
	boolean entered = false; // false if you have never opened this space to the screen

	// Network variables
	private MultiUserChat muc;
	private String roomID;

	// Controllers
	private MessageController mController;
	private ParticipantController pController;
	private InvitationController iController;
	private SpaceController sController;
	private KickoutController kController;
	private ParticipantStatusController psController;

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
	public Space(Context context, boolean isMainSpace, String roomID, User owner)
			throws XMPPException {
		this.context = context;
		// If the primary user is creating the space, join as owner
		if (MainApplication.user_primary.equals(owner)) {
			this.roomID = Network.ROOM_NAME + roomID + "@conference.jabber.org";
			this.muc = new MultiUserChat(LoginController.xmppService.getXMPPConnection(),
					this.roomID);
			this.muc.join(owner.getNickname());
			//muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			
			Form form = muc.getConfigurationForm();
			Form answerForm = form.createAnswerForm();
			answerForm.setAnswer("muc#roomconfig_moderatedroom", true);
			muc.sendConfigurationForm(answerForm);

			
			/*// Get the the room's configuration form
		      Form form = muc.getConfigurationForm();
		      // Create a new form to submit based on the original form
		      Form submitForm = form.createAnswerForm();
		      // Add default answers to the form to submit
		      for (Iterator fields = form.getFields(); fields.hasNext();) {
		          FormField field = (FormField) fields.next();
		          if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
		              // Sets the default value as the answer
		              submitForm.setDefaultAnswer(field.getVariable());
		          }
		      }
		      // Sets the new owner of the room
		      List owners = new ArrayList();
		      owners.add(owner.getNickname());
		      submitForm.setAnswer("muc#roomconfig_roomowners", owners);
		      // Send the completed form (with default values) to the server to configure the room
		      muc.sendConfigurationForm(submitForm);*/
		}
		// otherwise join as participant
		else {
			this.roomID = roomID;
			this.muc = new MultiUserChat(LoginController.xmppService.getXMPPConnection(),
					this.roomID);
			this.muc.join(MainApplication.user_primary.getNickname());
		}
		this.owner = owner;
		this.isMainSpace = isMainSpace;
		// create controllers and associate view
		if (isMainSpace()) {
			sController = new SpaceController(this,
					(SpaceView) ((Activity) context)
							.findViewById(R.id.space_view));
			Space.mainSpace = this;
		} else {
			sController = new SpaceController(this, new SpaceView(context, this));
		}
		this.mController = new MessageController(this);
		this.pController = new ParticipantController(this);
		this.iController = new InvitationController(this);
		this.kController = new KickoutController(this);
		this.psController = new ParticipantStatusController(this);
		// Create and instantiate all existing users
		Iterator<String> occItr = this.muc.getOccupants();
		while (occItr.hasNext()) {
			String occ = occItr.next();
			String occJID = occ.substring(occ.indexOf('/') + 1) + "@jabber.org";
			Log.d(TAG, occJID);
			// if there is an instance of User already created
			if (User.getAllUsers().get(occJID) != null) {
				this.allParticipants.put(occJID,
						(User.getAllUsers().get(occJID)));
				this.allNicks.put(User.getAllUsers().get(occJID).getNickname(),
						User.getAllUsers().get(occJID));
			} else {
				User u = new User(occJID, occJID.substring(0,
						occJID.indexOf('@')), R.drawable.question);
				this.allParticipants.put(occJID, u);
				this.allNicks.put(u.getNickname(), u);
			}
			Log.d(TAG, "Is get Occupant for occJID " + occJID + " valid? "
					+ (this.muc.getOccupant(occ) != null));
			allOccupants.put(occJID, this.muc.getOccupant(occ));
		}
		allSpaces.put(this.roomID, this);
	} // end Space constructor

	// GETTERS
	
	/** @return - the main space associated with this instance of the application */
	public static Space getMainSpace(){
		return mainSpace;
	}
	
	/** Sets the main space.*/
	public static void setMainSpace(Space main){
		mainSpace = main;
	}
	
	/** Set the entered state of the room: false if you have enver opened the space in
	 * the spaceview ever 
	 * @param entered
	 */
	public void setEntered(boolean entered){
		this.entered = entered;
	}
	
	public boolean getEntered(){
		return entered;
	}

	/** @return - all participants in Space, maps JID to User */
	public HashMap<String, User> getAllParticipants() {
		return allParticipants;
	} // end getAllParticipants method

	/** @return - the id of this Space */
	public String getRoomID() {
		return roomID;
	} // end getRoomID method

	/** @return - true if this Space is a main space, false otherwise */
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

	/** @return - the MultiUserChat associated with this Space */
	public MultiUserChat getMUC() {
		return muc;
	} // end getMUC method

	/** @return - the Space Controller associated with this Space */
	public SpaceController getSpaceController() {
		return sController;
	} // end getSpaceController method

	/** @return the KickoutController associated with this Space */
	public KickoutController getKickoutController() {
		return kController;
	} // end getKickoutController method

	/** @return the MessageController associated with this Space */
	public MessageController getMessageController() {
		return mController;
	} // end getMessageController method

	/** @return the InvitationController associated with this Space */
	public InvitationController getInvitationController() {
		return iController;
	} // end getInvitationController method

	/** @return the ParticipantController associated with this Space */
	public ParticipantController getParticipantController() {
		return pController;
	} // end getParticipantController method

	public ParticipantStatusController getPsController() {
		return psController;
	}

	// TODO: move to Controller?
	public boolean isScreenOn() {
		return screen_on;
	} // end isScreenOn method

	public void setScreenOn(boolean isIt) {
		screen_on = isIt;
	}

	public LinkedList<UserView> getAllIcons() {
		return allIcons;
	}

	public HashMap<String, Occupant> getAllOccupants() {
		return allOccupants;
	}
	
	public HashMap<String, User> getAllNicksnames() {
		return allNicks;
	}

	public static HashMap<String, Space> getAllSpaces(){
		return allSpaces;
	}
	
	public Context getContext() {
		return context;
	}

	// add getters for four listeners
} // end Class Space

