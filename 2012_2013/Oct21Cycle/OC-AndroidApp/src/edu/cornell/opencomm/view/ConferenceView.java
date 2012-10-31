

package edu.cornell.opencomm.view;

import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.model.ConferenceDataModel;
import android.app.Activity;
import android.os.Bundle;
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
	}
	/**
	 * To be invoked when occupant(s) location is changed in the 
	 * active chat space
	 */
	void refreshChatSpaceView(){
		
	}
	
	/**
	 * To be invoked when chat space is switched 
	 */
	void switchChatSpaceModel(){
		
	}
	
}
