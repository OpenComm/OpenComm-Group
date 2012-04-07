package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConferencePlannerController;


public class ConferencePlannerView{
	
	DisplayMetrics dm = new DisplayMetrics();
	int height;
	int width;

	
    public ConferencePlannerView(LayoutInflater inflater, Context context) {
    	setContext(context);
    	
    	((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    	height = dm.heightPixels;
    	width = dm.widthPixels;
    	
        this.inflater = inflater;
        this.conferencePlannerController =  new ConferencePlannerController(
                this);
        initEventsAndProperties();

    } 

	 private static String LOG_TAG = "Conference_Planner"; // for error
	// checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	// private PopupWindow window = null;
	private ConferencePlannerController conferencePlannerController=null;//Initialzed to null atm, called in onCreate()
	private View conferencePlannerLayout = null; // Initialized to null, made
													// with constructor, but not
													// used at the moment (used
													// with inflater)

	private void initEventsAndProperties() {

		// create property conferencePlannernLayout from inflalter and store it
		// as a
		// property
		
	    if (inflater != null) {
            View invitationLayoutFromInflater = inflater.inflate(
                    R.layout.conference_layout, null);
            if (invitationLayoutFromInflater != null) {
                this.conferencePlannerLayout= invitationLayoutFromInflater;
            }
        }

		// Initialize all my buttons
		initializeDateButton();
		initializeStartButton();
		initializeEndButton();
		initializeRecurringButton();
		initializeAttendeesButton();
		initializeCreateButtons();
		initializeCancelButtons();
		initializeNameBoxes();
		
	}

	private void initializeDateButton() {
		ImageButton dateButton = getDateButton();
		if (dateButton != null) {
			dateButton.setOnClickListener(onDateButtonClickListener);
		}
	}

	private void initializeStartButton() {
		ImageButton dateButton = getStartButton();
		if (dateButton != null) {
			dateButton.setOnClickListener(onStartButtonClickListener);
		}
	}

	private void initializeEndButton() {
		ImageButton dateButton = getEndButton();
		if (dateButton != null) {
			dateButton.setOnClickListener(onEndButtonClickListener);
		}
	}

	private void initializeRecurringButton() {
		ImageButton dateButton = getRecurringButton();
		if (dateButton != null) {
			dateButton.setOnClickListener(onRecurringButtonClickListener);
		}
	}

	private void initializeAttendeesButton() {
		Button attendeeNameButton = getAttendeeNameButton();
		if (attendeeNameButton != null) {
			Log.v("Buttons","name button clicker set");
			attendeeNameButton.setOnClickListener(onAttendeesClickListener);
		}


	}

	private void initializeCreateButtons() {
		ImageButton imageCreateButton = getImageCreateButton();
		Button createButton = getCreateButton();
		if (imageCreateButton != null) {
			imageCreateButton.setOnClickListener(onCreateConfClickListener);
		}
		if (createButton != null) {
			createButton.setOnClickListener(onCreateConfClickListener);
		}
	}

	private void initializeCancelButtons() {

		ImageButton imageCancelButton = getImageCancelButton();
		Button cancelButton = getCancelButton();
		if (imageCancelButton != null) {
			imageCancelButton.setOnClickListener(onCancelClickListener);
		}
		if (cancelButton != null) {
			cancelButton.setOnClickListener(onCancelClickListener);
		}
	}
	
	private void initializeNameBoxes(){
		EditText dateBox = getDateBox();
		dateBox.setFocusable(false);
		EditText startBox = getStartBox();
		startBox.setFocusable(false);
		EditText endBox = getEndBox();
		endBox.setFocusable(false);
	}

	public EditText getDateBox() {
		EditText dateBox = (EditText)(conferencePlannerLayout.findViewById(R.id.dateBox));
		return dateBox;
	}

	public EditText getStartBox() {
		EditText startBox = (EditText)(conferencePlannerLayout.findViewById(R.id.startBox));
		return startBox;
	}

	public EditText getEndBox() {
		EditText endBox = (EditText)(conferencePlannerLayout.findViewById(R.id.endBox));
		return endBox;
	}

	// Getters for all the buttons and overlays associated with the buttons
	private Button getCancelButton() {
		Button cancelButton = null;
		if (getContext() != null) {
			cancelButton = (Button) (conferencePlannerLayout
					.findViewById(R.id.buttonCancel));
		}

		return cancelButton;
	}

	private ImageButton getImageCancelButton() {
		ImageButton imageCancelButton = null;
		if (getContext() != null) {
			imageCancelButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.imageCancel));
		}

		return imageCancelButton;
	}

