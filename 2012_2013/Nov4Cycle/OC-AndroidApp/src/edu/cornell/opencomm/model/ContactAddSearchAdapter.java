package edu.cornell.opencomm.model;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import edu.cornell.opencomm.R;

public class ContactAddSearchAdapter extends ArrayAdapter<User> {
	
	@SuppressWarnings("unused")
	private static final String TAG = "ContactSearchAdapter";
	@SuppressWarnings("unused")
	private static final boolean D = true;
	Context context;
	int layoutResourceId;
	ArrayList<User> data = null;

	public ContactAddSearchAdapter(Context context, int layoutResourceId, ArrayList<User> data){
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		SearchHolder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new SearchHolder();
			holder.contactName = (Button)row.findViewById(R.id.contact_addsearch_itemtext);
			holder.picture = (ImageView) row.findViewById(R.id.contact_addsearch_itemimage);
			row.setTag(holder);
		}
		else {
			holder= (SearchHolder) row.getTag();
		}
		User contact = data.get(position);
		//if this does not work - make fields 
		holder.contactName.setText(contact.getNickname());
		holder.picture.setBackgroundResource(contact.getImage()); 
		return row;
	}
	
	static class SearchHolder{
		Button contactName;
		ImageView picture;
	}

}

