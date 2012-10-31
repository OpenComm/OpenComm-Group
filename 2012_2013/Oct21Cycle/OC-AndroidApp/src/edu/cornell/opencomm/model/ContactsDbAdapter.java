package edu.cornell.opencomm.model;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import edu.cornell.opencomm.R;

/**
 * More information here:
 * http://www.mysamplecode.com/2012/07/android-listview-cursoradapter
 * -sqlite.html
 * 
 * @author noranq
 * 
 */

public class ContactsDbAdapter extends ArrayAdapter{
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PHONE_NUMBER = "phone_number";
	public static final String KEY_PICTURE = "picture";

	@SuppressWarnings("unused")
	private static final String TAG = "ContactsDbAdapter";
	@SuppressWarnings("unused")
	private static final String DATABASE_NAME = "Opencomm";
	@SuppressWarnings("unused")
	private static final String MYSQL_TABLE = "Contacts";

	
	private static final boolean D = true;
	Context context;
	int layoutResourceId;
	ArrayList<User> data = null;

	public ContactsDbAdapter(Context context, int layoutResourceId, ArrayList<User> data){
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	

	/** Open writable database */
	public void open() {
		// TODO Connect to mysql database
	}

	/** Retrieve all contacts */
	public Cursor getAllContacts() {
		// TODO Fetch all contacts from mysql database
		Cursor contacts = null;
		return contacts;
	}
	

	@Override
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		ContactHolder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ContactHolder();
			holder.contactName = (Button)row.findViewById(R.id.contact_search_name);
			holder.contactPicture = (ImageView) row.findViewById(R.id.contact_picture);
			row.setTag(holder);
		}
		else {
			holder= (ContactHolder) row.getTag();
		}
		User user = data.get(position);
		//if this does not work - make fields 
		holder.contactName.setText(user.getUsername());
		holder.contactPicture.setBackgroundResource(user.getImage()); 
		return row;
	}
	
	static class ContactHolder{
		Button contactName;
		ImageView contactPicture;
	}
}
