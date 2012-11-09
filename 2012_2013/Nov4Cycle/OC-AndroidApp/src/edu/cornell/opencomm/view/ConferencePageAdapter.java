package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ConferencePageAdapter extends PagerAdapter {
	ConferenceView conferenceView;
	
	public ConferencePageAdapter(ConferenceView conferenceView){
		this.conferenceView = conferenceView;
	}
	
    public int getCount() {
        return 3;
    }
    public Object instantiateItem(View collection, int position) {
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
        	title.setText("Left Sidechat");
        	break;
        case 1:
        	title.setText("Main Chat");
        	break;
        case 2:
        	title.setText("Right Sidechat");
        	break;
        }
        conferenceView.switchChatSpaceModel(); // TODO Ankit ... should this method be called here?
        return view;
    }
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