package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConferenceSchedulerAdapter extends BaseExpandableListAdapter {
	private Context context;
	    private ArrayList<Conference> conferences;
	    private boolean conference_is_happening_now;
	    
	    public ConferenceSchedulerAdapter(Context context, ArrayList<Conference> groups, boolean conference_is_happening_now) {
	        this.context = context;
	        this.conferences = groups;
	        this.conference_is_happening_now = conference_is_happening_now;
	    }
	     
	    public void addItem(Conference conference) {
	    	if (!conferences.contains(conference)){
	    		conferences.add(conference);
	    	}
	    }
	    
	    public Object getChild(int groupPosition, int childPosition) {
	    	return conferences.get(groupPosition);
	    }
	 
	    public long getChildId(int groupPosition, int childPosition) {
	    	return groupPosition;
	    }
	 
	    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
	            ViewGroup parent) {
	     	if (view == null) {
	    		LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	    		view = infalInflater.inflate(R.layout.conference_details_layout, null);
	    	}
	    	Conference conference = (Conference) getGroup(groupPosition); 
	    	// TODO Spandana - Assign correct information from conference object to elements in view
	    	return view;
	    }
	 
	    public int getChildrenCount(int groupPosition) {
	    	return 1;
	    }
	 
	    public Object getGroup(int groupPosition) {
	        return conferences.get(groupPosition);
	    }
	 
	    public int getGroupCount() {
	        return conferences.size();
	    }
	 
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }
	 
	    public View getGroupView(int groupPosition, boolean isLastChild, View view,
	            ViewGroup parent) {
	    	Conference conference = (Conference) getGroup(groupPosition);
	    	if (view == null) {
	    		LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	    		view = inf.inflate(R.layout.conference_entry_layout, null);
	    	}
	    	
    		customizeView(conference, view);
	    	return view;
	    }
	    
	    /** Customize the view from conference information, and add functionality to buttons */
	    public void customizeView(Conference conference, View view){
	    	TextView conference_title = (TextView) view.findViewById(R.id.conference_title);
	    	conference_title.setText(conference.getConferenceTitle());
	    	TextView conference_attendees = (TextView) view.findViewById(R.id.attendees);
	    	conference_attendees.setText(conference.getAttendeesSummary());
	    	if (conference_is_happening_now){
	    		TextView day = (TextView)view.findViewById(R.id.day);
	    		day.setVisibility(View.GONE);
	    		TextView time = (TextView)view.findViewById(R.id.time);
	    		time.setVisibility(View.GONE); 
	    	}
	    	else {
	    		ImageView go_to_conference_button = (ImageView)view.findViewById(R.id.go_to_conference_button);
	    		go_to_conference_button.setVisibility(View.GONE);
	    		TextView day = (TextView)view.findViewById(R.id.day);
	    		day.setText(conference.getNumberMonthAndDay());
	    		TextView time = (TextView)view.findViewById(R.id.time);
	    		time.setText(conference.getTimeHourAndMinute()); 
	    	} 
	    	
	    }
	 
	    public boolean hasStableIds() {
	        return true;
	    }
	 
	    public boolean isChildSelectable(int arg0, int arg1) {
	        return true;
	    }

}
