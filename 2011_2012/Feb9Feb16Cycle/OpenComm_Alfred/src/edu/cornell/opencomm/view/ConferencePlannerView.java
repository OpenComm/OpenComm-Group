package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.ConferencePlannerController;

public class ConferencePlannerView extends Activity {

	// Create context according to our view xml
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContext(this);
		Log.d("Test", "Are you calling me?");
		setContentView(R.layout.conference_layout);

		// initEventsAndProperties();
		initEventsAndProperties();

	}

	// private static String LOG_TAG = "OC_ConfirmationView"; // for error
	// checking
	private Context context;
	private LayoutInflater inflater;
	private PopupWindow window = null;
	// private PopupWindow window = null;
	private ConferencePlannerController conferencePlannerController = new ConferencePlannerController(
			this);
	private View conferencePlannerLayout = null; // Initialized to null, made
													// with constructor, but not
													// used at the moment (used
													// with inflater)

	public ConferencePlannerView() {
		initEventsAndProperties();
		setContext(this);
	}

	// Don't think I would ever inflate this
	// public ConferencePlannerView(LayoutInflater inflater) {
	// this.inflater = inflater;
	// initEventsAndProperties();
	// Log.v("onCreate", "Oncreate called!");
	// setContext(this);
	//
	// }

	private void initEventsAndProperties() {

		// create property conferencePlannernLayout from inflalter and store it
		// as a
		// property

		// Initialize all my buttons
		initializeDateButton();
		initializeStartButton();
		initializeEndButton();
		initializeRecurringButton();
		initializeAttendeesButton();
		initializeCreateButtons();
		initializeCancelButtons();

		// Log.v("nullcheck", "getContext() null? " + getContext().toString());
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
		ImageView attendeeImageButton = getAttendeeImageButton();
		if (attendeeNameButton != null) {
			attendeeNameButton.setOnClickListener(onAttendeesClickListener);
		}
		if (attendeeImageButton != null) {
			attendeeImageButton.setOnClickListener(onAttendeesClickListener);
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

	// Getters for all the buttons and overlays associated with the buttons
	private Button getCancelButton() {
		Button cancelButton = null;
		if (getContext() != null) {
			cancelButton = (Button) ((Activity) getContext())
					.findViewById(R.id.buttonCancel);
		}

		return cancelButton;
	}

	private ImageButton getImageCancelButton() {
		ImageButton imageCancelButton = null;
		if (getContext() != null) {
			imageCancelButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.imageCancel);
		}

		return imageCancelButton;
	}

	private Button getCreateButton() {
		Button createButton = null;
		if (getContext() != null) {
			createButton = (Button) ((Activity) getContext())
					.findViewById(R.id.buttonCreate);
		}

		return createButton;
	}

	private ImageButton getImageCreateButton() {
		ImageButton imageCreateButton = null;
		if (getContext() != null) {
			imageCreateButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.imageCreate);
		}

		return imageCreateButton;
	}

	private ImageButton getDateButton() {
		ImageButton dateButton = null;
		if (getContext() != null) {
			dateButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.calenderButton);
		}

		return dateButton;
	}

	private ImageButton getStartButton() {
		ImageButton startButton = null;
		if (getContext() != null) {
			startButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.startTimeButton);
		}

		return startButton;
	}

	private ImageButton getEndButton() {
		ImageButton endButton = null;
		if (getContext() != null) {
			endButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.endTimeButton);
		}

		return endButton;
	}

	private ImageButton getRecurringButton() {
		ImageButton recurringButton = null;
		if (getContext() != null) {
			recurringButton = (ImageButton) ((Activity) getContext())
					.findViewById(R.id.recurringButton);
		}

		return recurringButton;
	}

	private ImageView getAttendeeImageButton() {
		ImageView attendeeImageButton = null;
		if (getContext() != null) {
			attendeeImageButton = (ImageView) ((Activity) getContext())
					.findViewById(R.id.attendeesButton);
		}

		return attendeeImageButton;
	}

	private Button getAttendeeNameButton() {
		Button attendeeNameButton = null;
		if (getContext() != null) {
			attendeeNameButton = (Button) ((Activity) getContext())
					.findViewById(R.id.attendeeNameButton);
		}

		return attendeeNameButton;
	}

	// Public getters: Overlays for controller and context/inflater for general
	// purpose use
	public ImageView getAttendeeButtonOverlay() {
		ImageView attendeeButtonOverlay = null;
		if (getContext() != null) {
			attendeeButtonOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.attendeeButtonOverlay);
		}
		return attendeeButtonOverlay;
	}

	public View getStartButtonOverlay() {
		ImageView startButtonOverlay = null;
		if (getContext() != null) {
			startButtonOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.startButtonOverlay);
		}
		return startButtonOverlay;
	}

	public View getRecurringButtonOverlay() {
		ImageView recurringButtonOverlay = null;
		if (getContext() != null) {
			recurringButtonOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.recurringButtonOverlay);
		}
		return recurringButtonOverlay;
	}

	public View getEndButtonOverlay() {
		ImageView endButtonOverlay = null;
		if (getContext() != null) {
			endButtonOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.endButtonOverlay);
		}
		return endButtonOverlay;
	}

	public View getDateButtonOverlay() {
		ImageView dateButtonOverlay = null;
		if (getContext() != null) {
			dateButtonOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.dateButtonOverlay);
		}
		return dateButtonOverlay;
	}

	public View getCreateOverlay() {
		ImageView createOverlay = null;
		if (getContext() != null) {
			createOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.createOverlay);
		}
		return createOverlay;
	}

	public View getCanceleOverlay() {
		ImageView cancelOverlay = null;
		if (getContext() != null) {
			cancelOverlay = (ImageView) ((Activity) getContext())
					.findViewById(R.id.cancelOverlay);
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

	/*
	 * this method launches the conferencePlanner layout on a popupwindow, can
	 * be changed later to launch like a normal view, currently not used since
	 * we are making this a view as of right now
	 */
	// public void launch() {
	//
	// if (inflater != null && getContext() != null) {
	// window = new PopupWindow(getContext(), Values.screenW,
	// Values.screenH, true);
	// window.showAtLocation(getContext(), 0, 1, 1);
	// } else {
	// Log.v("123",
	// "Cannot launch confirmation view as inflater/confirmation layout is nul");
	// }
	// }

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
			conferencePlannerController.handlestartButtonClicked();
		}
	};

	private View.OnClickListener onEndButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			conferencePlannerController.handleEndButtonClicked();
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

}
