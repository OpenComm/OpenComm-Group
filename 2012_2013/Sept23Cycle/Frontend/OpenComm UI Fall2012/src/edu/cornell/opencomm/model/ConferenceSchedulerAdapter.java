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
	    private ArrayList<ConferenceEntry> groups;
	    public ConferenceSchedulerAdapter(Context context, ArrayList<ConferenceEntry> groups) {
	        this.context = context;
	        this.groups = groups;
	    }
	     
	    public void addItem(ConferenceDetails item, ConferenceEntry group) {
	     /*   if (!groups.contains(group)) {
	            groups.add(group);
	        }
	        int index = groups.indexOf(group);

	        ArrayList<ConferenceDetails> ch = groups.get(index).getItems();
	        ch.add(item);
	        groups.get(index).setItems(ch); */
	    }
	    public Object getChild(int groupPosition, int childPosition) {
	     /*   // TODO Auto-generated method stub
	        ArrayList<ConferenceDetails> chList = groups.get(groupPosition).getItems();
	        return chList.get(childPosition); */
	    	return null;
	    }
	 
	    public long getChildId(int groupPosition, int childPosition) {
	        // TODO Auto-generated method stub
	        return childPosition;
	    }
	 
	    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
	            ViewGroup parent) {
	    /*	ConferenceDetails child = (ConferenceDetails) getChild(groupPosition, childPosition);
	        if (view == null) {
	            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	            view = infalInflater.inflate(R.layout.expandlist_child_item, null);
	        }
	        TextView tv = (TextView) view.findViewById(R.id.tvChild);
	        tv.setText(child.getName().toString());
	        tv.setTag(child.getTag());
	        // TODO Auto-generated method stub
	        return view; */
	    	return null;
	    }
	 
	    public int getChildrenCount(int groupPosition) {
	        // TODO Auto-generated method stub
	    /*    ArrayList<ConferenceDetails> chList = groups.get(groupPosition).getItems();
	        return chList.size(); */
	    	return 0;
	    }
	 
	    public Object getGroup(int groupPosition) {
	        // TODO Auto-generated method stub
	        return groups.get(groupPosition);
	    }
	 
	    public int getGroupCount() {
	        // TODO Auto-generated method stub
	        return groups.size();
	    }
	 
	    public long getGroupId(int groupPosition) {
	        // TODO Auto-generated method stub
	        return groupPosition;
	    }
	 
	    public View getGroupView(int groupPosition, boolean isLastChild, View view,
	            ViewGroup parent) {
	        ConferenceEntry group = (ConferenceEntry) getGroup(groupPosition);
	        if (view == null) {
	            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	            view = inf.inflate(R.layout.conference_entry_layout, null);
	        }
	        TextView tv = (TextView) view.findViewById(R.id.conference_title);
	        tv.setText(group.getConferenceTitle());
	        // TODO Auto-generated method stub
	        return view;
	    }
	 
	    public boolean hasStableIds() {
	        // TODO Auto-generated method stub
	        return true;
	    }
	 
	    public boolean isChildSelectable(int arg0, int arg1) {
	        // TODO Auto-generated method stub
	        return true;
	    }

}
