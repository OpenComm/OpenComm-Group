package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ConferenceListing> conferenceListings = new ArrayList<ConferenceListing>();

    public ConferenceListExpandableListAdapter(Context context, List<Conference> conferenceList) {
        this.context = context;
        for(Conference conference : conferenceList)
            this.conferenceListings.add(new ConferenceListing(context, conference));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        return ((ConferenceListing)getChild(groupPosition, childPosition)).getConferenceDetailsView();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return ((ConferenceListing)getGroup(groupPosition)).getConferenceHeaderView();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(childPosition != 0 || groupPosition > conferenceListings.size())
            return null;
        return conferenceListings.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition > conferenceListings.size())
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getChild(groupPosition, 0);
    }

    @Override
    public int getGroupCount() {
        return conferenceListings.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
