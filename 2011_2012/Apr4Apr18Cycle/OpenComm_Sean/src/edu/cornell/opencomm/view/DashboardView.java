package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.ContextTracker;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.GoToConferencePopupController;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.network.Network;

public class DashboardView extends Activity {

    // Debugging
    private static String TAG = "View.DashboardView";
    private static boolean D = true;

    // Layout
    private Context context;
    private LayoutInflater inflater = null;
    private PopupWindow window = null;
    private DashboardController dashboardController;
    private static DashboardView dashboardInstance;

    private View dashboardLayout;
    private ImageView dashboardOverlay;
    private Typeface font;

    private PopupNotificationView pnv;
	private static boolean popupGo;//If true: 
	
	public static void setPopupGo(boolean popupGo){
		DashboardView.popupGo=popupGo;
	}
	
	public static DashboardView getDashboardInstance(){
		return dashboardInstance;
	}
    /*
     * /** Called when an activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardInstance=this;
        ContextTracker.setContext(this);
        font = Typeface.createFromAsset(getAssets(), Values.font);
        setContentView(R.layout.dashboard_layout);
        this.dashboardOverlay = (ImageView) findViewById(R.id.dashboardOverlay);
        inflater = this.getLayoutInflater();
        initEventsAndProperties();
        dashboardController = new DashboardController(this);
    } // end onCreate method

    private void initEventsAndProperties() {
        // create property dashboardLayout from inflater and store it as a
        // property
        if (inflater != null) {
            if (D) Log.d(TAG, "initEaP: inflater");
            View dashboardViewFromInflater = inflater.inflate(
                    R.layout.dashboard_layout, null);
            if (D) Log.d(TAG, "initEaP: dashboardInflator -- null?: " + (dashboardViewFromInflater == null));
            if (dashboardViewFromInflater != null) {
                this.dashboardLayout = dashboardViewFromInflater;
                if (D) Log.d(TAG, "initEaP: dashboardLayout -- null?: " + (this.dashboardLayout == null));
            }
        }

        initializeContactsButtonClickedEvent();
        initializeConferencesButtonClickedEvent();
        initializeAccountButtonClickedEvent();
        initializeStartConferenceButtonClickedEvent();
        dashboardController = new DashboardController(this);

    }

    public Button getContactsButton() {
        Button startContactsButton = null;
        if (dashboardLayout != null) {
            startContactsButton = (Button) findViewById(R.id.buttonContacts);
            startContactsButton.setTypeface(font);
        }

        return startContactsButton;
    }


    public Button getConferencesButton() {
        Button startConferencesButton = null;
        if (dashboardLayout != null) {
            startConferencesButton = (Button) findViewById(R.id.buttonConferences);
            startConferencesButton.setTypeface(font);
        }

        return startConferencesButton;
    }

    public Button getAccountButton() {
        Button startAccountButton = null;
        if (dashboardLayout != null) {
            startAccountButton = (Button) findViewById(R.id.buttonAccount);
            startAccountButton.setTypeface(font);
        }

        return startAccountButton;
    }

    public ImageView getAccountButtonOverlay() {
    	ImageView overlay = null;
    	if(dashboardLayout != null) {
    		overlay = (ImageView) findViewById(R.id.buttonAccountOverlay);
    	}
    	return overlay;
    }

    public ImageButton getStartConferenceButton() {
        ImageButton startConferenceButton = null;
        startConferenceButton = (ImageButton) findViewById(R.id.buttonStartConference);

        return startConferenceButton;
    }

    public Button getStartConferenceTextButton(){
        Button startConferenceTextButton = null;
        if (this.dashboardLayout != null){
            startConferenceTextButton = (Button) findViewById(R.id.textViewConference);
            startConferenceTextButton.setTypeface(font);
        }
        if (D) Log.d(TAG, "dashboardLayout -- null?: " + (this.dashboardLayout == null));
        if (D) Log.d(TAG, "confTextView -- null?: " + (startConferenceTextButton == null));
        return startConferenceTextButton;
    }

    public ImageView getDashboardOverlay() {
        return this.dashboardOverlay;
    }

    private void initializeContactsButtonClickedEvent() {
        Button startContactsButton = getContactsButton();
        if (startContactsButton != null) {
        	if (D) Log.d(TAG, "Initialize Contacts Button");
            //startContactsButton.setOnTouchListener(onStartConferenceButtonClickedListener);
        }
    }

    private void initializeConferencesButtonClickedEvent() {
        Button startConferencesButton = getConferencesButton();
        if (startConferencesButton != null) {
        	if (D) Log.d(TAG, "Initialize Conferences Button");
            startConferencesButton.setOnClickListener(onConferencesClickListener);
        }
    }

    private void initializeAccountButtonClickedEvent() {
        Button startAccountButton = getAccountButton();
        if (startAccountButton != null) {
            if (D) Log.d(TAG, "Initialize Account Button");
            startAccountButton.setOnClickListener(onAccountButtonClickedListener);
        }
    }

    private void initializeStartConferenceButtonClickedEvent() {
        ImageButton startConferenceButton = getStartConferenceButton();
        if (startConferenceButton != null) {
            startConferenceButton.setOnClickListener(onStartConferenceButtonClickedListener);
        }

        Button startConferenceTextButton = getStartConferenceTextButton();
        if (startConferenceTextButton != null){
            if (D) Log.d(TAG, "initConfButtonClickedEvent: setting onClick for conference text");
            startConferenceTextButton.setOnClickListener(onStartConferenceButtonClickedListener);
        }
    }
    public void logoutButtonClicked(View v){
	  	Log.v(TAG, "logout button clicked");
	  	LoginController.xmppService.disconnect();
	  	Intent myIntent = new Intent();
		myIntent.setClass(this.getApplication(),
				LoginView.class);
		this.startActivity(myIntent);
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
            window.showAtLocation(dashboardLayout, 0, 0, 0);
            dashboardLayout.setOnClickListener(onClickListener);
        } else {
            Log.v(TAG,
                    "Cannot launch dashboard view as inflater layout is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //BACKEND: PLEASE CHANGE THIS TO CHECK WHETHER, AND WHAT, CONFERENCE IS SCHEDULED
        if (popupGo) {
        	popupGo=false;
	    	final String[] args = new String[1];
	    	args[0] = getIntent().getStringExtra(Network.KEY_USERNAME);
	    	pnv = new PopupNotificationView(this, args, "conference", "one of your conferences has started", "click to enter", Values.confirmation);
	    	pnv.setController(new GoToConferencePopupController(pnv));
	        dashboardLayout.post(new Runnable() {
				@Override
                public void run() {
			       	pnv.createPopupWindow();
				}

	        });
        }
    } // end onStart method

    @Override
    public void onPause() {
//    	if(pnv!=null){
//    	pnv.getPopup().dismiss();}
    	super.onPause();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if(pnv!=null){
    	pnv.getPopup().dismiss();}
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //dashboardController.handlePopupWindowClicked();
        }
    };

    private View.OnClickListener onAccountButtonClickedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        	Log.v(TAG, "listener recieved account button clicked");
            dashboardController.handleAccountButtonClicked();
        }
    };

    private View.OnClickListener onStartConferenceButtonClickedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        	Log.v(TAG, "listener recieved start conference button clicked");
            dashboardController.handleStartConferenceButtonClicked();
        }
    };

    private View.OnClickListener onConferencesClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        	Log.v(TAG, "listener recieved start conference button clicked");
            dashboardController.handleConferencesButtonClicked();
        }
    };

}
