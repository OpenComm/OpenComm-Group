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
 * ArrayAdapter for Notifications List:<br>
 * Given a list of notifications, it populates the list in the order given.
 * Each list item contains an ImageView and a TextView.
 * 
 * Issues [TODO] - For any other issues search for string "TODO"
 * 
 * @author Risa Naka [frontend]
 * */
public class NotificationsAdapter extends ArrayAdapter<Notification> {
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
	private static final String TAG = NotificationsAdapter.class.getSimpleName();
	
	private ArrayList<Notification> notifications;
	private Context context;
	private LayoutInflater inflater;

	public NotificationsAdapter(Context context, 
			int textViewResourceId, ArrayList<Notification> notes) {
		super(context, R.layout.notifications_item_layout, notes);
		this.context = context;
		this.notifications = notes;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/** 
	 * = View at position p, which contains a TextView and an ImageView
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.notifications_item_layout, null);
		TextView note = (TextView) vi.findViewById(R.id.notifications_itemtext);
		ImageView bullet = (ImageView) vi.findViewById(R.id.notifications_bullet);
		Notification notification = this.notifications.get(position);
		note.setText(notification.getMessage());
		// set bullet visible depending on whether the note has been read
		if (notification.isRead()) bullet.setVisibility(View.INVISIBLE);
		else {
			bullet.setVisibility(View.VISIBLE);
		}
		FontSetter.applySanSerifFont(this.context, note);
		return vi;
	}
}
