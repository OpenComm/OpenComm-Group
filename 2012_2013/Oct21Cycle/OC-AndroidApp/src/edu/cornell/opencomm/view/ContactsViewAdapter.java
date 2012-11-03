package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsViewAdapter extends ArrayAdapter<User>{
	Context context;
	int layoutResourceId;
	User[] contacts;
	
	public ContactsViewAdapter(Context context, int layoutResourceId,
			User[] contacts) {
		super(context, layoutResourceId, contacts);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.contacts = contacts;
	}
	
	 public View getView(int position, View convertView, ViewGroup parent) {
	     User contact = contacts[position];
	     View contact_entry = convertView;
	     if (contact_entry == null){
	    	 LayoutInflater infalInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				contact_entry = infalInflater.inflate(R.layout.contact_entry_layout,
						null);
	     }
	     ImageView contact_picture = (ImageView) contact_entry.findViewById(R.id.contact_picture);
	     Resources res = context.getResources();
//	   		TODO: fix to use bitmap
//	     Drawable drawable = res.getDrawable(contact.getImage());
//	     contact_picture.setImageDrawable(drawable); 
	     TextView contact_name = (TextView) contact_entry.findViewById(R.id.contact_name);
	     contact_name.setText(contact.getNickname());
	     return contact_entry;
	    }
	    
}
