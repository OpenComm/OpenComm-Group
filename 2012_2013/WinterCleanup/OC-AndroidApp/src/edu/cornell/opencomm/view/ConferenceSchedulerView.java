package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceSchedulerController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceSchedulerAdapter;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.util.TestDataGen;
//TODO: Conference scheduler view has typo--string "invited_by" should have value "invited by" not "invited_by"

@SuppressWarnings("unused")
public class ConferenceSchedulerView extends Activity {
	private static String TAG = ConferenceSchedulerView.class.getName();
	/**
	 * 
	 */
	private ArrayList<Conference> conferences;	
	/**
	 * 
	 */
	private Calendar currentTime;
	/**
	 * 
	 */
	private ConferenceSchedulerController controller; 
	/**
	 * 
	 */
	private ListView happeningNowListView;
	/**
	 * 
	 */
	private ListView upcomingListView;
	/**
	 * 
	 */
	private ListView invitedListView;



	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_scheduling_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.conference_scheduling_layout));
		controller = new ConferenceSchedulerController(this, this);
		currentTime = Calendar.getInstance();
		init();
	}
	//TODO: warning: for testing only
	private void populateTestData(){
		conferences = TestDataGen.createExampleConferences();
		createConferenceList();
	}
	
	@Override
	protected void onStop() {
		currentTime = null;
		conferences = null;
		happeningNowListView = null;
		upcomingListView = null;
		invitedListView = null;
		super.onStop();
	}
	
	/**
	 * Initializes the conference scheduler page
	 */
	private void init(){
		invitedListView = (ListView) findViewById(R.id.conferenceScheduling_invitedList);
		happeningNowListView = (ListView) findViewById(R.id.conferenceScheduling_happeningNowList);
		upcomingListView = (ListView) findViewById(R.id.conferenceScheduling_upcomingList);
		//Actual Call
//		retrieveConferences();
		//Mock Data
		populateTestData();
	}
	private void retrieveConferences(){
		controller.sendPullrequest();
	}
	public void dataReceived(String confernceList){
		conferences = Conference.parseConferenceDataToObjects(confernceList);
		createConferenceList();
	}
	private void createConferenceList(){
		ArrayList<Conference> happeningNowConfList = new ArrayList<Conference>();
		ArrayList<Conference> upcomingConfList = new ArrayList<Conference>();
		ArrayList<Conference> invitedConfList = new ArrayList<Conference>();
		if(conferences != null && conferences.size() > 0){
			for(Conference conference : conferences){
				int conferenceTimeType = conference.getConferenceTimeType(Calendar.getInstance());
				int conferenceType = conference.getConferenceType(Calendar.getInstance());
				switch (conferenceTimeType) {
				case Conference.HAPPENING_NOW:
					happeningNowConfList.add(conference);
					break;
				case Conference.UPCOMING:
					upcomingConfList.add(conference);
					break;
				default:
					//Ignore previous conferences
					break;
				}
				if(conferenceType == Conference.INVITED){
					invitedConfList.add(conference);
				}
			}//end of for
			displayConferenceList(invitedListView, invitedConfList,R.id.conferenceScheduling_invitedBar);
			displayConferenceList(happeningNowListView, happeningNowConfList,R.id.conferenceScheduling_happeningNow);
			displayConferenceList(upcomingListView,upcomingConfList,R.id.conferenceScheduling_upcoming_bar);
		}else{
			//show no scheduled conferences
		}
		
	}
	private void displayConferenceList(ListView listView, ArrayList<Conference> cList,int separatorLayout){
		Log.d(TAG, "List Size : "+cList.size());
		if(cList.size() > 0){
			ConferenceSchedulerAdapter adaptor = new ConferenceSchedulerAdapter(this,R.layout.conference_entry_layout,cList);
			listView.setAdapter(adaptor);
			findViewById(separatorLayout).setVisibility(View.VISIBLE);
			listView.setVisibility(View.VISIBLE);
			listView.invalidate();
			
		
		}
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


	public void addConferencePressed(View v){
		this.controller.addConferencePressed();
	}

	public void backButtonPressed(View v){
		this.controller.backButtonPressed();
	}

	public void notifications(View v){
		this.controller.notificationsPressed();
	}

	public void overflow(View v){
		this.controller.overflowPressed();
	}


	public void openContactCardActivity(Conference conference){
		Intent i = new Intent(this, ConferenceCardView.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("conference", conference);
        conference.getAttendees();
        for (User user : conference.getAttendees()){
        	Log.v("UsersInIntent", user.getUsername());
        }
        i.putExtras(bundle);
		startActivity(i);
	}
}
//