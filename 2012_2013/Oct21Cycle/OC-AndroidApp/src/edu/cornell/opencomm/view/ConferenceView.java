

package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.ConferenceDataModel;
import android.app.Activity;
import android.os.Bundle;

public final class ConferenceView extends Activity {
	
	/**
	 * The conference data model
	 */
	private ConferenceDataModel conferenceModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
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
