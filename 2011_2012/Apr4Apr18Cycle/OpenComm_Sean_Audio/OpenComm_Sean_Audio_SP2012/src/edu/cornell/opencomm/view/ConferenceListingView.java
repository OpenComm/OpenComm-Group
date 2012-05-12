package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListingView extends SlidingDrawer {
    Conference conference;

    public ConferenceListingView(Context context, AttributeSet attrs, Conference conf) {
        super(context, attrs);
        this.conference = conf;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.conference_list_layout, this);
        
        
    }

    public void editInfo(){
    	conference.getPlannerView().launch();
    }
    
    

   


}
