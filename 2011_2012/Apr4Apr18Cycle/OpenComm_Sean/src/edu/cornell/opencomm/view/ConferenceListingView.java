package edu.cornell.opencomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SlidingDrawer;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListingView extends SlidingDrawer {
    Conference conference;

    public ConferenceListingView(Context context, AttributeSet attrs, Conference conf) {
        super(context, attrs);
        this.conference = conference;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.conference_listing_layout, this);
    }
}
