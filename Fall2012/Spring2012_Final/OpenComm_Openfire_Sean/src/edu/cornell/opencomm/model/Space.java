package edu.cornell.opencomm.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
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
	/* Debugging
	private static boolean D = true;*/

	// Log for error-checking
	private static String TAG = "Model.Space"; 
	
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
	
	private int volume = 40;

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
	public Space(Context context, boolean isMainSpace, String roomID, boolean selfCreated/*, User owner*/)
			throws XMPPException {
		this.context = context;
		// If the primary user is creating the space, join as owner
		if (selfCreated/*MainApplication.user_primary.equals(owner)*/) {
			
			Log.v("Space", "Creating space with me as moderator");
			
			this.roomID = Network.ROOM_NAME + roomID + "@" + Network.DEFAULT_CONFERENCE;
			
			this.muc = new MultiUserChat(LoginController.xmppService.getXMPPConnection(), this.roomID);
			Log.v("Space", "Creating muc with me as moderator and roomID as"+this.roomID.toString());
			
			this.muc.join(/*owner.getNickname()*/MainApplication.user_primary.getNickname());
			Log.v("Space", "Joining me to muc");
			
			this.owner = MainApplication.user_primary;
			
			//Configure room
			Form form = muc.getConfigurationForm();
			Form answerForm = form.createAnswerForm();
			for (Iterator<FormField> fields = form.getFields(); fields.hasNext();){
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null){
					answerForm.setDefaultAnswer(field.getVariable());
				}
			}
			answerForm.setAnswer("muc#roomconfig_moderatedroom", true);
			answerForm.setAnswer("muc#roomconfig_publicroom", false);
			answerForm.setAnswer("muc#roomconfig_persistentroom", false);
			answerForm.setAnswer("muc#roomconfig_membersonly", true);
			muc.sendConfigurationForm(answerForm);
			//end experiments 
			Log.v("Space", "Configure Room finish");
		}
		// otherwise join as participant
		else {
			Log.v("Space", "Creating space with someone else as moderator");
			
			this.roomID = roomID;
			Log.v("Space", "roomID = " + roomID);
			this.muc = new MultiUserChat(LoginController.xmppService.getXMPPConnection(),
					roomID);
			this.muc.join(MainApplication.user_primary.getNickname());
			
		}
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
		Log.v("Space", "This room gonna count" );
		// starting position
		int start = Values.staggeredAddStart;
		while (occItr.hasNext()) {
			
			String occ = occItr.next();
			String occJID = occ.substring(occ.indexOf('/') + 1) + "@" + Network.DEFAULT_HOSTNAME;
			Log.v("Space", "Adding person " + occJID);
			User u = User.getAllUsers().get(occJID);
			// if there is an instance of User already created
			if (/*User.getAllUsers().get(occJID)*/ u != null) {
				Log.v("Space", "This person already existed =  " + occJID);
				this.allParticipants.put(occJID,u);
				this.allNicks.put(u.getNickname(), u);
			} else {
				Log.v("Space", "username = " + occJID + ", nickname = " + occJID.substring(0,
						occJID.indexOf('@')));
				u = new User(occJID, occJID.substring(0,
						occJID.indexOf('@')), R.drawable.question);
				Log.v("Space", "CHECK: username = " + u.getUsername() + ", nickname = " + u.getNickname());
				Log.v("Space", "This person didn't exist =  " + occJID);
				this.allParticipants.put(occJID, u);
				this.allNicks.put(u.getNickname(), u);
			}
			Log.d(TAG, "Is get Occupant for occJID " + occJID + " valid? "
					+ (this.muc.getOccupant(occ) != null));
			Log.d(TAG, "Their affiliation is: " + this.muc.getOccupant(occ).getAffiliation());
			allOccupants.put(occJID, this.muc.getOccupant(occ));
			if(u!=MainApplication.user_primary)
				allIcons.add(new UserView(context, u, R.drawable.question, this, start, start));
			start+= Values.userIconW/5;
		}
		allSpaces.put(this.roomID, this);
		// moderators
		if(selfCreated){
			this.owner = MainApplication.user_primary;
		}
		else{
			Iterator<String> iter = this.muc.getOccupants();
			this.owner = MainApplication.user_primary;
			while(iter.hasNext()){
				Occupant o = muc.getOccupant(iter.next());
				String role = o.getRole();
				if(role.equals("moderator")){
					this.owner = User.getAllNicknames().get(o.getNick());
					break;
				}
			}
		}
		this.muc.grantMembership("opencommsec@opencomm");
		this.muc.invite("opencommsec@opencomm", null);
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
	
	/** Set the entered state of the room: false if you have never opened the space in
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
	
	public void setOwner(User owner){
		this.owner = owner;
	}

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

	/*
	 * Volume level can be between 0 to 100
	 */
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		Log.d(TAG, "volume of space " + roomID + " set to " + volume);
		this.volume = volume;
	}
	
	

	// add getters for four listeners
} // end Class Space

