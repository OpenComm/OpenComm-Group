package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * ArrayAdapter for Overflow List:<br>
 * Given a list of options, it populates the overflow list in the order given.
 * Each list item contains an ImageView and a TextView.
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Risa Naka [frontend]
 * */
public class InvitationsAdapter extends ArrayAdapter<User> {
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
	private static final String TAG = OverflowAdapter.class.getSimpleName();

	private Context context;
	private ArrayList<User> options;
	private LayoutInflater inflater;

	/**
	 * Constructor: populates the overflow list with the given options, in the
	 * given order
	 * @param options - a string array of options to populate the list */
	public InvitationsAdapter(Context context, int textViewResourceId, ArrayList<User> options) {
		super(context, R.layout.dashboard_invitations_entry, options);
		this.context = context;
		this.options = options;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/** 
	 * = View at position p, which contains a TextView and an ImageView
	 * */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.dashboard_invitations_entry, null);
		TextView option = (TextView) vi.findViewById(R.id.invitations_itemtext);
		option.setText(this.options.get(position).getNickname());
		FontSetter.applyRobotoFont(this.context, vi);
		return vi;
	}
}