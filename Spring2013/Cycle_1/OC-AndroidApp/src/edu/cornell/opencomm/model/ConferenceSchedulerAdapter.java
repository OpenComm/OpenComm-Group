package edu.cornell.opencomm.model;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.cornell.opencomm.R;

public class ConferenceSchedulerAdapter extends ArrayAdapter<Conference> {
	
	@SuppressWarnings("unused")
	private static final String TAG = "ConferenceSchedulerView";
	@SuppressWarnings("unused")
	private static final boolean D = true;
	Context context;
	int layoutResourceId;
	ArrayList<Conference> data = null;

	public ConferenceSchedulerAdapter(Context context, int layoutResourceId, ArrayList<Conference> data){
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		ConferenceHolder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ConferenceHolder();
			holder.conferenceTitle = (TextView)row.findViewById(R.id.conferenceTitle);
			holder.startTime = (TextView) row.findViewById(R.id.startTime);
			row.setTag(holder);
		}
		else {
			holder= (ConferenceHolder) row.getTag();
		}
		Conference conference = data.get(position);
		//if this does not work - make fields 
		holder.conferenceTitle.setText(conference.getConferenceTitle());
		holder.startTime.setText(conference.getStartDateAndTimeDescription()); 
		return row;
	}
	
	static class ConferenceHolder{
		TextView conferenceTitle;
		TextView startTime;
	}

}

