package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Editable;
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
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConferencePlannerView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.NotificationView;
import edu.cornell.opencomm.view.UserView;

public class ConferencePlannerController {
	// Note: Right now, conferences has to start and end on the same day.

	private Conference openfireInvitation;
	// fields for our date widget
	Calendar endTime;
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
	private boolean endTimeSet = false; // True if an end time is set
	private boolean startTimeSet = false; //True if a start time is set
	protected boolean startDaySet = false;
	// associated conferencePlannerView
	private ConferencePlannerView conferencePlannerView;
	// fields for addUsers
	public CharSequence[] buddyList = { "CrystalQin", "ChrisLiu", "Third",
			"Forth" };// hard-code for now
	// =ContactListController.buddyList; // list of the user's buddies in
	// their username form
	public boolean[] buddySelection = { false, false, false, false };// hard-code
																		// for
																		// now
	// =ContactListController.buddySelection; // array of boolean for buddy
	private static String username = DashboardController.username.split("@")[0]; // the
																					// username
																					// of
																					// this
																					// account
	// selection
	private String occurance = "once"; // String description of how many times
										// conference
	// should be repeated

	AlertDialog recurring;// Spinner hack for popups (spinner needs an initial
							// UI element on the layout to become the spinner
							// once clicked, we have a custom image icon

	static ArrayList<String> addedUsers = new ArrayList<String>();
	String conferenceName;

	// OnSetListeners activate when the user presses the set key after selecting
	// a time/date (thus, fields need to be updated)
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
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

				NotificationView popup1 = new NotificationView(
						conferencePlannerView.getContext());
				// Should use a string xml
				popup1.launch("You entered a date in the past. Please Retry.",
						"RED", "WHITE", true);

