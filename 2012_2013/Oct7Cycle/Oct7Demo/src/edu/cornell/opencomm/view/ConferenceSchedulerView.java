package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceSchedulerAdapter;
import edu.cornell.opencomm.model.User;

public class ConferenceSchedulerView extends Activity {
	/** Called when the activity is first created. */

	private ArrayList<Conference> conferences;	
	private Calendar currentTime;
	private ConferenceController controller; 
	private ListView happeningNow;
	private ListView upcoming;
	private ListView invited;


	// TEMPORARY VARIABLES
	ArrayList<User> users;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_scheduling_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.layout.conference_scheduling_layout));
		currentTime = Calendar.getInstance();
		controller = new ConferenceController(ConferenceSchedulerView.this, this);
		retrieveAndDisplayConferences();

	}


	//TODO
	//1.Ask the design team about invited section
	//2. Need a field to check if you created the conference or if someone else did and invited you
	public void retrieveAndDisplayConferences(){
		users = createExampleUsers();
		conferences = createExampleConferences();
		ArrayList<Conference> happeningNow_conferences = this.getConferencesHappeningNow(conferences);
		ArrayList<Conference> upcoming_conferences = this.getUpcomingConferences(conferences);
		ArrayList<Conference> invited_list = new ArrayList<Conference>();
		for (Conference conference: conferences){
			if(conference.getConferenceType(currentTime) == Conference.INVITED){
				invited_list.add(conference);
			}
		}
		if (conferences.size() > 0){
			ConferenceSchedulerAdapter adapter = new ConferenceSchedulerAdapter(this, R.layout.conference_entry_layout, invited_list);
			
			//find the list view resource
			invited = (ListView) findViewById(R.id.conferenceScheduling_invitedList);
			invited.setAdapter(adapter);
			findViewById(R.id.conferenceScheduling_invitedBar).setVisibility(View.VISIBLE);
			invited.setVisibility(View.VISIBLE);
			invited.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					Conference selectedFrom = (Conference) (invited.getAdapter().getItem(position));
					Intent i = new Intent(getApplicationContext(), ConferenceCardView.class);
					i.putExtra("com.cornell.opencomm.model.Conference", selectedFrom);
					startActivity(i);					
				}

			});
		}
		if (happeningNow_conferences.size() > 0){
			ConferenceSchedulerAdapter adapter = new ConferenceSchedulerAdapter(this, R.layout.conference_entry_layout, happeningNow_conferences);

			//find the list view resource
			happeningNow = (ListView) findViewById(R.id.conferenceScheduling_happeningNowList);
			happeningNow.setAdapter(adapter);
			findViewById(R.id.conferenceScheduling_happeningNow).setVisibility(View.VISIBLE);
			happeningNow.setVisibility(View.VISIBLE);
		}
		if (upcoming_conferences.size() >0){
			ConferenceSchedulerAdapter adapter = new ConferenceSchedulerAdapter(this, R.layout.conference_entry_layout, upcoming_conferences);

			//find the list view resource
			upcoming = (ListView) findViewById(R.id.conferenceScheduling_upcomingList);
			upcoming.setAdapter(adapter);
			findViewById(R.id.conferenceScheduling_upcoming_bar).setVisibility(View.VISIBLE);
			upcoming.setVisibility(View.VISIBLE);
		}
	}
	


	public ArrayList<User> createExampleUsers(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("naka_shaka_laka", "Risa Naka", R.drawable.example_picture_1));
		users.add(new User("noratheexplora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		users.add(new User("makomania", "Makoto Bentz", R.drawable.example_picture_3));
		users.add(new User("graeme_craka", "Graeme Bailey", R.drawable.example_picture_1));
		users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
		users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
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
		conference2_attendees.add(users.get(3));
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
		conference2_attendees.add(users.get(4));
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
	
	//TODO- Need to fix this
	//Currently works when title is clicked only 
	//Need to make it so that if the overlay is clicked gets the title and works with it
	//Searches again among all conferences for title - highly inefficient 
	//Need to optimize and add more fields to conference to make this easier
	//Change the adapter so it takes in the conference object as a whole
	public void OnItemClick(View v){
		Button b = (Button) v;
		String conferenceTitle = b.getText().toString();
		for(Conference conference: conferences){
			if (conference.getConferenceTitle() == conferenceTitle){
				this.openContactCardActivity(conference); 
			}
		}
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


	public void addConferencePressed(View v){
		this.controller.addConferencePressed();
	}

	public void backButtonPressed(View v){
		this.controller.backButtonPressed();
	}

	public void notificationsPressed(View v){
		this.controller.notificationsPressed();
	}

	public void blockButtonPressed(View v){
		this.controller.blockButtonPressed();
	}


	public void openContactCardActivity(Conference conference){
		Intent i = new Intent(this, ConferenceCardView.class);
		i.putExtra("com.cornell.opencomm.model.Conference", conference);
		startActivity(i);
	}
}
