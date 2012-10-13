package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ConferenceSchedulerAdapter extends BaseExpandableListAdapter {
	private Context context;
	    private ArrayList<Conference> conferences;
	    
	    public ConferenceSchedulerAdapter(Context context, ArrayList<Conference> groups) {
	        this.context = context;
	        this.conferences = groups;
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
	    	TextView conference_title_textview = (TextView) view.findViewById(R.id.conference_title);
	    	conference_title_textview.setText(conference.getConferenceTitle());
	    	return view;
	    }
	 
	    public boolean hasStableIds() {
	        return true;
	    }
	 
	    public boolean isChildSelectable(int arg0, int arg1) {
	        return true;
	    }

}