				// Toast.makeText(
				// conferencePlannerView.getContext(),
				// "You selected a start time that is before the current time. Please reselect an appropriate time.",
				// Toast.LENGTH_LONG).show();
				handleDateButtonClicked();
			} else {
				conferencePlannerView.getDateBox().setText(
						" " + startMonth + "/" + startDay + "/" + startYear);
				startDaySet=true;
			}

			// One day conferences, initialize to 0:00 for start/end times.
			endTime.set(Calendar.YEAR, year);
			endTime.set(Calendar.MONTH, monthOfYear);
			endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			startDate.set(Calendar.YEAR, year);
			startDate.set(Calendar.MONTH, monthOfYear);
			startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
	};

	TimePickerDialog.OnTimeSetListener end = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			endTime.set(Calendar.HOUR, hourOfDay);
			endTime.set(Calendar.MINUTE, minute);

			if (hourOfDay < startHour || hourOfDay == startHour
					&& minute < startMinute) {
				NotificationView wrongTime = new NotificationView(
						conferencePlannerView.getContext());
				// Should use a string xml
				wrongTime
						.launch("You entered an end time before your start time. Please Retry.",
								"RED", "WHITE", true);
				handleEndTimeButtonClicked();
			} else {
				endMinute = minute;
				endHour = hourOfDay;
				String tempMinute = Integer.toString(minute);// Add a leading
																// zero for
																// aestetic
																// reason
				if (minute < 10) {
					tempMinute = "0" + tempMinute;
				}
				;
				conferencePlannerView.getEndBox().setText(
						" " + hourOfDay + ": " + tempMinute);
				endTimeSet = true;
			}

		}
	};

	TimePickerDialog.OnTimeSetListener start = new TimePickerDialog.OnTimeSetListener() {

		

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			startDate.set(Calendar.HOUR, hourOfDay);
			startDate.set(Calendar.MINUTE, minute);

			if ((endHour < hourOfDay || (endHour == hourOfDay && endMinute < minute))
					&& endTimeSet) {
				NotificationView wrongTime = new NotificationView(
						conferencePlannerView.getContext());
				// Should use a string xml
				wrongTime
						.launch("You had entered an end time before this start time. Please Retry.",
								"RED", "WHITE", true);
				handleStartTimeButtonClicked();
			} else {
				startMinute = minute;
				startHour = hourOfDay;
				String tempMinute = Integer.toString(minute);// Add a leading
																// zero for
																// aestetic
																// reason
				if (minute < 10) {
					tempMinute = "0" + tempMinute;
				}
				;
				conferencePlannerView.getStartBox().setText(
						" " + hourOfDay + ": " + tempMinute);
				startTimeSet=true;
			}
		}
	};

	public ConferencePlannerController(
			ConferencePlannerView conferencePlannerView) {
		Roster xmppRoster = LoginController.xmppService.getXMPPConnection()
				.getRoster();
		Collection<RosterEntry> entryCollection = xmppRoster.getEntries();
		Iterator<RosterEntry> entryItr = entryCollection.iterator();
		buddyList = new CharSequence[entryCollection.size()];
		int i = 0;
		while (entryItr.hasNext()) {
			String nickname = entryItr.next().getUser().split("@")[0];
			buddyList[i++] = (CharSequence) nickname;

		}

		Log.v("Cyt", Integer.toString(entryCollection.size()));

		endTime = Calendar.getInstance();
		startDate = Calendar.getInstance();
		this.conferencePlannerView = conferencePlannerView;
	}

	// Crystal: alternative constructor for debugging conference listing view
	public ConferencePlannerController(ConferencePlannerView conference,
			Conference c) {

		this(conference);
		if (c == null)
			Log.v("c", "buddylist is null");
		this.buddyList = c.getContactList().toArray(new CharSequence[0]);

		boolean[] hack = new boolean[buddyList.length];
		for (int i = 0; i < hack.length; i++) {
			hack[i] = true;
		}
		this.buddySelection = hack;
		this.openfireInvitation = c;
	}

	public Conference getOpenfireInvitation() {
		return openfireInvitation;
	}

	public void setOpenfireInvitation(Conference openfireInvitation) {
		this.openfireInvitation = openfireInvitation;
	}

	// TODO: All buttons that once clicked only pops up needs to be fixed so
	// that the white overlay is only applied while the user clicks
	public void handleDateButtonClicked() {
		Log.v("context check2", "getContext() is null?: "
				+ conferencePlannerView.getContext());

		conferencePlannerView.getDateButtonOverlay()
				.setVisibility(View.VISIBLE);

		// Makes overlay invis again after 500ms
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				conferencePlannerView.getDateButtonOverlay().setVisibility(
						View.INVISIBLE);
			}
		}, 500);

		new DatePickerDialog(conferencePlannerView.getContext(), date,
				endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH),
				endTime.get(Calendar.DAY_OF_MONTH)).show();
		Log.v("Date?",
				endTime.get(Calendar.DATE) + "day and "
						+ (endTime.get(Calendar.MONTH) + 1) + "month and "
						+ endTime.get(Calendar.YEAR) + " year.");
		// conferencePlannerView.getDateButtonOverlay().setVisibility(View.INVISIBLE);
	}

	public void handleEndTimeButtonClicked() {
		// Log.v("Date?",
		// endDate.get(Calendar.DATE) + "day and "
		// + (endDate.get(Calendar.MONTH) + 1) + "month and "
		// + endDate.get(Calendar.YEAR) + " year.");
		conferencePlannerView.getEndButtonOverlay().setVisibility(View.VISIBLE);

		// Makes overlay invis again after 500ms
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				conferencePlannerView.getEndButtonOverlay().setVisibility(
						View.INVISIBLE);
			}
		}, 500);

		new TimePickerDialog(conferencePlannerView.getContext(), end,
				endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE), true)
				.show();

	}

	public void handleRecurringButtonClicked() {
		conferencePlannerView.getRecurringButtonOverlay().setVisibility(
				View.VISIBLE);
		// Makes overlay invis again after 500ms
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				conferencePlannerView.getRecurringButtonOverlay()
						.setVisibility(View.INVISIBLE);
			}
		}, 500);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				conferencePlannerView.getContext());
		builder.setTitle(R.string.recurring_prompt);
		builder.setPositiveButton("Select",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (conferencePlannerView.getRecurringButtonOverlay() != null) {
							conferencePlannerView.getRecurringButtonOverlay()
									.setVisibility(View.INVISIBLE);
						}
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

	public void handleStartTimeButtonClicked() {
		conferencePlannerView.getStartButtonOverlay().setVisibility(
				View.VISIBLE);
		// Makes overlay invis again after 500ms
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				conferencePlannerView.getStartButtonOverlay().setVisibility(
						View.INVISIBLE);
			}
		}, 500);

		new TimePickerDialog(conferencePlannerView.getContext(), start,
				startDate.get(Calendar.HOUR), startDate.get(Calendar.MINUTE),
				true).show();

	}

	public void handleAttendeeButtonClicked() {
		conferencePlannerView.getAttendeeButtonOverlay().setVisibility(
				View.VISIBLE);
		// Makes overlay invis again after 500ms
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				conferencePlannerView.getAttendeeButtonOverlay().setVisibility(
						View.INVISIBLE);
			}
		}, 500);

		Log.v("end", "I clicked the attendee button!");
		showContactList();
	}

	private void showContactList() {
		final Context context = conferencePlannerView.getContext();
		// final LinearLayout vs=(LinearLayout)
		// conferencePlannerView.findViewById(R.id.userIcons);
		final LinearLayout vs = (LinearLayout) conferencePlannerView.getView()
				.findViewById(R.id.userIcons);

		// Once contact list is shown, white overlay disappears
		// conferencePlannerView.getAttendeeButtonOverlay().setVisibility(
		// View.INVISIBLE);

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
						if (buddySelection[i]
								&& (!addedUsers.contains(buddyList[i]))) {
							ConferencePlannerController.addUserIcon(
									(String) buddyList[i], context, vs);
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

	public static void addUserIcon(String username, Context cpv, LinearLayout vs) {
		int marginX = 0;
		int marginY = 0;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80, 80);
		lp.setMargins(0, 10, 20, 0);
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(200, 100);

		User p = new User(username + "@" + Network.DEFAULT_HOST, username,
				R.drawable.question);

		UserView invited = new UserView(cpv, p, R.drawable.question, null,
				marginX, marginY);
		// invitedUsers.put(username, invited);
		ConferencePlannerController.drawIcon(username, invited, lp, l, vs, cpv);
		addedUsers.add(username);
		Log.v("ContactListController", username + " was invited");

	}

	private static void drawIcon(String name, UserView user,
			LinearLayout.LayoutParams lp, LinearLayout.LayoutParams l,
			LinearLayout vs, Context c) {
		// String name = e.getKey();
		Bitmap uv = user.getImage();
		ImageView userI = new ImageView(c);
		userI.setImageBitmap(uv);

		LinearLayout every = new LinearLayout(c);
		every.addView(userI, lp);
		TextView userShow = new TextView(c);
		userShow.setText(name);
		userShow.setTextSize(20);
		userShow.setGravity(Gravity.CENTER_VERTICAL);
		every.addView(userShow, l);
		vs.addView(every);
	}

	// Crystal: for now, create conference makes the app goes into
	// MainApplication
	// Chris: Crreates a OpenfireInvitation object with all the fields we have.
	// TODO: where does this go?

	public void handleCreateButtonsClicked() {
		conferencePlannerView.getCreateOverlay().setVisibility(View.VISIBLE);

		SendConfToServer();
		boolean inTheFuture = startTimeSet && endTimeSet && startDaySet && conferenceName!=null;
		// Immediate conference, start MainApplication screen. (This is when not
		// all time fields are defined
		if (!inTheFuture) {
			Intent i = new Intent(conferencePlannerView.getContext(),
					MainApplication.class);
			i.putExtra(Network.KEY_USERNAME, DashboardController.username);
			i.setAction(Network.ACTION_LOGIN);
			conferencePlannerView.getContext().startActivity(i);
		}

		// This is when the conference is in the future. Just dismiss popup,
		// since conference info is alreafy sent to the server
		else {
			conferencePlannerView.getWindow().dismiss();
		}

	}

	// Helper method to send conference information to server, with intent i.

	private void SendConfToServer() {

		// Make string array of invited users from buddylist and the boolean
		// array telling if person is invited
		int inviteCounter = 0;
		for (boolean b : buddySelection) {
			if (b) {
				inviteCounter++;
			}
		}
		String[] inviteList = new String[inviteCounter > 10 ? inviteCounter
				: 10];
		int counter = 0;
		inviteCounter = 0;
		for (CharSequence s : buddyList) {
			if (buddySelection[counter] == true) {
				inviteList[inviteCounter] = s.toString();
			}
			counter++;
		}
		conferenceName = conferencePlannerView.getNameBox().getText().toString();
		// If not pre-populated, populate w/ user inputs
		// TODO: make the UI accomodate the room name
		if (openfireInvitation == null) {
			openfireInvitation = new Conference(null, new Date(startYear - 1900,
					startMonth, startDay, startHour, startMinute),
					new Date(startYear - 1900, startMonth, startDay, endHour,
							endMinute), username, conferenceName, new ArrayList<String>(
							Arrays.asList(inviteList)));
			LoginController.xmppService.getSchedulingService().pushConference(
					openfireInvitation.getName(),
					openfireInvitation.getHostName(),
					openfireInvitation.getDateString(),
					openfireInvitation.getStartLong(),
					openfireInvitation.getEndLong(),
					openfireInvitation.getRecurring(),
					openfireInvitation.getContactsAsArray());
			Log.v("date", String.valueOf(openfireInvitation.getStartLong()));
			// openfireInvitation.setRoom(room);

			openfireInvitation.setPlannerView(conferencePlannerView);

		} else {
			// go back to conference list view.
			// TO-DO: updating the conference in the backend database.
			Log.v("Conference Planner Controller", "conference value changed");
			openfireInvitation.setStartDate(startYear - 1900, startMonth,
					startDay, startHour, startMinute);
			openfireInvitation.setStartDate(startYear - 1900, startMonth,
					startDay, startHour, startMinute);
			openfireInvitation.setName(conferenceName);

			openfireInvitation.setEndDate(startYear, startMonth, startDay,
					endHour, endMinute);

			openfireInvitation.setContactList((ArrayList<String>) Arrays
					.asList(inviteList));
			openfireInvitation.setInviter(username);
			LoginController.xmppService.getSchedulingService()
					.updateConference(openfireInvitation);
		
		}
	}

	public String getRoom() {
		return conferenceName;
	}

	public void setRoom(String room) {
		this.conferenceName = room;
	}

	// for debugging
	public void setAll(Conference c) {
		this.startDay = c.getStartDay();
		startMonth = c.getStartMonth();
		this.startYear = c.getStartYear();
		this.startHour = c.getStartHour();
		startMinute = c.getStartMinute();
		endHour = c.getEndHour();
		endMinute = c.getEndMinute();
		username = c.getInviter();
		conferenceName = c.getName();

	}

	// Crystal: Cancel back to dashboard
	public void handleCancelClicked() {
		// Currently - make a new dashboard. since dismiss cause a weird effect
		// :(
		conferencePlannerView.getCanceleOverlay().setVisibility(View.VISIBLE);
		// Intent i = new Intent(conferencePlannerView.getContext(),
		// DashboardView.class);
		// conferencePlannerView.getContext().startActivity(i);
		conferencePlannerView.getWindow().dismiss();
	}

}
