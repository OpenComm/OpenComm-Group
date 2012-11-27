package edu.cornell.opencomm.view;

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
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.ConferenceDataModel;

/**
 * 
 * @author Ankit Singh:as2536
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
		Conference confernce = getConferenceFromIntent();
	
		conferenceModel = new ConferenceDataModel(
				confernce.getConferenceTitle(), confernce.getStartDateAndTime()
						.toString(), confernce.getInviter().getUsername());
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
		conferenceFragments.add(new ConferenceRoomFragment(this, LEFT_ROOM_LAYOUT, conferenceModel.getRoomByTag(LEFT_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, MAIN_ROOM_LAYOUT, conferenceModel.getRoomByTag(MAIN_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, RIGHT_ROOM_LAYOUT, conferenceModel.getRoomByTag(RIGHT_ROOM_INDEX)));
		return conferenceFragments;
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
			// TODO- Check to see if the user is the moderator or not - display
			// respective bottom bars for moderator/user
			RelativeLayout bottom_bar = (RelativeLayout) screen
					.findViewById(R.id.bottom_bar_reg_user);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		} else {
			RelativeLayout action_bar = (RelativeLayout) screen
					.findViewById(R.id.actionbar_sidechat);
			// TODO- Check to see if the user is the moderator or not - display
			// respective bottom bars for moderator/user
			RelativeLayout bottom_bar = (RelativeLayout) screen
					.findViewById(R.id.bottom_bar_reg_user);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		}

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
	public void onLeaveClicked(View v) {
		this.conferenceController.leaveConference();
	}

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

	// Moderator Context Bar
	public void onEndClicked(View v) {
		conferenceController.handleEndClicked();
		// this.conferenceController.endConference("Not sure what to pass in",
		// new User("hello", "Dog", R.drawable.example_picture_1));
	}

	// When the moderator presses leave conference
	public void onModeratorLeft(View v) {
		conferenceController.setNewModerator();
	}

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
