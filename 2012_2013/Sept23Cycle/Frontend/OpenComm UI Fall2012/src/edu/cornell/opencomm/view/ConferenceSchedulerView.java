package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ConferenceDetails;
import edu.cornell.opencomm.model.ConferenceEntry;
import edu.cornell.opencomm.model.ConferenceSchedulerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class ConferenceSchedulerView extends Activity {
	 /** Called when the activity is first created. */
	/*
	 private ExpandListAdapter ExpAdapter;
	 private ArrayList<ExpandListGroup> ExpListItems;
	 private ExpandableListView ExpandList;
	 */
	private ConferenceSchedulerAdapter adapter;
	private ArrayList<ConferenceEntry> conferenceEntries;
	private ExpandableListView expandList;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.conference_scheduling_layout);
	    expandList = (ExpandableListView) findViewById(R.id.conferences_list);
	    conferenceEntries = SetStandardGroups();
	    adapter = new ConferenceSchedulerAdapter(ConferenceSchedulerView.this, conferenceEntries);
	    expandList.setAdapter(adapter);
	 }
	 
	 public ArrayList<ConferenceEntry> SetStandardGroups() {
	/*	ArrayList<ConferenceEntry> list = new ArrayList<ConferenceEntry>();
		ArrayList<ConferenceDetails> list2 = new ArrayList<ConferenceDetails>();
		ConferenceEntry gru1 = new ConferenceEntry();
		gru1.setConferenceTitle("Comedy");
		ConferenceDetails ch1_1 = new ConferenceDetails();
		ch1_1.setName("A movie");
		ch1_1.setTag(null);
		list2.add(ch1_1);
		ConferenceDetails ch1_2 = new ConferenceDetails();
		ch1_2.setName("An other movie");
		ch1_2.setTag(null);
		list2.add(ch1_2);
		ConferenceDetails ch1_3 = new ConferenceDetails();
		ch1_3.setName("And an other movie");
		ch1_3.setTag(null);
		list2.add(ch1_3);
		gru1.setItems(list2);
		list2 = new ArrayList<ConferenceDetails>();          
		ConferenceEntry gru2 = new ConferenceEntry();
		gru2.setName("Action");
		ConferenceDetails ch2_1 = new ConferenceDetails();
		ch2_1.setName("A movie");
		ch2_1.setTag(null);
		list2.add(ch2_1);
		ConferenceDetails ch2_2 = new ConferenceDetails();
		ch2_2.setName("An other movie");
		ch2_2.setTag(null);
		list2.add(ch2_2);
		ConferenceDetails ch2_3 = new ConferenceDetails();
		ch2_3.setName("And an other movie");
		ch2_3.setTag(null);
		list2.add(ch2_3);
		gru2.setItems(list2);
		list.add(gru1);
		list.add(gru2);
		return list; */
		 return null;
	 }
}
