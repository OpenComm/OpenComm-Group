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
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        
        // TODO Spandana ... Decide which layout to use (can use multiple if you want)
        resId = R.layout.conference_room_layout;
        
        View view = inflater.inflate(resId, null);  
        ((ViewPager) collection).addView(view, 0);
        
        // TODO Spandana .. if you want to customize information
        TextView title = (TextView) view.findViewById(R.id.text);
        title.setTag("Title");
        switch (position) {
        case 0:
        	title.setText("Left Sidechat");
        	//view.setBackgroundResource(R.drawable.portrait_left_background); 
        	view.setContentDescription("leftSideChat"); 
        //	view.addView(new ChatSpaceViewGroup(), layout params); 
        	break;
        case 1:
        	title.setText("Main Chat");
        	 //view.setBackgroundResource(R.drawable.portrait_main_background); 
        	 view.setContentDescription("mainChat"); 
        	break;
        case 2:
        	title.setText("Right Sidechat");
        	//view.setBackgroundResource(R.drawable.portrait_left_background); 
        	// TODO spandana get a portrait right image
        	view.setContentDescription("rightSideChat"); 
        	break;
        }
        
        conferenceView.switchChatSpaceModel(); 
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