package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.controller.ConferenceController_v2;

public final class ConferenceView_v2 extends Activity 
{
	//if true: debug mode
	private static final boolean D = true;
	
	private static String TAG = ConferenceView_v2.class.getName();
	//TODO: ADD a controller
	private ConferenceController_v2 controller;
	
	private static ConferenceView_v2 _instance = null;
	
	// Audio variables
	private PowerManager pm;
	private WakeLock mWakeLock;

	/*
	 *controls on this view
	*/
	public TextView txtv_ConfTitle;
	
	public static ConferenceView_v2 getInstance() {
		if (_instance == null) {
			_instance = new ConferenceView_v2();
		}
		return _instance;
	}
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		if(D)
			Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_v2);
		//instantiate the handlers
		txtv_ConfTitle = (TextView) findViewById(R.id.confernecev2_title);
		
		
		// Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        // Applying font
        txtv_ConfTitle.setTypeface(tf);
		
		controller = new ConferenceController_v2();
		
		// Audio integration
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyTag");
		mWakeLock.acquire();
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
		controller.HandleAddPerson("oc4testorg");
	}
	
	
	//triggered when overflow button was pressed (on confernece_v2)
	public void overflowButtonClicked(View v)
	{
		controller.HandleOverflow();
	}
	
	
	//triggered when back button was pressed (on confernece_v2)
	public void backButtonClicked(View v)
	{
		controller.HandleBackButton();
	}
	
	
	//change the title of the conference ("CONFERENCE NAME" by default)
	public void renameConference(String title)
	{
		controller.setTitle(title);
	}
}
