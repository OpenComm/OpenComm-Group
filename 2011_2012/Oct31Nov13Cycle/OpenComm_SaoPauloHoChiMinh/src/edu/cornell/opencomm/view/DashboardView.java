package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.LoginController;

public class DashboardView extends Activity {
	private static String LOG_TAG = "OC_DashboardView"; // for error checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	private DashboardController dashboardController;

	private View dashboardLayout = null;

	/*public DashboardView(LayoutInflater inflater) {
		this.inflater = inflater;
		initEventsAndProperties();
	}*/

	/*
	 * /** Called when an activity is first created
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		initEventsAndProperties();
		dashboardController = new DashboardController(this);
	} // end onCreate method

	private void initEventsAndProperties() {
		// create property dashboardLayout from inflater and store it as a
		// property
		if (inflater != null) {
			View dashboardViewFromInflater = inflater.inflate(
					R.layout.dashboard_layout, null);
			if (dashboardViewFromInflater != null) {
				this.dashboardLayout = dashboardViewFromInflater;
			}
		}

		initializeContactsButtonClickedEvent();
		initializeHistoryButtonClickedEvent();
		initializeAccountButtonClickedEvent();
		initializeConferenceButtonClickedEvent();
		dashboardController = new DashboardController(this);
	}
	
	public Button getContactsButton() {
		Button startContactsButton = null;
		if (dashboardLayout != null) {
			startContactsButton = (Button) dashboardLayout.findViewById(R.id.buttonContacts);
		}
		
		return startContactsButton;
	}

	
	public Button getHistoryButton() {
		Button startHistoryButton = null;
		if (dashboardLayout != null) {
			startHistoryButton = (Button) dashboardLayout.findViewById(R.id.buttonHistory);
		}

		return startHistoryButton;
	}
	
	public Button getAccountButton() {
		Button startAccountButton = null;
		if (dashboardLayout != null) {
			startAccountButton = (Button) dashboardLayout.findViewById(R.id.buttonAccount);
		}

		return startAccountButton;
	}
	
	public ImageButton getStartConferenceButton() {
		ImageButton startConferenceButton = null;
		//if (dashboardLayout != null) {
		startConferenceButton = (ImageButton) findViewById(R.id.buttonStartConference);
		//}

		return startConferenceButton;
	}
	
	public Button getStartConferenceTextButton(){
		Button startConferenceTextButton = null;
		if (dashboardLayout != null){
			startConferenceTextButton = (Button) findViewById(R.id.textViewConference);
		}
		
		return startConferenceTextButton;
	}
	
	private void initializeContactsButtonClickedEvent() {
		Button startContactsButton = getContactsButton();
		if (startContactsButton != null) {
			Log.d(LOG_TAG, "Initialize Contacts Button");
			//startContactsButton.setOnTouchListener(onStartConferenceButtonClickedListener);
		}
	}
	
	private void initializeHistoryButtonClickedEvent() {
		Button startHistoryButton = getHistoryButton();
		if (startHistoryButton != null) {
			Log.d(LOG_TAG, "Initialize History Button");
			//startHistoryButton.setOnTouchListener(onStartConferenceButtonClickedListener);
		}
	}
	
	private void initializeAccountButtonClickedEvent() {
		Button startAccountButton = getAccountButton();
		if (startAccountButton != null) {
			Log.d(LOG_TAG, "Initialize Account Button");
			//startAccountButton.setOnTouchListener(onStartConferenceButtonClickedListener);
		}
	}
	
	private void initializeConferenceButtonClickedEvent() {
		ImageButton startConferenceButton = getStartConferenceButton();
		if (startConferenceButton != null) {
			startConferenceButton.setOnClickListener(onStartConferenceButtonClickedListener);
		}
		
		Button startConferenceTextButton = getStartConferenceTextButton();
		if (startConferenceTextButton != null){
			startConferenceTextButton.setOnClickListener(onStartConferenceButtonClickedListener);
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	/*public PopupWindow getWindow() {
		return window;
	}*/

	public void setWindow(PopupWindow window) {
		this.window = window;
	}

	/*
	 * this method launches the confirmation layout on a popupwindiw, can be
	 * changed later to launch like a normal view
	 */
	public void launch() {
		if (inflater != null && dashboardLayout != null) {
			window = new PopupWindow(dashboardLayout, Values.screenW,
					Values.screenH, true);
			window.showAtLocation(dashboardLayout, 0, 1, 1);
			dashboardLayout.setOnClickListener(onClickListener);
		} else {
			Log.v(LOG_TAG,
					"Cannot launch dashboard view as inflater layout is null");
		}
	}
	
	public void onStart() {
		super.onStart();
	} // end onStart method


	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//dashboardController.handlePopupWindowClicked();
		}
	};

	private View.OnClickListener onStartConferenceButtonClickedListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dashboardController.handleStartConferenceButtonClicked();
		}
	};
}
