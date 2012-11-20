package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;

public class ConferencePageAdapter extends PagerAdapter {
	ConferenceView conferenceView;

	public ConferencePageAdapter(ConferenceView conferenceView){
		this.conferenceView = conferenceView;
	}

	public int getCount() {
		return 3;
	}
	
	public Object instantiateItem(View collection, int position) {
		
		ViewPager viewPager = (ViewPager)collection;
		ConferenceRoomView room = (ConferenceRoomView) viewPager.getChildAt(position);
		room.create();
		return room;
	}
	
/*	public Object instantiateItem(View collection, int position) {
		LayoutInflater inflater = (LayoutInflater) collection.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int resId = 0;

		// TODO Spandana ... Decide which layout to use (can use multiple if you want)
		resId = R.layout.conference_room_layout;

		View view = inflater.inflate(resId, null);       
		((ViewPager) collection).addView(view, 0);
		
		// TODO Spandana .. if you want to customize information
		TextView title = (TextView) view.findViewById(R.id.text);
		switch (position) {
		case 0:
			View v =( (ViewPager) collection).getChildAt(position);
			v.in
			title.setText("Left Sidechat");
			Bitmap bitmap = BitmapFactory.decodeResource(conferenceView.getResources(), R.drawable.portrait_left_background); 
			bitmap = Bitmap.createScaledBitmap(bitmap, 320, 510, true); 
			BitmapDrawable bitmap2 = new BitmapDrawable(conferenceView.getResources(), bitmap); 
			view.setBackgroundDrawable(bitmap2); 
			view.setContentDescription("leftSideChat"); 
			break;
		case 1:
			title.setText("Main Chat");
			Bitmap bitmap1 = BitmapFactory.decodeResource(conferenceView.getResources(), R.drawable.portrait_main_background); 
			bitmap1 = Bitmap.createScaledBitmap(bitmap1, 320, 510, true); 
			BitmapDrawable bitmap3 = new BitmapDrawable(conferenceView.getResources(), bitmap1); 
			view.setBackgroundDrawable(bitmap3); 
			view.setContentDescription("mainChat"); 
			break;
		case 2:
			title.setText("Right Sidechat");
			view.setBackgroundResource(R.drawable.portrait_left_background); 
			view.setContentDescription("rightSideChat"); 
			break;
		}
		conferenceView.switchChatSpaceModel(); // TODO Ankit ... should this method be called here?
		return view;
	} */
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	@Override
	public Parcelable saveState() {
		return null;
	}
}