	private Button getCreateButton() {
		Button createButton = null;
		if (getContext() != null) {
			createButton = (Button) (conferencePlannerLayout
					.findViewById(R.id.buttonCreate));
		}

		return createButton;
	}

	private ImageButton getImageCreateButton() {
		ImageButton imageCreateButton = null;
		if (getContext() != null) {
			imageCreateButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.imageCreate));
		}

		return imageCreateButton;
	}

	private ImageButton getDateButton() {
		ImageButton dateButton = null;
		if (getContext() != null) {
			dateButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.calenderButton));
		}

		return dateButton;
	}

	private ImageButton getStartButton() {
		ImageButton startButton = null;
		if (getContext() != null) {
			startButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.startTimeButton));
		}

		return startButton;
	}

	private ImageButton getEndButton() {
		ImageButton endButton = null;
		if (getContext() != null) {
			endButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.endTimeButton));
		}

		return endButton;
	}

	private ImageButton getRecurringButton() {
		ImageButton recurringButton = null;
		if (getContext() != null) {
			recurringButton = (ImageButton) (conferencePlannerLayout
					.findViewById(R.id.recurringButton));
		}

		return recurringButton;
	}

	public Button getAttendeeNameButton() {
		Button attendeeNameButton = null;
		if (getContext() != null) {
			attendeeNameButton = (Button) (conferencePlannerLayout
					.findViewById(R.id.attendeeNameButton));
		}

		return attendeeNameButton;
	}

	// Public getters: Overlays for controller and context/inflater for general
	// purpose use
	public ImageView getAttendeeButtonOverlay() {
		ImageView attendeeButtonOverlay = null;
		if (getContext() != null) {
			attendeeButtonOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.attendeeButtonOverlay));
		}
		return attendeeButtonOverlay;
	}

	public View getStartButtonOverlay() {
		ImageView startButtonOverlay = null;
		if (getContext() != null) {
			startButtonOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.startButtonOverlay));
		}
		return startButtonOverlay;
	}

	public View getRecurringButtonOverlay() {
		ImageView recurringButtonOverlay = null;
		if (getContext() != null) {
			recurringButtonOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.recurringButtonOverlay));
		}
		return recurringButtonOverlay;
	}

	public View getEndButtonOverlay() {
		ImageView endButtonOverlay = null;
		if (getContext() != null) {
			endButtonOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.endButtonOverlay));
		}
		return endButtonOverlay;
	}

	public View getDateButtonOverlay() {
		ImageView dateButtonOverlay = null;
		if (getContext() != null) {
			dateButtonOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.dateButtonOverlay));
		}
		return dateButtonOverlay;
	}

	public View getCreateOverlay() {
		ImageView createOverlay = null;
		if (getContext() != null) {
			createOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.createOverlay));
		}
		return createOverlay;
	}

	public View getCanceleOverlay() {
		ImageView cancelOverlay = null;
		if (getContext() != null) {
			cancelOverlay = (ImageView) (conferencePlannerLayout
					.findViewById(R.id.cancelOverlay));
		}
		return cancelOverlay;
	}

	// Public getters for contexts/imageviews
	public ConferencePlannerController getConferencePlannerController() {
		return conferencePlannerController;
	}

	public Context getContext() {
		// return context;
		// Log.v("contextCheck", "context is null? "+ context.toString());
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



	// Click listeners for all the things that could be clicked in this view.

	private View.OnClickListener onDateButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleDateButtonClicked();
		}
	};

	private View.OnClickListener onStartButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleStartTimeButtonClicked();
		}
	};

	private View.OnClickListener onEndButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleEndTimeButtonClicked();
		}
	};

	private View.OnClickListener onRecurringButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleRecurringButtonClicked();
		}
	};

	private View.OnClickListener onAttendeesClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleAttendeeButtonClicked();
		}
	};

	private View.OnClickListener onCreateConfClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleCreateButtonsClicked();
		}
	};
	private View.OnClickListener onCancelClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleCancelClicked();
		}
	};

	

	/*
	 * this method launches the conferencePlanner layout on a popupwindow, can
	 * be changed later to launch like a normal view, but this method prevents this class from extending activity
	 */
	public void launch() {
        if (inflater != null && conferencePlannerLayout != null) {
            window = new PopupWindow(conferencePlannerLayout, width,
                    height, true);
            window.showAtLocation(conferencePlannerLayout, 0, 0, 0);
        } else {
            Log.v(LOG_TAG,
                    "Cannot launch conferencePlanner view as inflater/confirmation layout is nul");
        }
    } // end launch
	
	/**Gets popup window */
	public PopupWindow getWindow() {
		return window;
	}
	
	/**Gets the view thats popped up */
	public View getView(){
		return conferencePlannerLayout;
		
	}
}
