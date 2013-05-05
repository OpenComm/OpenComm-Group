package edu.cornell.opencomm.model;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.util.OCBitmapDecoder;
import edu.cornell.opencomm.view.UserView;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
	private float screenWidth;

	public ContactListAdapter(Context context, 
			int textViewResourceId, ArrayList<User> contacts) {
		super(context, R.layout.contactlist_item_layout, contacts);
		this.context = context;
		this.contacts = contacts;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
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
			vi = inflater.inflate(R.layout.contacts_entry_layout, null);
		TextView name = (TextView) vi.findViewById(R.id.contact_itemtext);
		ImageView image = (ImageView) vi.findViewById(R.id.contact_itemimage);
		
		View state_view = (View) vi.findViewById(R.id.contact_onlinestate);
		User contact = this.contacts.get(position);
		name.setText(contact.getNickname());
		
		//TODO: get the online statues of uerser here
		boolean online = true;
		// online = getOnlineStatus();
		if(online)
			state_view.setVisibility(View.VISIBLE);
		else
			state_view.setVisibility(View.INVISIBLE);
		
		Bitmap  bm = getRoundedCornerBitmap(OCBitmapDecoder.getThumbnailFromResource(
				image.getResources(), contact.getImage()));
		image.setImageBitmap(bm);
		// TODO set visibility of state_view here
		FontSetter.applyRobotoFont(this.context, name);
		return vi;
	}
	
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {	
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		float userPicSize = screenWidth * 13/48;
		Bitmap output = Bitmap.createBitmap((int)userPicSize, (int)userPicSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		// just for conversion
		int borderDips = 6;
		final int borderSizePx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, borderDips, context
				.getResources().getDisplayMetrics());
		// prepare for canvas
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, (int)userPicSize, (int)userPicSize);
		// prepare canvas for transfer
		paint.setAntiAlias(true);
		paint.setColor(0xFFFFFFFF);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(userPicSize / 2, userPicSize / 2, userPicSize / 2 - 5, paint);

		// draw bitmap
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, null, rect, paint);

		// draw border
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawCircle(userPicSize / 2, userPicSize / 2, userPicSize / 2 - 5, paint);

		return output;

	}
}
