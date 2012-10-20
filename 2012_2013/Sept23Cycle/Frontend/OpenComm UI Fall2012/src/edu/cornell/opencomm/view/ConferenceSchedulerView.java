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
import android.content.Intent;
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
	 /** Called when the activity is first created. */
	private ConferenceSchedulerAdapter adapter_happeningNow;
	private ConferenceSchedulerAdapter adapter_upcoming;
	private ArrayList<Conference> conferences;
	private ExpandableListView expandList_happeningNow;
	private ExpandableListView expandList_upcoming;
	private Calendar currentTime;
	
	// TEMPORARY VARIABLES
	ArrayList<User> users;
	
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.conference_scheduling_layout);
	    currentTime = Calendar.getInstance();
	    retrieveAndDisplayConferences();
	  //  adjustLayoutLookAndFunctionality();
	 }
	 
	 public void retrieveAndDisplayConferences(){
		 users = createExampleUsers();
		 conferences = createExampleConferences();
		// openContactCardActivity(conferences.get(2));
	 }
	 
	 public ArrayList<User> createExampleUsers(){
		 ArrayList<User> users = new ArrayList<User>();
		 users.add(new User("naka_shaka_laka", "Risa Naka", R.drawable.example_picture_1));
		 users.add(new User("noratheexplora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		 users.add(new User("makomania", "Makoto Bentz", R.drawable.example_picture_3));
	//	 users.add(new User("graeme_craka", "Graeme Bailey", R.drawable.example_picture_1));
	//	 users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
	//	 users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
		 return users;
	 }
	 
	 public ArrayList<Conference> createExampleConferences(){
		   ArrayList<Conference> conferences = new ArrayList<Conference>();
		   // Conference - Invited
		    Calendar startTime = (Calendar)currentTime.clone();
			startTime.add(Calendar.HOUR, -1);
			Calendar endTime = (Calendar)currentTime.clone();
			endTime.add(Calendar.HOUR, 3); 
		    conferences.add( new Conference( "Invited",
		    								 "Man, we're gonna have suche a rockin' Christmas party. So cool. Did I mention it's at my place, Joseph Gordon-Levitt's?",
		    								 startTime,
		    								 endTime,
		    								 "Every fuckinggg' year",
		    								 users.get(0),
		    								 users 
		    							   ) ); 
		    // Conference - Accepted + Happening Now
		    startTime = (Calendar)currentTime.clone();
			startTime.add(Calendar.HOUR, -1);
			endTime = (Calendar)currentTime.clone();
			endTime.add(Calendar.HOUR, 3); 
			ArrayList<User> conference2_attendees = new ArrayList<User>();
			conference2_attendees.add(users.get(1));
			conference2_attendees.add(users.get(2));
			//conference2_attendees.add(users.get(3));
		    Conference conference2 = new Conference( "Happening Now",
		    										"OH HAAAAAAAAAAAAAAAAAAAAAAAAAI",
		    										startTime,
		    										endTime,
		    										"NOWWWW",
		    										users.get(1),
		    										conference2_attendees 
		    						 	 		  );
		    conference2.acceptInvite();
		    conferences.add(conference2);
		    // Conference - Accepted + Upcoming
		    startTime = (Calendar)currentTime.clone();
			startTime.add(Calendar.HOUR, 3);
			endTime = (Calendar)currentTime.clone();
			endTime.add(Calendar.HOUR, 4); 
			ArrayList<User> conference3_attendees = new ArrayList<User>();
			conference2_attendees.add(users.get(2));
		    Conference conference3 = new Conference( "UPCOMING",
		    										"OMG it's Year 9000",
		    										startTime,
		    										endTime,
		    										"OMG IT'S OVER 9-",
		    										users.get(2),
		    										conference3_attendees 
		    						 	 		  );
		    conference3.acceptInvite();
		    conferences.add(conference3);
		    
		 return conferences;
	 }
	 
	 public ArrayList<Conference> getConferencesHappeningNow(ArrayList<Conference> conferences){
		 ArrayList<Conference> conferencesHappeningNow = new ArrayList<Conference>();
		 for (Conference conference : conferences){
			 if ((currentTime.equals(conference.getStartDateAndTime()) || currentTime.after(conference.getStartDateAndTime())) && (currentTime.equals(conference.getEndDateAndTime()) || currentTime.before(conference.getEndDateAndTime()))){
				 conferencesHappeningNow.add(conference);
			 }
		 }
		 return conferencesHappeningNow;
	 }
	 
	 public ArrayList<Conference> getUpcomingConferences(ArrayList<Conference> conferences){
		 ArrayList<Conference> conferencesUpcoming = new ArrayList<Conference>();
		 for (Conference conference : conferences){
			 if (currentTime.before(conference.getStartDateAndTime()) && currentTime.before(conference.getEndDateAndTime())){
				 conferencesUpcoming.add(conference);
			 }
		 }
		 return conferencesUpcoming;  
	 }
	 
	 /** Adjust the displayed layout and button functionality */
	 public void adjustLayoutLookAndFunctionality(){
		 initializeAddConferenceButton();
		 initializeSelectDateButton();
	 }
	 
	 public void initializeAddConferenceButton(){
	/*	final ImageView addConferenceButton = (ImageView) findViewById(R.id.add_conference_button);
		addConferenceButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
		        case MotionEvent.ACTION_DOWN: {
		            // TODO Change color of button? Ask Design team
		            break;
		        }
		        case MotionEvent.ACTION_UP:{
		            // TODO Start the Create Conference Activity 
		            //Intent intent = new Intent(ContactsView.this, CreateConferenceView.class);
		        	//startActivity(intent);

		        	// For now make a toast
		            Context context = getApplicationContext();
		            CharSequence text = "Go to Create Conference Page!";
		            int duration = Toast.LENGTH_SHORT;
		            Toast toast = Toast.makeText(context, text, duration);
		            toast.show();
		        }
		        }
		        return true;
		    }
		});  */
	} 
	 
	 public void initializeSelectDateButton(){
	/*	 final TextView select_date_button = (TextView) findViewById(R.id.date);
		 select_date_button.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
				showDatePickerDialog(select_date_button);
				return true;
			}
		 }); */
	 }
	 
	 public void showDatePickerDialog(View v) {
		/*DialogFragment newFragment = new DatePickerController();
		newFragment.show(getSupportFragmentManager(), "datePicker"); */
	 }
	
	
    public void openContactCardActivity(Conference conference){
    	Intent i = new Intent(this, ConferenceCardView.class);
    	i.putExtra("com.cornell.opencomm.model.Conference", conference);
    	startActivity(i);
    }
}
