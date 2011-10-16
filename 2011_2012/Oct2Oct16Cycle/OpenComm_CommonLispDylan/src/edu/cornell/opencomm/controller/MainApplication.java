package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.R.drawable;
import edu.cornell.opencomm.R.id;
import edu.cornell.opencomm.R.layout;
import edu.cornell.opencomm.model.Person;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.popups.PrivateSpacePreviewPopup;
import edu.cornell.opencomm.view.PersonView;
import edu.cornell.opencomm.view.PrivateSpaceView;
import edu.cornell.opencomm.view.SpaceView;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.util.Log;

import android.widget.LinearLayout;


/** The MainApplication handles and manages the PrivateSpaces for every 
 * Person involved. Receives its notifications from the GUI, and then
 * updates the data of the private space, and talks with the network. */
public class MainApplication extends Activity{
	// Debugging
	private static String LOG_TAG = "OC_MainApplication"; // for error checking with logcat
	private static boolean D = true;
	
    public static LinkedList allBuddies; // Your buddy list! Has been previously saved from the network 	
    public static Person user_you;// the person object that is YOU, the user of this program 
    public static Space mainspace= null; // the official mainSpace, the one space that has EVERYBODY in it
    public static SpaceView screen; // the screen that shows all of the icons 
    public static PrivateSpaceView emptyspace;
    
    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection
    
    public static String username=""; // the username of this account
    public static String password=""; // the password to this account 
    
