package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.PrivateSpacePreviewPopup;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;


/** The MainApplication handles and manages the PrivateSpaces for every
 * User involved. Receives its notifications from the GUI, and then
 * updates the data of the private space, and talks with the network. */
public class MainApplication extends Activity{
	// Debugging
	private static String TAG = "Controller.MainApplication"; // for error checking with logcat
	private static boolean D = true;
	private User debug;
	private User debug1;

    public static LinkedList allBuddies; // Your buddy list! Has been previously saved from the network
    public static User user_primary;// the user of this program
    public static Space mainspace= null; // the official mainSpace, the one space that has EVERYBODY in it
    public static SpaceView screen; // the screen that shows all of the icons
    public static PrivateSpaceIconView emptyspace;

    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection

    private static String username=""; // the username of this account

    // Parameters needed to describe the objects created in the XML code
    LinearLayout.LayoutParams PSparams = new LinearLayout.LayoutParams(
    		ViewGroup.LayoutParams.WRAP_CONTENT,
    		ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);

    public static final String PS_ID = "edu.cornell.opencomm.which_ps";

	// A counter for spaces (to generate SpaceID's). TODO Will use for now, takeout later when add network
	public static int space_counter= -1;


	/** Called when application is first created.
	 * <b>Set up:</b>
	 * <ol>
	 * <li>Create and open the main space and put the primary user (the user
	 * that started this application) into the space</li>
	 * <li>Initialize button functionality: main button</li>
	 * </ol>
	 * <b>Assumptions:</b>
	 * <ul>
	 * <li>The main space is created by the primary user</li>
	 * <li>XMPPConnection is established and authorized before this
	 * Activity is called
	 * </ul>
	 */
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	if (D) Log.d(TAG,"onCreate - Started the MainApplication activity");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        adjustLayoutParams();

        // The spaceview already created for you in the XML file, so retrieve it
        screen = (SpaceView)findViewById(R.id.space_view);

