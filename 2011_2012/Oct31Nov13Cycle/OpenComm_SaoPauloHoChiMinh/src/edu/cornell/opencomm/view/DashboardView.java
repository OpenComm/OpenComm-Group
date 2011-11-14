package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
	private DashboardController dashboardController = new DashboardController(
			this);
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
		// create property dashboardLayout from infalter and store it as a
		// property
		if (inflater != null) {
			View dashboardViewFromInflater = inflater.inflate(
					R.layout.dashboard_layout, null);
			if (dashboardViewFromInflater != null) {
				this.dashboardLayout = dashboardViewFromInflater;
			}
		}
		initializeConferenceButtonClickedEvent();

	}

	private void initializeConferenceButtonClickedEvent() {
		ImageButton startConferenceButton = getStartConferenceButton();
		if (startConferenceButton != null) {
			startConferenceButton
					.setOnTouchListener(onStartConferenceButtonClickedListener);
		}
	}

	public ImageButton getStartConferenceButton() {
		ImageButton startConferenceButton = null;
		if (dashboardLayout != null) {
			startConferenceButton = (ImageButton) dashboardLayout
					.findViewById(R.id.buttonStartConference);
		}

		return startConferenceButton;
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

	private View.OnTouchListener onStartConferenceButtonClickedListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			dashboardController.handleStartConferenceButtonClicked();
			return true;
		}
	};
}