    // Parameters needed to describe the objects created in the XML code
    LinearLayout.LayoutParams PSparams = new LinearLayout.LayoutParams(
    		ViewGroup.LayoutParams.WRAP_CONTENT,
    		ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
    public static final String PS_ID = "edu.cornell.opencomm.which_ps";
    
	
    
	// A counter for spaces (to generate SpaceID's). TODO Will use for now, takeout later when add network 
	public static int space_counter= -1;
    
    /** Start the application! You have already been connected to the Network,
     * XMPP server, and the MUCGUI activity. You have been 
     * given the user's id (username), so...
     * 1) Open first Space (the mainspace) and put this user in it
     * 2) Fetch the buddy list from network and put chosen people in mainspace 
     * 3) Initialize button functionality: main button  */
    public void onCreate(Bundle savedInstanceState) {
    	Log.v(LOG_TAG,"Started the a MainApplication activity!");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        adjustLayoutParams();
        
        /* For now you are just given the username and password,
         * as if all went well with the network */
        Intent start_intent= getIntent();
        username=start_intent.getStringExtra(Network.KEY_USERNAME);
        
        // The spaceview already created for you in the XML file, so retrieve it
        screen = (SpaceView)findViewById(R.id.space_view);
        
        // (1) Create a mainspace, put yourself in it
        if (mainspace == null){
        	// YOU are the moderator of this new space
        	user_you = new Person(username, username.split("@")[0], R.drawable.question, "I'm awesome!"); 
        	mainspace = init_createPrivateSpace(true);
        	screen.setSpace(mainspace);
        	mainspace.setScreenOn(true);
        	
        	init_createPrivateSpace(false); // Justin : Create another empty space        	
        	
        // (2) Initialize the people in the chat
        	allBuddies = new LinkedList<Person>();
        	addPerson(mainspace, user_you);
        }
        // (3)
        initializeButtons();
    }
    
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
    
    /** YOU created a new PrivateSpace, therefore can call createPrivateSpace
     * however in addition need to notify the network of the newly created
     * Private Space */
    public Space init_createPrivateSpace(boolean isMainSpace){
    	
        /* TODO network: 
         * 1) Notify network of new private space to create
         * 2) Place YOU in this privatespace and make you moderator (on network)
         * 3) Should perhaps return an id# for this space? Put it in the newSpaceID variable, or you 
         * can keep the space_counter that is already implemented
         */
        int newSpaceID=space_counter++;
        return createPrivateSpace(true, isMainSpace, null, String.valueOf(newSpaceID), user_you);
    }
    
    /** Create a new Private Space, makes sure to put yourself (Person) in the 
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
     * then existing buddies should be null and ICreatedThis should be true */ 
	public Space createPrivateSpace(boolean ICreatedThis, boolean isMainSpace, LinkedList<Person> existing_buddies, String spaceID, Person moderator){
		// Either your first space (mainspace) or a newly created space
		if (ICreatedThis) 
            return new Space(this, isMainSpace, spaceID, moderator);
		// An already existing space that somebody else put you in.
        else
            return new Space(this, existing_buddies, spaceID, moderator);
    }
    
    /** YOU remove an existing PrivateSpace, with the intention of deleting this 
     * PrivateSpace for EVERYBODY who was taking part in it (can only do that if you are
     * the moderator for this group). Therefore does same thing
     * as deletePrivateSpace except in addition needs to notify the network
     * of this deletion, the network will delete this privatespace for everyone.
     * You cannot delete a mainspace */
    public void init_deletePrivateSpace(Space SpaceToDelete){
    	if(SpaceToDelete.getModerator()==user_you && SpaceToDelete.isMainSpace()==false){
    		/* TODO network: 
    		 * 1) Delete this PrivateSpace
    		 * 2) Notify each person in the space to be removed (can use deletePrivateSpace() for other people)
    		 * deletePrivateSpace (called below) already removes this space for YOU
    		 */
            deletePrivateSpace(SpaceToDelete);
    	}
    }
    
    /** Remove an existing PrivateSpace for yourself. Make sure to also delete the 
     * PrivateSpace's corresponding PrivateSpaceView and SpaceView. 
     * This method also called (by network) if someone else deleted a PrivateSpace that you
     * were a part of, or if you decided to leave but are not moderator of the space*/
    public void deletePrivateSpace(Space SpaceToDelete){
        SpaceToDelete.deletePrivateSpace();
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
    public void addPrivateSpaceButton(PrivateSpaceView psv){
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
    
    PopupWindow pirvateSpacePreviewPopupWindow = null;
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
							
				pirvateSpacePreviewPopupWindow = new PopupWindow(layout, 150, 158, true);

				PrivateSpaceView psv = (PrivateSpaceView) view;
				if (view != null) {
					ArrayList<PersonView> personViews = new ArrayList<PersonView>();
					for (PersonView personView : psv.space.getAllIcons()) {
						personViews.add(personView);
					}
					
					popupLayout.setPersonViews(personViews);
				}

				// display the popup in the center
				
				pirvateSpacePreviewPopupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, psv.getLeft() - 55, 68/*115*/);
				
				Button cancelButton = (Button) layout
						.findViewById(R.id.cancel_button);
				// makeBlack(cancelButton);
				if (cancelButton != null) {
					cancelButton.setOnClickListener(onCancelClickListener);
				}

			} catch (Exception e) {
				//System.out.println(e.getMessage());
				Log.v(LOG_TAG, "Exception while inflating popup:\n" + e.getMessage());
			}
		}
	};

	private View.OnClickListener onCancelClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// ViewGroup popup =
			// (ViewGroup)v.findViewById(R.id.popup_inner_layout);
			//PopupWindow pw = (PopupWindow) v.getParent().getParent();
			pirvateSpacePreviewPopupWindow.dismiss();
		}
	};

    
    /** Need to delete the this PrivateSpace button to the bottom GUI by altering the XML code */
    public void delPrivateSpaceButton(PrivateSpaceView psv){
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		bottomBar.removeView(psv);
		bottomBar.invalidate();
		PrivateSpaceView.allPSIcons.remove(psv);
    }
    
    /** YOU invited this person to the PrivateSpace, so invite them over the network, you must 
     * still wait for their response though (through accept_invite() or decline_invite()) before you
     * can take any further action. 
     * If is a mainspace then no need for an invitation, just add it to your own (addPerson). TODO ask Risa about this
     * Make sure every time you add a person to confirm if this person is real in your buddylist */
    public void initAddPerson(Space space, Person person){
        if(space.isMainSpace())
            addPerson(space, person); 
        else
        	sendInvite(space, person);
    }
    
    /** You want to invite someone to chat in this group, so invite them over the network, you must
     * still wait for their response though (through accept_invite() or decline_invite()) before you
     * can take any action */
    public void sendInvite(Space ourspace, Person newPerson){
        /* TODO network: 
         * 1) Notify network that you have invited newPerson to ourspace, and put in some pending invite queue
         * 2) Send newPerson an invite:
         * 2a) If they accept (they used accept_invite()) then add newPerson to space in network, send updates to 
         * all people in that space to add a new person (addPerson()), create a new Space for newPerson (createPrivateSpace())
         * 2b) If they decline (they used decline_invite()) then take the invite out of the pending invite queue (in the network)
         */
    	
    	//Nora: For now am just going to pretend like the person accepted the request
    	addPerson(ourspace, newPerson);
    }
    
    /** Add this new person to this Space, make sure to also make an icon for this person and add it.
     * Could be situations where: you added this person to mainspace through buddylist,
     * this person joined a privatespace you are in (without you knowing), you invited this
     * this person to the privatespace and s/he accepted. This method will usually be called by the 
     * Network itself */
    public void addPerson(Space space, Person person){
        space.addPerson(person);
    }
    
    /** Set the moderator of this space to this person. The moderator gets special privelege and control
     * for a private space. If you used init_createPrivateSpace() then YOU are automatically moderator */
    public void setModerator(Space space, Person person){
    	/* TODO network:
    	 * 1) Change the space's moderator to this new person
    	 */
    	space.setModerator(person);
    }
    
    /** Accept invitation to join a PrivateSpace. Network needs to know to tell everyone
     * that you joined and needs to update the privatespace */
    public void acceptInvite(Space space){
        /* TODO network: (Same as part 2a of send_invite())
         * 1) Notify network that you accepted the invitation
         * 2) Create a new space for YOU and add you to the space (createPrivateSpace()), network
         * has to initiate these methods so that it can put in existing people in the privatespace
         * 3) Send updates to all people in this space to add you to their space (addPerson())
         * 4) Take pending invite out of the waiting queue
         */
    }
    
    /** Decline invitation to join a PrivateSpace. Network needs to know so that it can stop
     * waiting for your response */
    public void delineInvite(Space space){
        /* TODO network: (Same as part 2b of send_invite())
         * 1) Notify network that you declined the invitation
         * 2) Take pending invite out of the waiting queue
         */
    }
    
    /** YOU kick this person out of the PrivateSpace, BUT only if you are moderator,
     * make sure to tell the network */
    public void initDeletePerson(Space space, Person person){
        if(space.getModerator()==user_you){
            /*TODO network: 
             * 1) Remove this person from the space (in the network)
             * 2) Update all the people's (who are in that space) spaces to remove this person (deletePerson()) 
             */          
            deletePerson(space, person);
        }
    }
    
    /** Remove this person from this space, take away that person's icon from the SpaceView
     * as well. Could be situations where: you do not want this person in your mainspace,
     * this person removed him/herself from the privatespace, the privatespace got deleted, 
     * you kicked someone out of the group (if you are moderator) */
    public void deletePerson(Space space, Person person){
        try {
			space.removePerson(person);
	        LinearLayout screen = (LinearLayout)findViewById(R.id.space_view);
	        screen.invalidate();
		} catch (XMPPException e) {
			Log.e(LOG_TAG, "Cannot kick out person " + person.getUsername() + " from space " + space.getSpaceID()
					+ "\nXMPPException error: " + e.getXMPPError().getCode());
			e.printStackTrace();
		}
    }
    
    /** Notify the network with the icon you moved so that it can update the sound simulation */
    public void movedPersonIcon(Space space, PersonView icon, int x, int y){
        /* TODO network: 
         * 1) Send details to network so it can update the sound sound spatialization
         */
    }
    
    /*delete the specific privatespaceview psv--Crystal Qin*/
    public void deletePrivateSpaceView(Space psv){
    	Button check=(Button) findViewById(R.id.delete_button);
	       Log.v(LOG_TAG, "check button " +check);
			check.setVisibility(View.VISIBLE);
			Log.v(LOG_TAG, "SEE A BUTTON");
			final Space sp= psv;
			check.setOnClickListener(new OnClickListener(){
                    
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
	
	
	
	/* This function builds a dialog for buddylist*/
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
	
	/* Add users from the buddylist dialog to the main space*/
	public void addFromBuddyList() throws XMPPException{
		for( int i = 0; i < buddySelection.length; i++ ){
			if(buddySelection[i]){
				username = (String) buddyList[i];
				Person p = new Person(username + "@jabber.org", username, R.drawable.question, "xmppID");
				if (D) Log.d(LOG_TAG, "Adding person " + username + " to mainspace");
	        	initAddPerson(mainspace, p);
			}
		}
	} // end addFromBuddyList method
	
	/** Updates the buddylist and the boolean selection array */
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
}