        // Check if the mainspace was already created
        if (mainspace == null){
        	// Obtain username used to log into the application
            Intent start_intent= getIntent();
            username = start_intent.getStringExtra(Network.KEY_USERNAME);
        	// Create instance of primary user
        	user_primary = new User(username, username.split("@")[0], R.drawable.question);
        	try {
        		// create the mainspace
				SpaceController.createMainSpace(this);

				// create an empty private space
				SpaceController.addSpace(this);

				// TODO add private space preview
			} catch (XMPPException e) {
				Log.e(TAG, "onCreate - Error (" + e.getXMPPError().getCode()
						+ ") " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
        	screen.setSpace(mainspace);
        	mainspace.setScreenOn(true);
        }
        initializeButtons();

        //records keypad events
        screen.setOnKeyListener(onKeyListener);
        
		//DEBUG: create User object to test invitations and kickouts
		debug = new User("opencommsec@jabber.org", "opencommsec", 0);
		//for (Space s : Space.allSpaces) Log.v(TAG, s.getRoomID());
		debug1 = new User("mucopencomm@jabber.org", "mucopencomm", 0);
    }

    public View.OnKeyListener onKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction()!=KeyEvent.ACTION_DOWN) {
				return true;
			}
			switch (keyCode) {
			case KeyEvent.KEYCODE_1: {
				Log.v(TAG, "pressed M key - confirmation screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ConfirmationView confirmationView = new ConfirmationView(inflater);
				confirmationView.launch();
				break;
			}
			case KeyEvent.KEYCODE_M: {
				// invite a user to the mainspace. Assume inviter is owner
				int i = 0;
				Log.v(TAG, "pressed N key - invitation" + i++);
				(MainApplication.mainspace.getInvitationController()).inviteUser(debug, "You're fun!");
				break;
			}
			case KeyEvent.KEYCODE_N: {
				Log.v(TAG, "pressed B key - kickout");
				try {
					(MainApplication.mainspace.getKickoutController()).kickoutUser(debug, "You suck!");
				} catch (XMPPException e) {
					Log.d(TAG, "Couldn't kick!");
				}
				break;
			}
			};
			return true;
		}
	};



    /** Adjust the parameters of the main layout according to the Values class.
     * For situations when the phone size is different */
    public void adjustLayoutParams(){
    	// Calculations
    	Values.spaceViewH = Values.screenH - Values.bottomBarH;
    	Values.privateSpaceButtonW = Values.bottomBarH - 5;


    	// Adjust the space view
    	View sv = findViewById(R.id.space_view);
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Values.screenW , Values.spaceViewH);
    	sv.setLayoutParams(lp);

    	// Adjust the bottom bar
    	View bb = findViewById(R.id.bottom_bar);
    	lp = new LinearLayout.LayoutParams(Values.screenW, Values.bottomBarH);
    	bb.setLayoutParams(lp);

    	// Adjust the main button
    	View mb = findViewById(R.id.main_button);
    	lp = new LinearLayout.LayoutParams(Values.bottomBarH, Values.bottomBarH);
    	mb.setLayoutParams(lp);

    }

    /** You are exiting the application! Definitely tell the network so it can tell
     * EVERYONE else and remove you from rooms that you were in. Save any history
     * (new friends made, recordings of discussions, chats, etc.), and delete the
     * the unimportant data. Delete privateSpaces that you were moderator of */
    public void disconnect(){
        /* TODO network:
         * 1) Save history on network
         * 2) Delete PrivateSpaces that you were moderator of (and notify people who were in that privatespace)
         * 3) Remove you from PrivateSpaces that you were in that you were not moderator of
         * 4) Disconnect connection and log off
         */
    }

    /** TODO: UI/Network - is this still necessary?
     * YOU created a new PrivateSpace, therefore can call createPrivateSpace
     * however in addition need to notify the network of the newly created
     * Private Space
     * @throws XMPPException */
    public Space init_createPrivateSpace(boolean isMainSpace) throws XMPPException{

        /* TODO network:
         * 1) Notify network of new private space to create
         * 2) Place YOU in this privatespace and make you moderator (on network)
         * 3) Should perhaps return an id# for this space? Put it in the newSpaceID variable, or you
         * can keep the space_counter that is already implemented
         */
        int newSpaceID=space_counter++;
		return createPrivateSpace(true, isMainSpace, null, String.valueOf(newSpaceID), user_primary);
    }

    /** Create a new Private Space, makes sure to put yourself (User) in the
     * Private Space. The creation can be done in the PrivateSpace constructor,
     * if preferred but called from here.
     *
     * PARAMETERS: ICreatedThis->true if YOU created this,
     * isMainSpace->true if is a mainspace, existingbuddies->list of people already
     * in this privatespace if applicable, spaceID->network number for this space,
     * moderator->person object who can control this group
     *
     * Two situations in which this method is called:
     *
     * 1) This method is called (by network) if someone else invited you to a PrivateSpace and you
     * accepted. Therefore this method should be called by the network, and this assumes
     * that the network already has added you to its network private space.
     * Then existing_buddies will hold all the people already in that PrivateSpace.
     *
     * 2) Called by init_createPrivateSpace() if YOU created this privateSpace with intention,
     * then existing buddies should be null and ICreatedThis should be true
     * @throws XMPPException */
	public Space createPrivateSpace(boolean ICreatedThis, boolean isMainSpace, LinkedList<User> existing_buddies, String spaceID, User moderator) throws XMPPException{
		// Either your first space (mainspace) or a newly created space
		return new Space(this, isMainSpace, spaceID, moderator);
    }

    /** YOU remove an existing PrivateSpace, with the intention of deleting this
     * PrivateSpace for EVERYBODY who was taking part in it (can only do that if you are
     * the moderator for this group). Therefore does same thing
     * as deletePrivateSpace except in addition needs to notify the network
     * of this deletion, the network will delete this privatespace for everyone.
     * You cannot delete a mainspace */
    public void init_deletePrivateSpace(Space spaceToDelete){
    	if(spaceToDelete.getOwner().getUsername().equals(user_primary.getUsername()) && spaceToDelete.isMainSpace()==false){
    		spaceToDelete.getMUC().getOccupants();
    		// message containing delete request tag, the username of the deleter,
			// the name of the space to be deleted, and the reason
			Message msg = new Message(Network.REQUEST_DELETE + "@requester" +
					MainApplication.user_primary.getUsername() + "@deletee" +
					user_primary.getUsername() + "@reason" +
					Network.DEFAULT_DELETE,
					Message.Type.groupchat);
			try {
				spaceToDelete.getMUC().sendMessage(msg);
			} catch (XMPPException e) {
				if (D) Log.d(TAG, "init_deletePrivateSpace - message not sent: "
						+ e.getXMPPError().getCode() + " - " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
            deletePrivateSpace(spaceToDelete);
    	} else {
    		Log.w(TAG, "Cannot delete main private space, or user does not have authority");
    	}
    }

    /** Remove an existing PrivateSpace for yourself. Make sure to also delete the
     * PrivateSpace's corresponding PrivateSpaceIconView and SpaceView.
     * This method also called (by network) if someone else deleted a PrivateSpace that you
     * were a part of, or if you decided to leave but are not moderator of the space*/
    public void deletePrivateSpace(Space spaceToDelete){
        try {
			spaceToDelete.getSpaceController().deleteSpace();
		} catch (XMPPException e) {
			Log.w(TAG, "Failed to delete space with ID:" + spaceToDelete.getRoomID());
		}
    }

    /** Change the space whose contents the screen (spaceview) is showing.
     * Need to notify network of this change so that it can adjust sound
     */
    public void changeSpace(Space s){
    	SpaceView spaceView = (SpaceView)findViewById(R.id.space_view);
    	screen.getSpace().setScreenOn(false);
    	spaceView.changeSpace(s);
    	s.setScreenOn(true);

    	/* TODO network:
    	 * 1) Adjust sound in network (if you want the space onscreen to be louder than other for example)
    	 */
    }

    /** Need to add the new PrivateSpace button to the bottom GUI by altering the XML code */
    public void addPrivateSpaceButton(PrivateSpaceIconView psv){
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		psv.setLayoutParams(new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW));
		psv.setPadding(Values.privateSpacePadding, 0, Values.privateSpacePadding, 0);
		bottomBar.addView(psv);
		bottomBar.invalidate();

		/*
		 * Displaying preview of private space when clicked on it
		 */
		psv.setOnClickListener(onClickListener);
    }

    PopupWindow privateSpacePreviewPopupWindow = null;
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			try {
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				/*View layout = inflater.inflate(R.layout.space_preview_popup,
						(ViewGroup) findViewById(R.id.private_space_preview_popup));*/

				View layout = inflater.inflate(R.layout.space_preview_popup,
						(ViewGroup) findViewById(R.id.private_space_preview_popup_layout));

				PrivateSpacePreviewPopup popupLayout = (PrivateSpacePreviewPopup)layout.findViewById(R.id.private_space_preview_popup);

				privateSpacePreviewPopupWindow = new PopupWindow(layout, 150, 158, true);

				PrivateSpaceIconView psv = (PrivateSpaceIconView) view;
				if (view != null) {
					ArrayList<UserView> personViews = new ArrayList<UserView>();
					for (UserView personView : psv.space.getAllIcons()) {
						personViews.add(personView);
					}

					popupLayout.setPersonViews(personViews);
				}

				// display the popup in the center

				privateSpacePreviewPopupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, psv.getLeft() - 55, 68/*115*/);

				Button cancelButton = (Button) layout
						.findViewById(R.id.cancel_button);
				// makeBlack(cancelButton);
				if (cancelButton != null) {
					cancelButton.setOnClickListener(onCancelClickListener);
				}

			} catch (Exception e) {
				//System.out.println(e.getMessage());
				Log.v(TAG, "Exception while inflating popup:\n" + e.getMessage());
			}
		}
	};

	private View.OnClickListener onCancelClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// ViewGroup popup =
			// (ViewGroup)v.findViewById(R.id.popup_inner_layout);
			//PopupWindow pw = (PopupWindow) v.getParent().getParent();
			privateSpacePreviewPopupWindow.dismiss();
		}
	};


    /** Need to delete the this PrivateSpace button to the bottom GUI by altering the XML code */
    public void delPrivateSpaceButton(PrivateSpaceIconView psv){
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		bottomBar.removeView(psv);
		bottomBar.invalidate();
		PrivateSpaceIconView.allPSIcons.remove(psv);
    }


    /** Remove this person from this space, take away that person's icon from the SpaceView
     * as well. Could be situations where: you do not want this person in your mainspace,
     * this person removed him/herself from the privatespace, the privatespace got deleted,
     * you kicked someone out of the group (if you are moderator)
     * @throws XMPPException */
    public void deletePerson(Space space, User person) throws XMPPException{
		space.getKickoutController().kickoutUser(person, Network.DEFAULT_KICKOUT);
        LinearLayout screen = (LinearLayout)findViewById(R.id.space_view);
        screen.invalidate();
    }

    /** Notify the network with the icon you moved so that it can update the sound simulation */
    public void movedPersonIcon(Space space, UserView icon, int x, int y){
        /* TODO network:
         * 1) Send details to network so it can update the sound sound spatialization
         */
    }

    /*delete the specific privatespaceview psv--Crystal Qin*/
    public void deletePrivateSpaceView(Space psv){
    	Button check=(Button) findViewById(R.id.delete_button);
	       Log.v(TAG, "check button " +check);
			check.setVisibility(View.VISIBLE);
			Log.v(TAG, "SEE A BUTTON");
			final Space sp= psv;
			check.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(sp!= MainApplication.mainspace){
						deletePrivateSpace(sp);
					}
				}
			});


    }

			/** Initialize the buttons declared in the xml. In this case just the MAIN button.
     * Main button: add a touch listener, when clicked should take you back to the main conversation */
	public void initializeButtons() {
		// set listener to main button
		Button mainButton = (Button) findViewById(R.id.main_button);
		mainButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					if(!(screen.getSpace()==mainspace)){
						changeSpace(mainspace);
					}
					break;
				}
				return false;
			}
		});
	}

	/** ========================================================================
	 * =========================================================================
	 * ========================================================================
	 * ========================================================================
	 * ========================================================================
	 *
	 * Move code below to appropriate Model and Controller classes
	 */
	/**
	// This function builds a dialog for buddylist
	public void showBuddyList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
		{
			public void onClick( DialogInterface dialog, int clicked, boolean selected )
			{
				//Do Nothing
			}
		}


		class DialogButtonClickHandler implements DialogInterface.OnClickListener
		{
			public void onClick( DialogInterface dialog, int clicked )
			{
				switch( clicked )
				{
					case DialogInterface.BUTTON_POSITIVE:
//						for( int i = 0; i < _options.length; i++ ){
//							Log.i( "ME", _options[i] + " selected: " + _selections[i] );
//						}
					try {
						addFromBuddyList();
					} catch (XMPPException e) {
						e.printStackTrace();
					}

						break;
				}
			}
		}
		updateBuddyList();
		builder.setTitle( "Buddylist" )
    		    .setMultiChoiceItems( buddyList, buddySelection, new DialogSelectionClickHandler() )
    		    .setPositiveButton( "OK", new DialogButtonClickHandler() )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}


	// Add users from the buddylist dialog to the main space
	public void addFromBuddyList() throws XMPPException{
		for( int i = 0; i < buddySelection.length; i++ ){
			if(buddySelection[i]){
				username = (String) buddyList[i];
				User p = new User(username + "@jabber.org", username, R.drawable.question);
				if (D) Log.d(LOG_TAG, "Adding person " + username + " to mainspace");
	        	initAddPerson(mainspace, p);
			}
		}
	} // end addFromBuddyList method

	// Updates the buddylist and the boolean selection array
	public void updateBuddyList() {
		// obtain current buddylist from server
		Roster xmppRoster = Login.xmppService.getXMPPConnection().getRoster();
		Collection<RosterEntry> entryCollection = xmppRoster.getEntries();
		Iterator<RosterEntry> entryItr = entryCollection.iterator();
		buddyList = new CharSequence[entryCollection.size()];
		int i = 0;
		while (entryItr.hasNext()) {
			buddyList[i++] = entryItr.next().getUser().split("@")[0];
		}
		buddySelection = new boolean[buddyList.length];
	} // end updateBuddyList method
	*/
}
