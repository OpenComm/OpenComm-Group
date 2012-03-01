package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConferencePlannerView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.UserView;

public class ConferencePlannerController {

	private Conference openfireInvitation;
	// fields for our date widget
	Calendar endDate;
	Calendar startDate;
	Calendar stock = Calendar.getInstance();
	// fields for start/end times
	private int startMonth;
	private int startYear;
	private int startDay;
	private int startMinute;
	private int startHour;
	private int endMinute;
	private int endHour;
	// associated conferencePlannerView
	private ConferencePlannerView conferencePlannerView;
	// fields for addUsers
	CharSequence[] buddyList = { "CrystalQin", "ChrisLiu" };// hard-code for now
	// =ContactListController.buddyList; // list of the user's buddies in
	// their username form
	boolean[] buddySelection = { false, false };// hard-code for now
	// =ContactListController.buddySelection; // array of boolean for buddy
	private String username = ""; // the username of this account
	// selection
	private String occurance; // String description of how many times conference
								// should be repeated

	AlertDialog recurring;// Spinner hack for popups (spinner needs an initial
							// UI element on the layout to become the spinner
							// once clicked, we have a custom image icon

	static ArrayList<String> addedUsers=new ArrayList();
	// OnSetListeners activate when the user presses the set key after selecting
	// a time/date (thus, fields need to be updated)
	DatePickerDialog.OnDateSetListener endDay = new DatePickerDialog.OnDateSetListener() {
		@Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startDay = dayOfMonth;
			startMonth = monthOfYear + 1;
			startYear = year;

			// If start day is before current day, complain and allow user
			// reselect
			if (year < stock.get(Calendar.YEAR)
					|| (year == stock.get(Calendar.YEAR) && monthOfYear < stock
							.get(Calendar.MONTH))
					|| (year == stock.get(Calendar.YEAR)
							&& monthOfYear == stock.get(Calendar.MONTH) && dayOfMonth < stock
							.get(Calendar.DATE))) {
				Toast.makeText(
						conferencePlannerView,
						"You selected a start time that is before the current time. Please reselect an appropriate time.",
						Toast.LENGTH_LONG).show();
				handleDateButtonClicked();
			}

			endDate.set(Calendar.YEAR, year);
			endDate.set(Calendar.MONTH, monthOfYear);
			endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			Log.v("startTime", "day,month,year: " + startDay + "," + startMonth
					+ "," + startYear);
		}
	};

	TimePickerDialog.OnTimeSetListener end = new TimePickerDialog.OnTimeSetListener() {
		@Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			endMinute = minute;
			endHour = hourOfDay;
			endDate.set(Calendar.HOUR, hourOfDay);
			endDate.set(Calendar.MINUTE, minute);
		}
	};

	TimePickerDialog.OnTimeSetListener start = new TimePickerDialog.OnTimeSetListener() {
		@Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			startMinute = minute;
			startHour = hourOfDay;
			startDate.set(Calendar.HOUR, hourOfDay);
			startDate.set(Calendar.MINUTE, minute);
		}
	};

	public ConferencePlannerController(
			ConferencePlannerView conferencePlannerView) {
		endDate = Calendar.getInstance();
		startDate = Calendar.getInstance();
		this.conferencePlannerView = conferencePlannerView;

	}

	// TODO: All buttons that once clicked only pops up needs to be fixed so
	// that the white overlay is only applied while the uesr clicks
	public void handleDateButtonClicked() {
		Log.v("context check2", "getContext() is null?: "
				+ conferencePlannerView.getContext());

		conferencePlannerView.getDateButtonOverlay()
				.setVisibility(View.VISIBLE);
		new DatePickerDialog(conferencePlannerView, endDay,
				endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH),
				endDate.get(Calendar.DAY_OF_MONTH)).show();
		Log.v("Date?",
				endDate.get(Calendar.DATE) + "day and "
						+ (endDate.get(Calendar.MONTH) + 1) + "month and "
						+ endDate.get(Calendar.YEAR) + " year.");
		// conferencePlannerView.getDateButtonOverlay().setVisibility(View.INVISIBLE);
	}

	public void handleEndButtonClicked() {
		// testing if its updating properly.
		Log.v("Date?",
				endDate.get(Calendar.DATE) + "day and "
						+ (endDate.get(Calendar.MONTH) + 1) + "month and "
						+ endDate.get(Calendar.YEAR) + " year.");
		conferencePlannerView.getEndButtonOverlay().setVisibility(View.VISIBLE);
		new TimePickerDialog(conferencePlannerView, end,
				endDate.get(Calendar.HOUR), endDate.get(Calendar.MINUTE), true)
				.show();
		Log.v("end", "I clicked the end button!");

	}

	public void handleRecurringButtonClicked() {
		conferencePlannerView.getRecurringButtonOverlay().setVisibility(
				View.VISIBLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				conferencePlannerView);
		builder.setTitle(R.string.recurring_prompt);
		builder.setPositiveButton("Select",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
					// Do nothing, since it is being constantly updated in the
					// onClick() as user click around
					// Perhaps better to just have it find which here, but it
					// returned -1 for all
				});
		builder.setSingleChoiceItems(R.array.recurring_options, 0,
				new DialogInterface.OnClickListener() {

					// Location = where user clicked. 0 = once, 1=every
					// day,2=every week,3=every two weeks,4=every month 5= every
					// year
					@Override
                    public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							occurance = "Once";
						} else if (which == 1) {
							occurance = "EveryDay";
						} else if (which == 2) {
							occurance = "EveryWeek";
						} else if (which == 3) {
							occurance = "EveryTwoWeeks";
						} else if (which == 4) {
							occurance = "EveryMonth";
						} else {
							occurance = "EveryYear";
						}

						// Log.v("where?", "location of user click: " + which);
						// Debugging with toast, switch failed for some reason
						// Toast.makeText(conferencePlannerView,"You selected "
						// + occurance, Toast.LENGTH_SHORT).show();
					}

				});
		recurring = builder.create();
		recurring.show();

	}

	public void handlestartButtonClicked() {
		conferencePlannerView.getStartButtonOverlay().setVisibility(
				View.VISIBLE);
		new TimePickerDialog(conferencePlannerView, start,
				startDate.get(Calendar.HOUR), startDate.get(Calendar.MINUTE),
				true).show();

	}

	public void handleAttendeeButtonClicked() {
		conferencePlannerView.getAttendeeButtonOverlay().setVisibility(
				View.VISIBLE);
		Log.v("end", "I clicked the attendee button!");
		showContactList();
	}

	private void showContactList() {
		 final Context context = conferencePlannerView;
		 final  LinearLayout vs=(LinearLayout) conferencePlannerView.findViewById(R.id.userIcons);
		if (context == null)
			Log.v("ShowBUddyLIst", "NULL");
		Log.v("ContactListController", "showBuddyList() 1");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		Log.v("ContactListController", "showBuddyList() 2");

		class DialogSelectionClickHandler implements
				DialogInterface.OnMultiChoiceClickListener {
			@Override
			public void onClick(DialogInterface dialog, int clicked,
					boolean selected) {
				buddySelection[clicked] = selected;
			}
		}

		class DialogOkButtonClickHandler implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int clicked) {
				switch (clicked) {
				case DialogInterface.BUTTON_POSITIVE:
					for (int i = 0; i < buddySelection.length; i++) {
						if (buddySelection[i] && (!addedUsers.contains(buddyList[i]))){
					     ConferencePlannerController.addUserIcon((String)buddyList[i], context,vs);
					    }
				    }
					break;
				}
			}
		}

		class DialogCancelButtonClickHandler implements
				DialogInterface.OnClickListener {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}
		Log.v("ContactListController", "showBuddyList() 3");
		Log.v("ContactListController", "showBuddyList() 4");
		builder.setTitle("Buddylist")
				.setMultiChoiceItems(buddyList, buddySelection,
						new DialogSelectionClickHandler())
				.setPositiveButton("Ok", new DialogOkButtonClickHandler())
				.setNegativeButton("Cancel",
						new DialogCancelButtonClickHandler()).create();
		Log.v("ContactListController", "showBuddyList() 5");
		AlertDialog alert = builder.create();
		Log.v("ContactListController", "showBuddyList() 6");
		// MainApplication.screen.getActivity().displayEmptySpaceMenu(alert);
		alert.show();
		Log.v("ContactListController", "showBuddyList() 7");
	}
	private static void addUserIcon(String username, Context cpv, LinearLayout vs) {
		int marginX = 0; // marginX of the icon
		int marginY = 0;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80,80);
		lp.setMargins(0, 10, 20, 0);
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(200,100);
				
				User p = new User(username + "@jabber.org", username,
						R.drawable.question);
				
				UserView invited = new UserView(cpv, p,
						R.drawable.question, null, marginX, marginY);
				//invitedUsers.put(username, invited);
				ConferencePlannerController.drawIcon(username, invited, lp, l, vs,cpv);
				addedUsers.add(username);
				Log.v("ContactListController", username + " was invited");
				
			
	}

	

	
	
	private static void drawIcon(String name, UserView user, LinearLayout.LayoutParams lp,LinearLayout.LayoutParams l,
			LinearLayout vs, Context c){
		//String name = e.getKey();
		Bitmap uv =user.getImage();
		ImageView userI= new ImageView(c);
		userI.setImageBitmap(uv);
	
		LinearLayout every=new LinearLayout(c);
		every.addView(userI,lp);
		TextView userShow = new TextView(c);
		userShow.setText(name);
		userShow.setTextSize(20);
		userShow.setGravity(Gravity.CENTER_VERTICAL);
		every.addView(userShow,l);
		vs.addView(every);
	}

	// Crystal: for now, create conference makes the app goes into
	// MainApplication
	//Chris: Crreates a OpenfireInvitation object with all the fields we have. TODO: where does this go?

	public void handleCreateButtonsClicked() {
		conferencePlannerView.getCreateOverlay().setVisibility(View.VISIBLE);
		Intent i = new Intent(conferencePlannerView, MainApplication.class);
		i.putExtra(Network.KEY_USERNAME, DashboardController.username);
		i.setAction(Network.ACTION_LOGIN);

		//Make string array of invited users from buddylist and the boolean array telling if person is invited
		int inviteCounter=0;
		for (boolean b: buddySelection){
			if (b){inviteCounter++;}
		}
		String[] inviteList= new String[inviteCounter];
		int counter=0;
		inviteCounter=0;
		for (CharSequence s: buddyList){
			if (buddySelection[counter]==true){
				inviteList[inviteCounter] = s.toString();
			}
			counter++;
		}
		openfireInvitation = new Conference(startYear, startMonth, startDay, startHour, startMinute, endHour, endMinute,inviteList,username);
		conferencePlannerView.startActivity(i);
	}

	// Crystal: Cancel back to dashboard
	public void handleCancelClicked() {
		conferencePlannerView.getCanceleOverlay().setVisibility(View.VISIBLE);
		Intent i = new Intent(conferencePlannerView, DashboardView.class);
		conferencePlannerView.startActivity(i);
	}

	// public void handleAttendeeImageButtonClicked() {
	// conferencePlannerView.getAttendeeButtonOverlay().setVisibility(View.VISIBLE);
	// Log.v("end", "I clicked the attendee button!");
	// // TODO Auto-generated method stub something that brings it to last
	// screen (dashboard)
	//
}
