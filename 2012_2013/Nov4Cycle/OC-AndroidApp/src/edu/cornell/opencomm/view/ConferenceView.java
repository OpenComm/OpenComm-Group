

package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.ConferenceDataModel;
//TODO: Remove this
@SuppressWarnings("unused")
public final class ConferenceView extends Activity {
	private boolean areActionBarsDisplayed = false;
	
	
	/**
	 * The conference data model
	 */
	private ConferenceDataModel conferenceModel;
	
	
	/**
	 * The conference controller
	 */
	private ConferenceController conferenceController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO temporary layout with real one (conference_layout) once xml errors are fixed
		setContentView(R.layout.conference_layout_temporary);
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
		int visibility = View.VISIBLE;
		if (areActionBarsDisplayed)
			visibility = View.INVISIBLE;
		areActionBarsDisplayed = !areActionBarsDisplayed;
		LinearLayout action_bar = (LinearLayout) screen.findViewById(R.id.action_bar);
		LinearLayout bottom_bar = (LinearLayout) screen.findViewById(R.id.bottom_bar);
		action_bar.setVisibility(visibility);
		bottom_bar.setVisibility(visibility); 
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
	
}
