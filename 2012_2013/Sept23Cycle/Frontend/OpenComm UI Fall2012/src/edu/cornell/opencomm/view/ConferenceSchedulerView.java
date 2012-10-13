package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceSchedulerAdapter;
import edu.cornell.opencomm.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class ConferenceSchedulerView extends Activity {
	 /** Called when the activity is first created. */
	private ConferenceSchedulerAdapter adapter;
	private ArrayList<Conference> conferences;
	private ExpandableListView expandList;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.conference_scheduling_layout);
	    expandList = (ExpandableListView) findViewById(R.id.conferences_list);
	    conferences = setExampleConferences();
	    adapter = new ConferenceSchedulerAdapter(ConferenceSchedulerView.this, conferences);
	    expandList.setAdapter(adapter);
	 }
	 
	 public ArrayList<Conference> setExampleConferences(){
		 ArrayList<Conference> conferences = new ArrayList<Conference>();
		 Conference conference1 = new Conference( "Morning Meeting", 
				 					  			  "5/7/12", 
				 					  			  "5:00am", 
				 					  			  "7:30am", 
				 					  			  "Every Thursday", 
				 					  			  "Remember to have your documents ready", 
				 					  			  null );
		 Conference conference2 = new Conference( "Evening Meeting", 
	  			  "5/7/12", 
	  			  "5:00pm", 
	  			  "7:30pm", 
	  			  "Every Thursday", 
	  			  "Remember to have your documents ready", 
	  			  null );
		 conferences.add(conference1);
		 conferences.add(conference2);
		 return conferences;
	 }
}
