/** The MainApplication handles and manages the PrivateSpaces for every
 * User involved. Receives its notifications from the GUI, and then
 * updates the data of the private space, and talks with the network. */

package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
import android.widget.TextView;
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
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SoundSettingsView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.TipView;

public final class MainApplication extends Activity{
    /** String identifier for error checking with LOGCAT */
    private static String TAG = "Controller.MainApplication";
    private static boolean D = true;

    /** Dummy people for debugging */
    private User debug;
    private User debug1;

    /** The user of this program (You, the person holding the phone) */
    public static User userPrimary;

    /** The SpaceView object (UI) representing the space that the user is currently talking to */
    public static SpaceView screen;
    /** The empty private space icon at the bottom of the screen */
    public static PrivateSpaceIconView emptyspace;

    //Controllers
    SideChatIconMenuController sideChatIconMenuController;

    //font
    private Typeface font;

    public static Values viewDimensions = new Values();

    /** TODO Network - Do you need any of these?  -Nora 11/6 */
    public static LinkedList<User> allBuddies; // Your buddy list! Has been previously saved from the network
    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection
    private static String username =""; // the username of this account
    public static final String PS_ID = "edu.cornell.opencomm.which_ps";

    /** Parameters needed to describe the objects created in the XML code */
    LinearLayout.LayoutParams PSparams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);
    protected ParticipantStatusController PsController;

    // A counter for spaces (to generate SpaceID's). TODO Will use for now, takeout later when add network
    public static int spaceCounter = -1;


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
            if(userPrimary == null){
                userPrimary = new User(username, username.split("@")[0],
                        R.drawable.question);
            }
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
            //screen.getSpaceViewController().changeSpace(Space.getMainSpace());
            Space.getMainSpace().setScreenOn(true);
            //Space.getMainSpace().setEntered(true);

            font = Typeface.createFromAsset(getAssets(), Values.font);
            Button mainButtonText = (Button) findViewById(R.id.main_button);
            mainButtonText.setTypeface(font);

        }
        this.plusButtonSetUp(0);
        initializeButtons();

        //Initializes the onKeyListener to record keypad events
        screen.setOnKeyListener(onKeyListener);

        //DEBUG: create User object to test invitations and kickouts
        debug = new User("opencommsec@jabber.org", "opencommsec", 0);
        //for (Space s : Space.allSpaces) Log.v(TAG, s.getRoomID());
        debug1 = new User("mucopencomm@jabber.org", "mucopencomm", 0);

        // Change screen dimensions to 480x800
        Values.setValues(480, 800);

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
            case KeyEvent.KEYCODE_7: {
                Log.v(TAG, "pressed 7 key - sound settings screen");
                LayoutInflater inflater = (LayoutInflater) MainApplication.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                SoundSettingsView soundSettingsView = new SoundSettingsView(inflater);
                soundSettingsView.launch();
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
                Space.getMainSpace().getKickoutController().kickoutUser(debug, "You suck!");
                break;
            }
            case KeyEvent.KEYCODE_V: {
                Log.v(TAG, "pressed V key - change owner");
                Space.getMainSpace().getParticipantController().grantOwnership(
                        "opencommsec@jabber.org", false);
                break;
            }
            case KeyEvent.KEYCODE_Z: {
                Iterator<String> affiliates = Space.getMainSpace().getMUC().getOccupants();
                while (affiliates.hasNext()){
                    String a = affiliates.next();
                    Log.v(TAG, a + "'s affiliaton is " +
                            Space.getMainSpace().getMUC().getOccupant(a).getAffiliation());
                }
                break;
            }
            case KeyEvent.KEYCODE_J: {
                Log.v(TAG, "Pressed J key - join");
                /*ParticipantStatusController psController = new ParticipantStatusController(mainspace);
				String test = "roomname@conference.jabber.org/" + debug1.getNickname();
				psController.joined(test);*/


                Space.getMainSpace().getParticipantStatusController().joined("roomname@conference.jabber.org/" + debug1.getNickname());
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
            case KeyEvent.KEYCODE_Q: {
                Log.v(TAG, "pressed Q key - you (a moderator) received an invite request");
                InvitationController ic= MainApplication.screen.getSpace().getInvitationController();
                // fake invite request
                String inviteRequest = "" + Network.REQUEST_INVITE + "@inviter" + debug.getUsername()
                        + "@invitee" + debug1.getUsername() + "@reason" + "Because you're a cool cat.";
                ic.receiveInvitationRequest(inviteRequest);
                break;
            }
            case KeyEvent.KEYCODE_W: {
                Log.v(TAG, "pressed W key - you received an invite to a chat");
                InvitationController ic= MainApplication.screen.getSpace().getInvitationController();
                // fake invite request
                String inviteRequest = "" + Network.REQUEST_INVITE + "@requester" + debug.getUsername()
                        + "@invitee" + MainApplication.userPrimary.getUsername() + "@reason" + "Because you're a cool cat.";
                ic.receiveInvitationRequest(inviteRequest);
                /*LoginController.xmppService.getInvitiationListener().invitationReceived(LoginController.xmppConnection,
						"", debug.getUsername(), "reason", "password", new Message("blah message")); */
                break;
            }
            case KeyEvent.KEYCODE_E: {
                Log.v(TAG, "pressed E key - you (a moderator) received a kickout request");
                KickoutController kc = MainApplication.screen.getSpace().getKickoutController();
                // fake kickout request
                String kickoutRequest = "" + Network.REQUEST_KICKOUT + "@requester" + debug.getUsername() +
                        "@kickee" + debug1.getUsername() + "@reason" + "Because you didn't give me food.";
                kc.receiveKickoutRequest(kickoutRequest);
                break;
            }
            case KeyEvent.KEYCODE_R: {
                Log.v(TAG, "pressed R key - you received a kickout confirmation");
                KickoutController kc = MainApplication.screen.getSpace().getKickoutController();
                // fake kickout request
                String kickoutRequest = "" + Network.REQUEST_KICKOUT + "@requester" + debug.getUsername() +
                        "@kickee" + MainApplication.userPrimary.getUsername() + "@reason" + "Because you didn't give me food.";
                kc.receiveKickoutRequest(kickoutRequest);
                break;
            }
            case KeyEvent.KEYCODE_A: {
                Log.v(TAG, "pressed A key - logout");
                MainApplication.this.disconnect();
                break;
            }
            case KeyEvent.KEYCODE_H: {
                Log.v(TAG, "pressed H key - invite request");
                Space.getMainSpace().getInvitationController().inviteUser(
                        debug1, Network.DEFAULT_INVITE);
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

        TextView logo = (TextView) findViewById(R.id.logo);
        logo.setClickable(true);
        logo.setOnClickListener(this.logoOnClickListener());

        ImageView soundButton = (ImageView) findViewById(R.id.sound_button);
        soundButton.setClickable(true);
        soundButton.setOnClickListener(this.getSoundButtonOnClickListener());
        /*Log.v(TAG, "Clicked on MENU key");
		LayoutInflater inflater = (LayoutInflater) MainApplication.this
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MenuView menuView = new MenuView(inflater);
		menuView.launch();*/
    }

    /** You are exiting the application! Definitely tell the network so it can tell
     * EVERYONE else and remove you from rooms that you were in. Save any history
     * (new friends made, recordings of discussions, chats, etc.), and delete the
     * the unimportant data. Delete privateSpaces that you were moderator of */
    public void disconnect(){
        //Leave all side chats (after leaving all of the side chats, the screen should be at the main space
        for (Space s : Space.getAllSpaces().values()){
            if (!s.isMainSpace()){
                if (D) Log.v(TAG, "leaving a side chat");
                //s.getParticipantController().leaveSpace(false);
                s.getMUC().leave();
            }
            else{
                //Leave main space
                if (D) Log.v(TAG, "leaving the main space");
                s.getMUC().leave();
            }
        }
        userPrimary = null;
        Space.setMainSpace(null);
        buddyList = null;
        buddySelection = null;
        PrivateSpaceIconView.allPSIcons.clear();

        //Disconnect
        if(D) Log.v(TAG, "starting disconnect");

        //Disconnect and return to login screen
        LoginController.xmppService.disconnect();
        LoginController.xmppService = null;

        //if (D) Log.v(TAG, "is there a connection? "+LoginController.xmppService.getXMPPConnection().isConnected());
        finishFromChild(this);

        if (D) Log.v(TAG, "starting intent stuff");

        Intent i = new Intent(MainApplication.this, LoginView.class);
        startActivity(i);
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
    /*public Space createPrivateSpace(boolean ICreatedThis, boolean isMainSpace, LinkedList<User> existing_buddies, String spaceID, User moderator) throws XMPPException{
		// Either your first space (mainspace) or a newly created space
		return new Space(this, isMainSpace, spaceID, moderator);
    }*/

    /** Remove an existing PrivateSpace for yourself. Make sure to also delete the
     * PrivateSpace's corresponding PrivateSpaceIconView and SpaceView.
     * This method also called (by network) if someone else deleted a PrivateSpace that you
     * were a part of, or if you decided to leave but are not moderator of the space*/
    public void deletePrivateSpace(Space spaceToDelete){
        spaceToDelete.getSpaceController().deleteSpace();
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
        bottomBar.removeViewAt(PrivateSpaceIconView.allPSIcons.size()-1);
        bottomBar.addView(psv,lp);
        plusButtonSetUp(PrivateSpaceIconView.allPSIcons.size());
        bottomBar.invalidate();

    }

    PopupWindow privateSpacePreviewPopupWindow = null;
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

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


    /** Removes all UI traces of a privatespace/sidechat
     * 1) Removes space from hashmap of all spaces
     * 2) Removes the corresponding privatespaceButton
     * 3) Changes the space to the Mainspace (but only if wasn't a mainspace)
     * However, if you are leaving the mainspace, then start a new activity
     * and go ack to the dashboard*/
    public void delPrivateSpaceUI(Space space, boolean isMainSpace){
        if(isMainSpace){
            Intent i = new Intent(MainApplication.screen.getSpace().getContext(),
                    DashboardView.class);
            MainApplication.screen.getSpace().getContext().startActivity(i);
        }
        else{
            // 1
            Space.allSpaces.remove(space.getRoomID());
            // 2
            PrivateSpaceIconView foundIcon=null;
            for(PrivateSpaceIconView ps : PrivateSpaceIconView.allPSIcons){
                if(ps.getSpace()==space)
                    foundIcon = ps;
            }
            if(foundIcon!=null)
                delPrivateSpaceButton(foundIcon);
            // 3
            screen.getSpaceViewController().changeSpace(Space.getMainSpace());
        }
    }

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screen.invalidate();
            }
        });
    }

    /** Same reasons as invalidateSpaceView() except to invalidate
     * a privatespaceicon when someone is add/removed from the space
     * @param psv - the PrivateSpaceIconView whose GUI needs to be updated
     */
    public void invalidatePSIconView(final PrivateSpaceIconView psv){
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                psv.invalidate();
            }
        });
    }

    /** Same reasons as invalidateSpaceView() except to launch a PopupWindow.
     * In this case used to launch invitation/confirmation views
     * @param iv
     */
    public void displayPopup(final InvitationView iv){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                iv.launch();
            }
        });
    }

    public void displayEmptySpaceMenu(final AlertDialog alert){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                alert.show();
            }
        });
    }

    public void launchNotificationView(final User user, final String notify){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotificationView notificationView = new NotificationView(MainApplication.this);
                notificationView.launch(user.getNickname(), notify);
            }
        });
    }

    /**Crystal: add the plus button to the bottom bar*/
    public void plusButtonSetUp(int position){
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Values.privateSpaceButtonW,  Values.privateSpaceButtonW);
        lp.setMargins(0, 0, Values.iconBorderPaddingH, 0);
        ImageView plus=PrivateSpaceIconView.plusSpaceButton(getResources().getColor(R.color.off_white),this);
        plus.setBackgroundColor(getResources().getColor(R.color.dark_grey));


        plus.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO NORA - might need to change to mainspace in Space class
                try{
                    //Space.getMainSpace().getSpaceController().addSpace(Space.getMainSpace().getContext());
                    Space newSpace=Space.getMainSpace().getSpaceController().addSpace(Space.getMainSpace().getContext());
                    PrivateSpaceIconView psIcon=new PrivateSpaceIconView(Space.getMainSpace().getContext(),newSpace);
                    newSpace.getSpaceController().setPSIV(psIcon);
                }
                catch(XMPPException e){
                    Log.d("MainApplication plusButtonSetUp()", "Could not add a Space");
                }
                // MainApplication.this.init_createPrivateSpace(false);
            }

        });
        bottomBar.addView(plus,position,lp);
        bottomBar.invalidate();
    }

    /** Justin : Call when the sound button is clicked **/
    public View.OnClickListener getSoundButtonOnClickListener() {
        View.OnClickListener soundButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //DashboardView dashboardView = new DashboardView();
                //dashboardView.launch();
                Log.v(TAG, "Clicked on Sound button");
                LayoutInflater inflater = (LayoutInflater) MainApplication.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                SoundSettingsView soundView = new SoundSettingsView(inflater);
                soundView.launch();

            }
        };
        return soundButtonListener;
    }


    /** Rahul : Click on logo and go back to dashboard **/
    public View.OnClickListener logoOnClickListener() {
        View.OnClickListener logoListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked on Logo");

                //Intent i = new Intent(getBaseContext(), DashboardView.class);
                //i.putExtra(Network.KEY_USERNAME, username);
                //i.setAction(Network.ACTION_LOGIN);
                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //getApplication().startActivity(i);

            }
        };
        return logoListener;
    }

}
