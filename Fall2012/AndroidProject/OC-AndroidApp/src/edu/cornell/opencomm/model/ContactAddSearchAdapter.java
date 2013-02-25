package edu.cornell.opencomm.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;

/**
 * ArrayAdapter for Contact Search/Add:<br>
 * Given an array of contacts(User), it populates the list as the user types in the contact info.
 * Each list item contains an ImageView and a TextView.
 * 
 * Issues [TODO] 
 * - For any other issues search for string "TODO"
 * 
 * @author 
 * */
public class ContactAddSearchAdapter extends ArrayAdapter<String> {
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
	private static final String TAG =ContactAddSearchAdapter.class.getSimpleName();
	private Context context;
	private ArrayList<String> data;
	private LayoutInflater inflater;

	public ContactAddSearchAdapter(Context context, int textViewResourceId, ArrayList<String> data){
		super(context, R.layout.contact_addsearch_item_layout, data);
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}
	
	@Override
	
	public View getView(int position, View convertView, ViewGroup parent){
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.contact_addsearch_item_layout, null);
		TextView name = (TextView) vi.findViewById(R.id.contact_addsearch_itemtext);
		ImageView image = (ImageView) vi.findViewById(R.id.contact_addsearch_itemimage);
		String contact = this.data.get(position);
		name.setText(contact);
		// TODO [frontend] change image to the person's profile photo
		// TODO [backend] get user based on the name/email
		//image.setImageDrawable(null);
		FontSetter.applySanSerifFont(this.context, name);
		return vi;
	}
}

