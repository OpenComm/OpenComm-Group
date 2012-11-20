

package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
//TODO: Remove this
@SuppressWarnings("unused")
//TODO - Integrate this with Chat Space View/View Group
public final class ConferenceView extends Activity {
	private boolean areActionBarsDisplayed = false;


	/**
	 * The conference data model
	 */
	private ConferenceDataModel conferenceModel;
	
	private Vibrator vibe; 

	/**
	 * The conference controller
	 */
	private ConferenceController conferenceController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = this.getApplicationContext(); 
		// TODO temporary layout with real one (conference_layout) once xml errors are fixed
		setContentView(R.layout.conference_layout_temporary);	
		conferenceController = new ConferenceController(this, ConferenceView.this); 
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
		init();

	}
	private void init(){
		//1. Bind to the conference model
		//2. instantiate the conference controller
		//3. register to listners

		//3.1 Nora : Register the onSwipe listeners
		ConferencePageAdapter adapter = new ConferencePageAdapter(this);
		ViewPager myPager = (ViewPager) findViewById(R.id.threepanelpager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(1);
	}

	private static final boolean D = true; 

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

	/** When the user swipes right
	 * Display the left sidechat
	 */
	public void onLeftToRightSwipe(View v) {
		/* TODO Ankit I don't think I need this... I can just call
		 * switchChatSpaceModel from ConferencePageAdapter... unless you 
		 * want to split it up somehow.
		 */
	}
	/** When the user swipes left 
	 * Display the right sidechat
	 */
	public void onRightToLeftSwipe(View v) {
		// TODO Ankit... same comment as onLeftToRightSwipe
	}

	//Action bar methods 
	//TODO- Should pop up the add contact to conference page 
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

}
