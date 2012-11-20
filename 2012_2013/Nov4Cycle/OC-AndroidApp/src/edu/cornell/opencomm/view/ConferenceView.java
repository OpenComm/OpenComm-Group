

package edu.cornell.opencomm.view;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
//TODO: Remove this
@SuppressWarnings("unused")
//TODO - Integrate this with Chat Space View/View Group
public final class ConferenceView extends FragmentActivity implements ViewPager.OnPageChangeListener {
	
	private boolean areActionBarsDisplayed = false;
	
	private static final boolean D = true; 
	private static int roomCount = 3;
	private ConferenceRoomView[] conferenceRooms;
	private static int mainRoomlayout = R.layout.confernec_main_room;
	private static int sideLeftRoomLayout = R.layout.conference_leftside_room; // change layout TODO
	private static int sideRightRoomLayout = R.layout.conference_rightside_room; // change layout TODO
	private static int mainRoomIndex=1, leftRoomIndex=0, rightRoomIndex=2;
	
	private Conference conference;
	private Context context;
	
	private static String TAG = ConferenceView.class.getName();
	/**
	 * The conference data model
	 */
	private ConferenceDataModel conferenceModel;
	
	private Vibrator vibe; 

	/**
	 * The conference controller
	 */
	private ConferenceController conferenceController;

	private ConferencePageAdapter mPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext(); 
		conference = (Conference)getIntent().getSerializableExtra("conference");
		// TODO temporary layout with real one (conference_layout) once xml errors are fixed
		setContentView(R.layout.conference_layout_temporary);	
		conferenceController = new ConferenceController(this, ConferenceView.this); 
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
		init(); 
	}
	
	/**
	 * Initialize the conference view
	 */
	private void init(){
		List<Fragment> fragments = createRooms(sideLeftRoomLayout, mainRoomlayout, sideRightRoomLayout);
		
		this.mPagerAdapter  = new ConferencePageAdapter(super.getSupportFragmentManager(), fragments);
	
		ViewPager pager = (ViewPager)super.findViewById(R.id.threepanelpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(1);
			
	}
	
	public List<Fragment> createRooms(int leftLayout, int mainLayout, int rightLayout){
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(instantiateRoom(leftLayout));
		fragments.add(instantiateRoom(mainLayout));
		fragments.add(instantiateRoom(rightLayout));
		return fragments;
	}
	
	private Fragment instantiateRoom(int id){
		ConferenceRoomFragment room = (ConferenceRoomFragment) Fragment.instantiate(this, ConferenceRoomFragment.class.getName());
		room.layoutId = id;
		//room.room = new ConferenceRoom();
		return room;
	}


	/**
	 * To be invoked when occupant(s) location is changed in the 
	 * active chat space
	 */
	void refreshChatSpaceView(){

	}

	private boolean isOnEmptySpace(Point p){
		return false;
	}
	/**
	 * To be invoked when chat space is switched 
	 */
	void switchChatSpaceModel(){

	}

	/** When user clicks an empty space on the screen
	 * toggle between showing both the action and bottom bar
	 * @param screen
	 */
	public void clickedEmptySpace(View screen){
		String view = screen.getContentDescription().toString();  
		int visibility = View.VISIBLE;
		if (areActionBarsDisplayed)
			visibility = View.INVISIBLE;
		areActionBarsDisplayed = !areActionBarsDisplayed;
		if (view.equals("mainChat")){
			RelativeLayout action_bar = (RelativeLayout) screen.findViewById(R.id.action_bar);
			//TODO- Check to see if the user is the moderator or not - display respective bottom bars for moderator/user
			RelativeLayout bottom_bar = (RelativeLayout) screen.findViewById(R.id.bottom_bar_reg_user);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		}
		else{
			RelativeLayout action_bar = (RelativeLayout) screen.findViewById(R.id.actionbar_sidechat);
			//TODO- Check to see if the user is the moderator or not - display respective bottom bars for moderator/user
			RelativeLayout bottom_bar = (RelativeLayout) screen.findViewById(R.id.bottom_bar_reg_user);
			action_bar.setVisibility(visibility);
			bottom_bar.setVisibility(visibility);
		}
		 
	}

	public void addPersonClicked(View v){
		this.conferenceController.addPersonClicked(); 
	}

	//Should make the other person's phone vibrate
	public void pingClicked(View v){
		vibe.vibrate(2000); //making it vibrate for 2000 ms 
	}

	private boolean p = true; 
	//Should display a red bar and when its clicked again should unmute 
	public void muteClicked(View v){
		Button mute = (Button)v; 
		if (p){
			mute.setBackgroundResource(R.drawable.mute);
			p = false; 
		}
		else {
			mute.setBackgroundResource(R.drawable.unmute); 
			p= true; 
		}
		this.conferenceController.muteClicked();

	}

	public void overflowButtonClicked(View v){
		this.conferenceController.handleOverflow(); 
	}


	public void backButtonClicked(View v){
		this.conferenceController.handleBackButtonClicked(); 
	}

	//Context Bar methods
	//TODO - need a method to check to see if this user is a regular user or the moderator
	public void onLeaveClicked(View v){
		this.conferenceController.leaveConference(); 
	}


	//Open the profile of this user
	public void onProfileClicked(View v){
		this.conferenceController.showProfile(); 
	}

	//To add a person to the side chat 
	//Need more specs on this
	//TODO - Ask ankit if this is the right method to go to
	public void onContextAddClicked(View v){
		this.conferenceController.handleOnContextAddClicked(); 
		//this.conferenceController.addUser(new User("dummy", "dum dum", R.drawable.example_picture_2), "Add to side chat"); 
	}

	//Moderator Context Bar
	public void onEndClicked(View v){
		this.conferenceController.handleEndClicked(); 
		//this.conferenceController.endConference("Not sure what to pass in", new User("hello", "Dog", R.drawable.example_picture_1)); 
	}

	//When the moderator presses leave conference
	public void onModeratorLeft(View v){
		this.conferenceController.setNewModerator(); 
	}

	//TODO - Not sure what this is supposed to do. Ask Design team
	public void onModeratorClicked(View v){
		this.conferenceController.setNewModerator(); 
	}

	//When a user is removed from the conference
	public void onRemoveClicked(View v){
		this.conferenceController.removeUser(); 
	}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	public void onPageSelected(int roomNumber) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Room Number selected :"+roomNumber);
	}
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
