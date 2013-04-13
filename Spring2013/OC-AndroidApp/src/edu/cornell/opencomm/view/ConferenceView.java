package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.controller.ConferenceController_v2;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.ConferenceUser;

/**
 * 
 * @author  Spandana Govindgari [frontend], Ankit Singh[frontend],Nora NQ[frontend]
 * 
 */
public final class ConferenceView extends FragmentActivity implements
ConferenceConstants, ViewPager.OnPageChangeListener {

	private static String TAG = ConferenceView.class.getName();
	// TODO the debug flag should not be here
	private static final boolean D = true;

	/**
	 * The conference data model
	 */
	private ConferenceDataModel conferenceModel;

	/**
	 * The conference controller
	 */
	private ConferenceController_v2 conferenceController;

	/**
	 * The conference pager adaptor
	 */
	private ConferencePageAdapter mPagerAdapter;

	/**
	 * The conference (holds data about the conference) e.g. for the conferenceCardPage
	 */
	private Conference conference;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */

	private static ConferenceView _instance = null;

	// Audio variables
	private PowerManager pm;
	private WakeLock mWakeLock;

	/*
	 *controls on this view
	 */
	public TextView txtv_ConfTitle;

	public static ConferenceView getInstance() {
		if (_instance == null) {
			_instance = new ConferenceView();
		}
		return _instance;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		// Bind the conference view/activity to the layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_layout);
		//instantiate the handlers
		txtv_ConfTitle = (TextView) findViewById(R.id.confernecev2_title);


		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		// Applying font
		//txtv_ConfTitle.setTypeface(tf);


		// Bind the DataModel(s)
		// Get the main room id from the intent
		conference = getConferenceFromIntent();

		conferenceModel = new ConferenceDataModel(
				conference.getConferenceTitle(), conference.getStartDateAndTime()
				.toString(), conference.getInviter().getUsername());
		// Instantiate the controller
//		conferenceController = new ConferenceController_v2();
		// Initialize the pager
		// Audio integration
//		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyTag");
//		mWakeLock.acquire();

		initPager();
	}

	private void initPager() {
		List<Fragment> fragments = createFragments();
		mPagerAdapter = new ConferencePageAdapter(
				super.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) super.findViewById(R.id.threepanelpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(MAIN_ROOM_INDEX);
	}

	private List<Fragment> createFragments(){
		List<Fragment> conferenceFragments = new Vector<Fragment>();
		//conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(LEFT_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, MAIN_ROOM_LAYOUT, conferenceModel.getRoomByTag(MAIN_ROOM_INDEX)));
		//conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(RIGHT_ROOM_INDEX)));
		return conferenceFragments;
	}

	/**
	 * Update a specific room given the room index
	 * @param roomIndex => Examples... MAIN_ROOM_INDEX, LEFT_ROOM_INDEX, RIGHT_ROOM_INDEX
	 */
	public void updateRoom(int roomIndex){
		// TODO need lock/mutex?
		ArrayList<ConferenceUser> users = conferenceModel.getRoomByTag(roomIndex).getCUserList();
		ConferenceRoomFragment roomView = (ConferenceRoomFragment) mPagerAdapter.getItem(roomIndex);
		ArrayList<UserView> userViews = roomView.getUserView(); 
		if (users.size()>userViews.size()){
			ArrayList<ConferenceUser> usersToAdd = getUsersToAdd(users, userViews);
			addUserViewsToConference(roomView, usersToAdd);
		}
		else if (users.size()>userViews.size()){
			ArrayList<UserView> userViewsToDelete = getUserViewsToDelete(users, userViews);
			removeUserViewsFromConference(roomView, userViewsToDelete);
		}
		roomView.createTheCirleOfTrust();
	}

	/**
	 * Return a list of ConferenceUsers who have not had a userView created for them in the conference screen
	 * @param users
	 * @param userViews
	 * @return
	 */
	public ArrayList<ConferenceUser> getUsersToAdd(ArrayList<ConferenceUser> users, ArrayList<UserView> userViews){
		ArrayList<ConferenceUser> newUsers = users;
		for (UserView userView : userViews){
			newUsers.remove(userView.getCUser());
		}
		return newUsers;
	}


	/**
	 * Return a list of UserViews who do not have their corresponding ConferenceUsers in the conference
	 * @param users
	 * @param userViews
	 * @return
	 */
	public ArrayList<UserView> getUserViewsToDelete(ArrayList<ConferenceUser> users, ArrayList<UserView> userViews){
		ArrayList<UserView> userViewsToDelete = userViews;
		for(UserView userView : userViews){
			if(users.contains(userView.getCUser())){
				userViewsToDelete.remove(userView);
			}
		}
		return userViewsToDelete;
	}

	/**
	 * Create a UserView for each given ConferenceUser and add to the ConferenceRoomFragment
	 * @param users
	 * @param roomView
	 */
	public void addUserViewsToConference(ConferenceRoomFragment roomView, ArrayList<ConferenceUser> users){
		for (ConferenceUser user : users){
			UserView userView = new UserView(this, user);
			roomView.addUserView(userView);
		}
	}

	/**
	 * Remove the corresponding UserView for each given ConferenceUser and add to the ConferenceRoomFragment
	 * @param users
	 * @param roomView
	 */
	public void removeUserViewsFromConference(ConferenceRoomFragment roomView, ArrayList<UserView> userViews){
		for (UserView userView : userViews){
			roomView.removeUserView(userView);
		}
	}

	/**
	 * Exit the conference and return to the conference card page for this conference
	 */
	public void exitConference(){
		Log.d("MODERATOR", "exitConference");
		Intent i = new Intent(this, ConferenceCardView.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("conference", conference);
		i.putExtras(bundle);
		onStop();
		startActivity(i);
	}

	/**
	 * Switch the view back to the page with the given index
	 * @param roomIndex
	 */
	public void returnToPage(int roomIndex){
		ViewPager pager = (ViewPager) super.findViewById(R.id.threepanelpager);
		pager.setCurrentItem(MAIN_ROOM_INDEX);
		pager.invalidate();
	}

	/* [TODO]: check to see if this should be onPause() */
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	//triggered when add person button was pressed (on confernece_v2)
	public void addPersonClicked(View v)
	{
		//TODO: get username of person to be added (hardcoded for now)
		conferenceController.HandleAddPerson("oc4testorg@cuopencomm");
	}


	//triggered when overflow button was pressed (on confernece_v2)
	public void overflowButtonClicked(View v)
	{
		conferenceController.HandleOverflow();
	}


	//triggered when back button was pressed (on confernece_v2)
	public void backButtonClicked(View v)
	{
		conferenceController.HandleBackButton();
	}


	//change the title of the conference ("CONFERENCE NAME" by default)
	public void renameConference(String title)
	{
		conferenceController.setTitle(title);
	}



	public void displayInvitation(){
		RelativeLayout invitationBar = (RelativeLayout) findViewById(R.id.side_chat_invitation_bar);

	}


	private Conference getConferenceFromIntent() {
		Intent intent = getIntent();
		Conference conference = (Conference) intent
				.getSerializableExtra("conference");
		Log.d(TAG, "Conference from Intent :"+conference);
		return conference;
	}


	// Context Bar methods

	// TODO - need a method to check to see if this user is a regular user or
	// the moderator
	/*public void onLeaveClicked(View v) {
		this.conferenceController.leaveConference();
	}*/


	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}
	int currentPageNumber = 1;
	public void onPageSelected(int roomNumber) {
		Log.d(TAG, "Room Number selected :" + roomNumber);
		currentPageNumber = roomNumber;
		ConferenceRoomFragment fragment = (ConferenceRoomFragment) mPagerAdapter.getItem(roomNumber);

		//		fragment.createUsers();
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onStop() {
		conference = null;
		conferenceController = null;
		conferenceModel = null;
		mPagerAdapter = null; 
		super.onStop();
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int eventType = event.getKeyCode();
		boolean returnType = true;
		switch (eventType) {
		case KeyEvent.KEYCODE_I:
			System.out.println("ConferenceView.dispatchKeyEvent()");
			ConferenceRoomFragment fragment = (ConferenceRoomFragment) mPagerAdapter.getItem(currentPageNumber);
			fragment.displayInvitationBar();
			break;
		case KeyEvent.KEYCODE_J:
			//Someone joins the room
			break;
		case KeyEvent.KEYCODE_E:
			//Mod ends the conference
			break;
		case KeyEvent.KEYCODE_M:
			//When you get mode priv
			break;
		case KeyEvent.KEYCODE_K:
			//when someone gets 
			break;

		default:
			returnType = super.dispatchKeyEvent(event);
			break;
		}
		return returnType;
	}

}
