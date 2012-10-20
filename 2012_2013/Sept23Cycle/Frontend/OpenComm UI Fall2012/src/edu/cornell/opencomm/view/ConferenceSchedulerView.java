package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Calendar;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.DatePickerController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceSchedulerAdapter;
import edu.cornell.opencomm.model.User;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConferenceSchedulerView extends Activity {
//	 /** Called when the activity is first created. */
//	private ConferenceSchedulerAdapter adapter_happeningNow;
//	private ConferenceSchedulerAdapter adapter_upcoming;
//	private ArrayList<Conference> conferences;
//	private ExpandableListView expandList_happeningNow;
//	private ExpandableListView expandList_upcoming;
//	private Calendar currentTime;
//	
//	 public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.conference_scheduling_layout);
//	    currentTime = Calendar.getInstance();
//	    retrieveAndDisplayConferences();
//	    adjustLayoutLookAndFunctionality();
//	 }
//	 
//	 public void retrieveAndDisplayConferences(){
//		 conferences = createExampleConferences();
//		 
//		 // Create list of conferences happening now
//		 expandList_happeningNow = (ExpandableListView) findViewById(R.id.conferences_list_happening_now);
//		 ArrayList<Conference> conferences_happeningNow = getConferencesHappeningNow(conferences); 
//		 if (conferences_happeningNow.size()>0){
//			 RelativeLayout happening_now_separator_bar = (RelativeLayout) findViewById(R.id.conference_separator_happening_now);
//			 happening_now_separator_bar.setVisibility(View.VISIBLE);
//		 }
//		 adapter_happeningNow = new ConferenceSchedulerAdapter(ConferenceSchedulerView.this, conferences_happeningNow, true);
//		 expandList_happeningNow.setAdapter(adapter_happeningNow);
//		 
//		 // Create list of upcoming conferences
//		 expandList_upcoming = (ExpandableListView) findViewById(R.id.conferences_list_upcoming);
//		 ArrayList<Conference> conferences_upcoming = getUpcomingConferences(conferences); 
//		 if (conferences_upcoming.size()>0){
//			 RelativeLayout upcoming_separator_bar = (RelativeLayout) findViewById(R.id.conference_separator_upcoming);
//			 upcoming_separator_bar.setVisibility(View.VISIBLE);
//		 }
//		 adapter_upcoming = new ConferenceSchedulerAdapter(ConferenceSchedulerView.this, conferences_upcoming, false);
//		 expandList_upcoming.setAdapter(adapter_upcoming);
//	 }
//	 
//	 public ArrayList<Conference> createExampleConferences(){
//		/* User user1 = new User("Risa", "Naka");
//		 User user2 = new User("Makoto", "Bentz");
//		 User user3 = new User("Jason", "Xu");
//		 User user4 = new User("Nathan", "Chun");
//		 User user5 = new User("Najla", "Elmachtoub"); */
//		 ArrayList<Conference> conferences = new ArrayList<Conference>();
//		 Calendar startTime = (Calendar)currentTime.clone();
//		 startTime.add(Calendar.HOUR, -2);
//		 Calendar endTime = (Calendar)currentTime.clone();
//		 endTime.add(Calendar.HOUR, 2);
//		/* ArrayList<User> conference1_attendees = new ArrayList<User>();
//		 conference1_attendees.add(user1);
//		 conference1_attendees.add(user2);
//		 conference1_attendees.add(user3); */
//		 Conference conference1 = new Conference( "Morning Meeting",  
//				 					  			  startTime, 
//				 					  			  endTime, 
//				 					  			  "Every Thursday", 
//				 					  			  "Remember to have your documents ready", 
//				 					  			   null); 
//		 startTime = (Calendar)currentTime.clone();
//		 startTime.add(Calendar.HOUR, 2);
//		 endTime = (Calendar)currentTime.clone();
//		 endTime.add(Calendar.HOUR, 4); 
//		/* ArrayList<User> conference2_attendees = new ArrayList<User>();
//		 conference2_attendees.add(user1);
//		 conference2_attendees.add(user2);
//		 conference2_attendees.add(user3);
//		 conference2_attendees.add(user4);
//		 conference2_attendees.add(user5); */
//		 Conference conference2 = new Conference( "Evening Meeting", 
//	  			  								  startTime, 
//	  			  								  endTime, 
//	  			  								  "Every Thursday", 
//	  			  								  "Remember to have your documents ready", 
//	  			  								  null); 
//		 startTime = (Calendar)currentTime.clone();
//		 endTime = (Calendar)currentTime.clone();
//		/* ArrayList<User> conference3_attendees = new ArrayList<User>();
//		 conference3_attendees.add(user1); */
//		 Conference conference3 = new Conference( "Right-this-second Meeting", 
//					  startTime, 
//					  endTime, 
//					  "Only right now!", 
//					  "Remember to have your documents ready", 
//					  null ); 
//		 conferences.add(conference1);
//		 conferences.add(conference2);
//		 conferences.add(conference3);
//		 return conferences;
//	 }
//	 
//	 public ArrayList<Conference> getConferencesHappeningNow(ArrayList<Conference> conferences){
//		 ArrayList<Conference> conferencesHappeningNow = new ArrayList<Conference>();
//		 for (Conference conference : conferences){
//			 if ((currentTime.equals(conference.getStartDateAndTime()) || currentTime.after(conference.getStartDateAndTime())) && (currentTime.equals(conference.getEndDateAndTime()) || currentTime.before(conference.getEndDateAndTime()))){
//				 conferencesHappeningNow.add(conference);
//			 }
//		 }
//		 return conferencesHappeningNow;
//	 }
//	 
//	 public ArrayList<Conference> getUpcomingConferences(ArrayList<Conference> conferences){
//		 ArrayList<Conference> conferencesUpcoming = new ArrayList<Conference>();
//		 for (Conference conference : conferences){
//			 if (currentTime.before(conference.getStartDateAndTime()) && currentTime.before(conference.getEndDateAndTime())){
//				 conferencesUpcoming.add(conference);
//			 }
//		 }
//		 return conferencesUpcoming;  
//	 }
//	 
//	 /** Adjust the displayed layout and button functionality */
//	 public void adjustLayoutLookAndFunctionality(){
//		 initializeAddConferenceButton();
//		 initializeSelectDateButton();
//	 }
//	 
//	 public void initializeAddConferenceButton(){
//		final ImageView addConferenceButton = (ImageView) findViewById(R.id.add_conference_button);
//		addConferenceButton.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				switch (arg1.getAction()) {
//		        case MotionEvent.ACTION_DOWN: {
//		            // TODO Change color of button? Ask Design team
//		            break;
//		        }
//		        case MotionEvent.ACTION_UP:{
//		            // TODO Start the Create Conference Activity 
//		            //Intent intent = new Intent(ContactsView.this, CreateConferenceView.class);
//		        	//startActivity(intent);
//
//		        	// For now make a toast
//		            Context context = getApplicationContext();
//		            CharSequence text = "Go to Create Conference Page!";
//		            int duration = Toast.LENGTH_SHORT;
//		            Toast toast = Toast.makeText(context, text, duration);
//		            toast.show();
//		        }
//		        }
//		        return true;
//		    }
//		}); 
//	} 
//	 
//	 public void initializeSelectDateButton(){
//		 final TextView select_date_button = (TextView) findViewById(R.id.date);
//		 select_date_button.setOnTouchListener(new OnTouchListener(){
//			public boolean onTouch(View v, MotionEvent event) {
//				showDatePickerDialog(select_date_button);
//				return true;
//			}
//		 });
//	 }
//	 
//	 public void showDatePickerDialog(View v) {
//		/*DialogFragment newFragment = new DatePickerController();
//		newFragment.show(getSupportFragmentManager(), "datePicker"); */
//	 }
}
