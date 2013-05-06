package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.SearchService;
import edu.cornell.opencomm.model.User;

/**
 * 
 * @author Spandana Govindgari [frontend], Ankit Singh[frontend],Nora
 *         NQ[frontend]
 * 
 */
public final class ConferenceView extends FragmentActivity implements
		ConferenceConstants, ViewPager.OnPageChangeListener {

	private static String TAG = ConferenceView.class.getName();

	/**
	 * The conference data model
	 */
	private Conference conferenceModel;
	// private Conference_Dummy conferenceModel;

	/**
	 * The conference controller
	 */
	private ConferenceController conferenceController;

	/**
	 * The conference pager adaptor
	 */
	private ConferencePageAdapter mPagerAdapter;

	public Context context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */

	private static ConferenceView _instance = null;

	private WakeLock mWakeLock;
	private View roomLayout;

	/*
	 * controls on this view
	 */
	//public TextView conference_title;

	public static ConferenceView getInstance() {
		if (_instance == null) {
			_instance = new ConferenceView();
		}
		return _instance;
	}
	/*
	 * TODO: Set conference title to the current conference name (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		// Bind the conference view/activity to the layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_layout);
		// instantiate the handlers  
		//conference_title = (TextView) findViewById(R.id.conference_Title);
		roomLayout = (ViewGroup) findViewById(R.layout.conference_v2);
		Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		conferenceController = ConferenceController.getInstance(this);
		context = this;
		conferenceModel = conferenceController.getRoom();  
		initPager();
	}

	private void initPager() {
		List<Fragment> fragments = createFragments();
		mPagerAdapter = new ConferencePageAdapter(
				super.getSupportFragmentManager(), fragments);
		NonSwipeablePager pager = (NonSwipeablePager) super.findViewById(R.id.threepanelpager);
		pager.setAdapter(this.mPagerAdapter);
		//pager.setOnPageChangeListener(this);
	}

	private List<Fragment> conferenceFragments = new Vector<Fragment>();
	
	private List<Fragment> createFragments() {
		conferenceFragments.add(new ConferenceRoomFragment(this,
				MAIN_ROOM_LAYOUT, conferenceModel));
		return conferenceFragments;
	}

	/**
	 * Remove the corresponding UserView for each given ConferenceUser and add
	 * to the ConferenceRoomFragment
	 * 
	 * @param users
	 * @param roomView
	 */
	public void removeUserViewsFromConference(ConferenceRoomFragment roomView,
			ArrayList<UserView> userViews) {
		for (UserView userView : userViews) {
			roomView.removeUserView(userView);
		}
	}

	/**
	 * Exit the conference and return to the conference card page for this
	 * conference
	 */
	public void exitConference() {
		Intent i = new Intent(this, DashboardView.class);
		onStop();
		startActivity(i);
	}

	/**
	 * Switch the view back to the page with the given index
	 * 
	 * @param roomIndex
	 */
	public void returnToPage(int roomIndex) {
		ViewPager pager = (ViewPager) super.findViewById(R.id.threepanelpager);
		pager.invalidate();
	}

	/* [TODO]: check to see if this should be onPause() */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	// triggered when add person button was pressed (on confernece_v2)
	public void addPersonClicked(View v) {
		// TODO: get username of person to be added (hardcoded for now)
		Log.v(TAG, "trying to add oc6testorg@opencomm");
		ConferenceController.getInstance(this).HandleAddPerson(
				"oc6testorg@opencomm");
		User user = SearchService.getUser("oc6testorg"); 
		ConferenceRoomFragment roomView = (ConferenceRoomFragment) conferenceFragments.get(0); 
		conferenceModel = conferenceController.getRoom(); 
		roomView.addUser(user, conferenceModel); 
		v.invalidate(); 
	}
	
	// triggered when leave button was pressed (on confernece_v2)
	public void leaveButtonClicked(View v) {
			conferenceController.HandleLeave();
	}

	// triggered when setting button was pressed (on confernece_v2)
	public void settingButtonClicked(View v) {
		conferenceController.HandleSetting();
	}
	// triggered when back button was pressed (on confernece_v2)
	public void backButtonClicked(View v) {
		//conferenceController.HandleBackButton();
		Intent account = new Intent(this, DashboardView.class);
		this.startActivity(account);
	}

//	// change the title of the conference ("CONFERENCE NAME" by default)
//	public void renameConference(String title) {
//		conferenceController.setTitle(title);
//	}

	// Context Bar methods

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	int currentPageNumber = 1;

	@Override
	public void onPageSelected(int roomNumber) {
		Log.d(TAG, "Room Number selected :" + roomNumber);
		currentPageNumber = roomNumber;
		mPagerAdapter.getItem(roomNumber);

		// fragment.createUsers();
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onStop() {
		conferenceModel = null;
		conferenceController = null;
		mPagerAdapter = null;
		super.onStop();
	}
}
