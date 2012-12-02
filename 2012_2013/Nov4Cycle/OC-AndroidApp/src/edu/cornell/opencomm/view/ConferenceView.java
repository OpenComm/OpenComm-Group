package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceUser;
import edu.cornell.opencomm.model.User;

/**
 * 
 * @author  Spandana Govindgari [frontend], Ankit Singh[frontend],Nora NQ[frontend]
 * 
 */
public final class ConferenceView extends FragmentActivity implements
		ConferenceConstants, ViewPager.OnPageChangeListener {

	// TODO: Get rid of all the local boolean flags
	private boolean areActionBarsDisplayed = false;

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
	private ConferenceController conferenceController;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		// Bind the conference view/activity to the layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_layout);
		// Bind the DataModel(s)
		// Get the main room id from the intent
		conference = getConferenceFromIntent();
	
		conferenceModel = new ConferenceDataModel(
				conference.getConferenceTitle(), conference.getStartDateAndTime()
						.toString(), conference.getInviter().getUsername());
		// Instantiate the controller
		conferenceController = new ConferenceController(this, conferenceModel);
		// Initialize the pager
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
		conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(LEFT_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, MAIN_ROOM_LAYOUT, conferenceModel.getRoomByTag(MAIN_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(RIGHT_ROOM_INDEX)));
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
 
	
	/**
	 * When user clicks an empty space on the screen toggle between showing both
	 * the action and bottom bar
	 * 
	 * @param screen
	 */
	public void clickedEmptySpace(View screen) {
		String view = screen.getContentDescription().toString();
		int visibility = View.VISIBLE;
		if (areActionBarsDisplayed)
			visibility = View.INVISIBLE;
		areActionBarsDisplayed = !areActionBarsDisplayed;
		
		if (view.equals("mainChat")) {
			RelativeLayout action_bar = (RelativeLayout) screen
					.findViewById(R.id.action_bar);
			boolean isModeratorOfConference = true; // TODO check to see if user is moderator from backend
			int bottom_bar_id = (isModeratorOfConference ? R.id.bottom_bar_conference_action_moderator : R.id.bottom_bar_conference_action);
			RelativeLayout bottom_bar = (RelativeLayout) screen
					.findViewById(bottom_bar_id);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		} else {
			RelativeLayout action_bar = (RelativeLayout) screen
					.findViewById(R.id.actionbar_sidechat);
			boolean isModeratorOfChat = false; // TODO check to see if user is moderator for this chat from backend
			int bottom_bar_id = (isModeratorOfChat ? R.id.bottom_bar_chat_action_moderator : R.id.bottom_bar_chat_action);
			RelativeLayout bottom_bar = (RelativeLayout) screen
					.findViewById(bottom_bar_id);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		}
		RelativeLayout bottom_bar_user_action = (RelativeLayout) screen.findViewById(R.id.bottom_bar_user_action);
		RelativeLayout bottom_bar_user_action_moderator = (RelativeLayout) screen.findViewById(R.id.bottom_bar_user_action_moderator);
		bottom_bar_user_action.setVisibility(View.INVISIBLE);
		bottom_bar_user_action_moderator.setVisibility(View.INVISIBLE);
	

	}

	public void addPersonClicked(View v) {
		this.conferenceController.addPersonClicked();
	}

	// Should make the other person's phone vibrate
	public void pingClicked(View v) {
		// vibe.vibrate(2000); //making it vibrate for 2000 ms
	}

	public void muteClicked(View v) {
		Button mute = (Button) v;

		int resId = conferenceModel.toggleMute() ? R.drawable.mute
				: R.drawable.unmute;
		mute.setBackgroundResource(resId);
		this.conferenceController.muteClicked();

	}

	public void overflowButtonClicked(View v) {
		this.conferenceController.handleOverflow();
	}

	private Conference getConferenceFromIntent() {
		Intent intent = getIntent();
		Conference conference = (Conference) intent
				.getSerializableExtra("conference");
		Log.d(TAG, "Conference from Intent :"+conference);
		return conference;
	}

	public void backButtonClicked(View v) {
		// TODO: Should i tell anything to controller?

		Intent i = new Intent(this, ConferenceCardView.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("conference", getConferenceFromIntent());
		i.putExtras(bundle);
		startActivity(i);

	}

	// Context Bar methods
	
	// TODO - need a method to check to see if this user is a regular user or
	// the moderator
	/*public void onLeaveClicked(View v) {
		this.conferenceController.leaveConference();
	}*/

	// Open the profile of this user
	public void onProfileClicked(View v) {
		conferenceController.showProfile();
	}

	// To add a person to the side chat
	// Need more specs on this
	// TODO - Ask ankit if this is the right method to go to
	public void onContextAddClicked(View v) {
		conferenceController.handleOnContextAddClicked();
		// this.conferenceController.addUser(new User("dummy", "dum dum",
		// R.drawable.example_picture_2), "Add to side chat");
	}

	/**
	 * When moderator presses End in main chat
	 * @param v
	 */
	public void onModeratorEndConference(View v) {
		conferenceController.handleEndClicked(UserManager.PRIMARY_USER);
		exitConference();
	}

	/**
	 * When the moderator presses Leave in main chat
	 * @param v
	 */
	public void onModeratorLeaveConference(View v) {
		conferenceController.handleLeaveClicked(MAIN_ROOM_INDEX, UserManager.PRIMARY_USER);
		conferenceController.setNewModerator();
		exitConference();
	}
	
	/**
	 * When moderator presses End in side chat
	 * @param v
	 */
	public void onModeratorEndChat(View v){
		// TODO how is this different?
		returnToPage(MAIN_ROOM_INDEX);
	}
	
	/**
	 * When moderator presses Leave in side chat
	 * @param v
	 */
	public void onModeratorLeaveChat(View v){
		// TODO how to get index?
		int roomIndex = LEFT_ROOM_INDEX;
		conferenceController.handleLeaveClicked(roomIndex, UserManager.PRIMARY_USER);
		returnToPage(MAIN_ROOM_INDEX);
	}
	
	/**
	 * When non-moderator presses Leave in side chat
	 * @param v
	 */
	public void onUserLeaveChat(View v){
		int roomIndex = RIGHT_ROOM_INDEX; // TODO how to tell index?
		conferenceController.handleLeaveClicked(roomIndex, UserManager.PRIMARY_USER);
		returnToPage(MAIN_ROOM_INDEX);
	}
	
	/**
	 * When the moderator presses Leave Conference on main chat
	 * @param v
	 */
	/*public void onLeaveConference(View v) {
		conferenceController.handleLeaveClicked(MAIN_ROOM_LAYOUT, UserManager.PRIMARY_USER);
		exitConference();
	} */

	// TODO - Not sure what this is supposed to do. Ask Design team
	public void onModeratorClicked(View v) {
		conferenceController.setNewModerator();
	}

	// When a user is removed from the conference
	public void onRemoveClicked(View v) {
		conferenceController.removeUser();
	}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	public void onPageSelected(int roomNumber) {
		Log.d(TAG, "Room Number selected :" + roomNumber);
		ConferenceRoomFragment fragment = (ConferenceRoomFragment) mPagerAdapter.getItem(roomNumber);
		
//		fragment.createUsers();
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
