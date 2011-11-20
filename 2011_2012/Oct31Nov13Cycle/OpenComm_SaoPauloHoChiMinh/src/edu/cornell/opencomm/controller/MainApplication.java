package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.AdminTipView;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.InvitationView;
import edu.cornell.opencomm.view.LoginView;
import edu.cornell.opencomm.view.MenuView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.PrivateSpacePreviewPopup;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.TipView;
import edu.cornell.opencomm.view.UserView;


/** The MainApplication handles and manages the PrivateSpaces for every
 * User involved. Receives its notifications from the GUI, and then
 * updates the data of the private space, and talks with the network. */
public final class MainApplication extends Activity{
	/** String identifier for error checking with LOGCAT */
	private static String TAG = "Controller.MainApplication"; 
	private static boolean D = true;
	
	/** Dummy people for debugging */
	private User debug;
	private User debug1;

    /** The user of this program (You, the person holding the phone) */
    public static User user_primary;
    
    /** The SpaceView object (UI) representing the space that the user is currently talking to */
    public static SpaceView screen; 
    /** The empty private space icon at the bottom of the screen */
    public static PrivateSpaceIconView emptyspace;
    
    //Controllers
    SideChatIconMenuController sideChatIconMenuController;
    
    public static Values viewDimensions = new Values();

	/** TODO Network - Do you need any of these?  -Nora 11/6 */
    public static LinkedList<User> allBuddies; // Your buddy list! Has been previously saved from the network
    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection
    private static String username=""; // the username of this account
    public static final String PS_ID = "edu.cornell.opencomm.which_ps";
	
    /** Parameters needed to describe the objects created in the XML code */
    LinearLayout.LayoutParams PSparams = new LinearLayout.LayoutParams(
    		ViewGroup.LayoutParams.WRAP_CONTENT,
    		ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
	protected ParticipantStatusController PsController;

 // A counter for spaces (to generate SpaceID's). TODO Will use for now, takeout later when add network
 	public static int space_counter= -1;

    
    /** TODO Network - There are many times when the the onCreate method failes to create a 
     * space and therefore make the rest of this code crash. This usually gives an 
     * error 80% of the time, so it is probably linked to the network service. 
     * The source of the error is the line:
     * this.muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
     * in the Space class. Please fix this so that it works 100% of the time.
     * 
     * -Nora 11/6
     * 
     */
    
    

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
        
        // Open up the layout specified by the main XML
        setContentView(R.layout.main);
        // Change the parameters of the appearance according to screen size
        //adjustLayoutParams();
        // This spaceview already created for you in the XML file, so retrieve it
        screen = (SpaceView)findViewById(R.id.space_view);
     // Check if the mainspace was already created
        if (Space.getMainSpace() == null){
        	// Obtain username used to log into the application
            Intent start_intent= getIntent();
            username = start_intent.getStringExtra(Network.KEY_USERNAME);
        	// Create instance of primary user
            if(user_primary == null){
            	user_primary = new User(username, username.split("@")[0], 
            			R.drawable.question);
            }
        	this.plusButtonSetUp();
        	try {
        		// create the mainspace
				SpaceController.createMainSpace(this);

				// create an empty private space
				//SpaceController.addSpace(this);

				// TODO add private space preview
			} catch (XMPPException e) {
			//	Log.e(TAG, "onCreate - Error (" + e.getXMPPError().getCode()
			//			+ ") " + e.getXMPPError().getMessage());
				e.printStackTrace();
			}
        	screen.setSpace(Space.getMainSpace());
        	Space.getMainSpace().setScreenOn(true);
        }
        initializeButtons();

        //Initializes the onKeyListener to record keypad events
        screen.setOnKeyListener(onKeyListener);
        
		//DEBUG: create User object to test invitations and kickouts
		debug = new User("opencommsec@jabber.org", "opencommsec", 0);
		//for (Space s : Space.allSpaces) Log.v(TAG, s.getRoomID());
		debug1 = new User("mucopencomm@jabber.org", "mucopencomm", 0);
    }

