package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ArrayAdapter for Contact List:<br>
 * Given an array of contacts(User), it populates the list in the order given.
 * Each list item contains an ImageView and a TextView.
 * 
 * Issues [TODO] 
 * - [frontend] change image to the person's profile photo
 * - For any other issues search for string "TODO"
 * 
 * @author Risa Naka [frontend]
 * */
public class ContactListAdapter extends ArrayAdapter<User> {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = ContactListAdapter.class.getSimpleName();
	
	private ArrayList<User> contacts;
	private Context context;
	private LayoutInflater inflater;

	public ContactListAdapter(Context context, 
			int textViewResourceId, ArrayList<User> contacts) {
		super(context, R.layout.contactlist_item_layout, contacts);
		this.context = context;
		this.contacts = contacts;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public User getItem(int position) {
		return this.contacts.get(position);
	}

	/** 
	 * = View at position p, which contains a TextView and an ImageView
	 * */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.contactlist_item_layout, null);
		TextView name = (TextView) vi.findViewById(R.id.contact_itemtext);
		ImageView image = (ImageView) vi.findViewById(R.id.contact_itemimage);
		View state_view = (View) vi.findViewById(R.id.contact_onlinestate);
		User contact = this.contacts.get(position);
		name.setText(contact.getNickname());
		// TODO set visibility of state_view here
		FontSetter.applyRobotoFont(this.context, name);
		return vi;
	}
}
