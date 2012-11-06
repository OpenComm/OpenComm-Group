

package edu.cornell.opencomm.view;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.ConferenceDataModel;
//TODO: Remove this
@SuppressWarnings("unused")
public final class ConferenceView extends Activity {
	
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		init();
	}
	private void init(){
		//1. Bind to the conference model
		//2. instantiate the conference controller
		//3. register to listners
		//3.1 Nora : Register the onSwipe listners
		
	}
	/**
	 * To be invoked when occupant(s) location is changed in the 
	 * active chat space
	 */
	void refreshChatSpaceView(){
		
	}
	/**When the user swipes left
	 * @param v
	 */
	public void onLeftSwipe(View v){
		
	}
	/**When the user swipes right
	 * @param v
	 */
	public void onRightSwipe(View v){
		
	}
	public void onTap(){
		
	}
	
	private boolean isOnEmptySpace(Point p){
		return false;
	}
	/**
	 * To be invoked when chat space is switched 
	 */
	void switchChatSpaceModel(){
		
	}
	
}