    /** An onKeyListner to listen to any key events.
     * Will be used mainly for debugging purposes.
     */
    public View.OnKeyListener onKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction()!=KeyEvent.ACTION_DOWN) {
				return true;
			}
			switch (keyCode) {
			case KeyEvent.KEYCODE_1: {
				Log.v(TAG, "pressed 1 key - confirmation screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ConfirmationView confirmationView = new ConfirmationView(inflater);
				confirmationView.launch();
				break;
			}
			case KeyEvent.KEYCODE_2: {
				Log.v(TAG, "pressed 2 key - invitation screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				InvitationView invitationView = new InvitationView(inflater);
				invitationView.launch();
				break;
			}
			case KeyEvent.KEYCODE_3: {
				Log.v(TAG, "pressed 3 key - login screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//LoginView loginView = new LoginView(inflater);
				//loginView.launch();
				break;
			}
			case KeyEvent.KEYCODE_4: {
				Log.v(TAG, "pressed 4 key - tip screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				TipView tipView = new TipView(inflater);
				tipView.launch();
				break;
			}
			case KeyEvent.KEYCODE_5: {
				Log.v(TAG, "pressed 5 key - admin tip screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				AdminTipView adminTipView = new AdminTipView(inflater);
				adminTipView.launch();
				break;
			}
			case KeyEvent.KEYCODE_6: {
				Log.v(TAG, "pressed 6 key - dashboard screen");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//DashboardView dashboardView = new DashboardView(inflater);
				//dashboardView.launch();
				break;
			}
			case KeyEvent.KEYCODE_M: {
				// invite a user to the mainspace. Assume inviter is owner
				int i = 0;
				Log.v(TAG, "pressed M key - invitation" + i++);
				(Space.getMainSpace().getInvitationController()).inviteUser(debug, "You're fun!");
				break;
			}
			case KeyEvent.KEYCODE_N: {
				Log.v(TAG, "pressed N key - kickout");
				try {
					(Space.getMainSpace().getKickoutController()).kickoutUser(debug, "You suck!");
				} catch (XMPPException e) {
					Log.d(TAG, "Couldn't kick!");
				}
				break;
			}
			case KeyEvent.KEYCODE_V: {
				Log.v(TAG, "pressed V key - change owner");
				Space.getMainSpace().getParticipantController().grantOwnership(
						"opencommsec@jabber.org", false);
			}
			case KeyEvent.KEYCODE_J: {
				Log.v(TAG, "Pressed J key - join");
				/*ParticipantStatusController psController = new ParticipantStatusController(mainspace);
				String test = "roomname@conference.jabber.org/" + debug1.getNickname();
				psController.joined(test);*/
				
				
				Space.getMainSpace().getPsController().joined("roomname@conference.jabber.org/" + debug1.getNickname());
				break;
			}
			case KeyEvent.KEYCODE_S: {
				Log.v(TAG, "Pressed S key - create a new private space");
				try{
				SpaceController.addSpace((Context)MainApplication.screen.getContext());
				} catch (XMPPException e){
					Log.v(TAG, "Can't create another private space :(");
				}
				break;
			}
			case KeyEvent.KEYCODE_MENU:{
				Log.v(TAG, "Clicked on MENU key");
				LayoutInflater inflater = (LayoutInflater) MainApplication.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				MenuView menuView = new MenuView(inflater);
				menuView.launch();
				break;
			}
			case KeyEvent.KEYCODE_B: {
				Log.v(TAG, "pressed B key - leave space");
				Space.getMainSpace().getParticipantController().leaveSpace(false);
				break;
			}
			}
			;
			return true;
		}
	};



	/** Adjust the parameters of the main layout according to the Values class.
     * For situations when the phone size is different */
    public void adjustLayoutParams(){
    	// Calculations
    	Display display = getWindowManager().getDefaultDisplay();
 
    	//viewDimensions.setValues(display.getWidth(), display.getHeight());
    	
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
					if(!(screen.getSpace()==Space.getMainSpace())){
						MainApplication.screen.getSpaceViewController().changeSpace(Space.getMainSpace());
					}
					break;
				}
				return false;
			}
		});
		Button actionBar = (Button) findViewById(R.id.ocActionBar);
		actionBar.setOnClickListener(this.getActionBarOnClickListener());
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
  //  public Space init_createPrivateSpace(boolean isMainSpace) throws XMPPException{

        /* TODO network:
         * 1) Notify network of new private space to create
         * 2) Place YOU in this privatespace and make you moderator (on network)
         * 3) Should perhaps return an id# for this space? Put it in the newSpaceID variable, or you
         * can keep the space_counter that is already implemented
         */
    /*    int newSpaceID=space_counter++;
		return createPrivateSpace(true, isMainSpace, null, String.valueOf(newSpaceID), user_primary);
    } */

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
  /*  public void changeSpace(Space s){
    	SpaceView spaceView = (SpaceView)findViewById(R.id.space_view);
    	screen.getSpace().setScreenOn(false);
    	spaceView.changeSpace(s);
    	s.setScreenOn(true);

    	/* TODO network:
    	 * 1) Adjust sound in network (if you want the space onscreen to be louder than other for example)
    	 */
    //} */

    /** Need to add the new PrivateSpace button to the bottom GUI by altering the XML code */
    public void addPrivateSpaceButton(PrivateSpaceIconView psv){
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
        
        //Crystal
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW);
        lp.setMargins(0, 0, Values.iconBorderPaddingH, 0);
        
        
		psv.setLayoutParams(new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW));
		psv.setPadding(Values.iconBorderPaddingH, Values.iconBorderPaddingV,Values.iconBorderPaddingH, Values.iconBorderPaddingV);
		bottomBar.addView(psv,lp);
		bottomBar.invalidate(); 

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
    
    /** Reupdates the spaceview UI. Does the same thing as invalidate(). 
     * Purpose of this method is so that any thread that is not from the UI 
     * or the activity may call it (to avoid the "Only the original thread that 
     * created a view hierarchy can touch its views" error)
     */
    public void invalidateSpaceView(){
    	Log.v("MainApplication", "Invalidating SpaceView");
    	runOnUiThread(new Runnable() {
    	     public void run() {
    	     	Log.v("MainApplication", "run()");
    	    	 MainApplication.screen.draw(new Canvas());
    	    	 screen.invalidate();
    	    	 screen.postInvalidate();
    	    	 Log.v("MainApplication", "run() after");
    	    }
    	});
    }
    
    /**Crystal: add the plus button to the bottom bar*/
    public void plusButtonSetUp(){
    	LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
    	 LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW);
         lp.setMargins(0, 0, Values.iconBorderPaddingH, 0); 
    	ImageView plus=PrivateSpaceIconView.plusSpaceButton(getResources().getColor(R.color.off_white),this);
    	 plus.setBackgroundColor(getResources().getColor(R.color.dark_grey));
    	
   
    	 plus.setOnClickListener(new OnClickListener(){

 			public void onClick(View v) {
 				// TODO NORA - might need to change to mainspace in Space class
 				try{
 					//Space.getMainSpace().getSpaceController().addSpace(Space.getMainSpace().getContext());
 					Space newSpace=Space.getMainSpace().getSpaceController().addSpace(Space.getMainSpace().getContext());
 					new PrivateSpaceIconView(Space.getMainSpace().getContext(),newSpace);
 				}
 				catch(XMPPException e){
 					Log.d("MainApplication plusButtonSetUp()", "Could not add a Space");
 				}
 			// MainApplication.this.init_createPrivateSpace(false);
 			}
 				
 			});
    	bottomBar.addView(plus,lp);
		bottomBar.invalidate(); 
    }
    
    public View.OnClickListener getActionBarOnClickListener() {
    	View.OnClickListener actionBarListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				DashboardView dashboardView = new DashboardView();
				dashboardView.launch();

			}
    	};
    	return actionBarListener;
    }

    /** Notify the network with the icon you moved so that it can update the sound simulation */
    public void movedPersonIcon(Space space, UserView icon, int x, int y){
        /* TODO network:
         * 1) Send details to network so it can update the sound sound spatialization
         */
    }

    /*delete the specific privatespaceview psv--Crystal Qin*/
    public void deletePrivateSpaceView(Space psv){
    	/*Button check=(Button) findViewById(R.id.delete_button);
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
			}); */
    } 
}
