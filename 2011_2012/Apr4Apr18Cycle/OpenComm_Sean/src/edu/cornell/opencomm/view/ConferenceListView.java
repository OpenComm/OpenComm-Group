package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListView extends Activity {

    private View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        layout = getLayoutInflater().inflate(R.layout.conference_list_layout, null);
        setContentView(layout);
        /*ConferenceListingView view = new ConferenceListingView(this.getApplicationContext(), null, null);
        ConferenceListingView view1 = new ConferenceListingView(this.getApplicationContext(), null, null);
        ConferenceListingView view2 = new ConferenceListingView(this.getApplicationContext(), null, null);*/
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //getHappeningNowConferences().addView(inflater.inflate(R.layout.conference_listing_layout, null));
        //getHappeningNowConferences().addView(inflater.inflate(R.layout.conference_listing_layout, null));
        getUpcomingConferences().addView(inflater.inflate(R.layout.conference_listing_layout, null));
        getHappeningNowConferences().invalidate();
        getUpcomingConferences().invalidate();
        layout.invalidate();


    } // end onCreate method

    public ArrayList<ConferenceListView> getCurrentConferences() {
        return null;
    }

    public ArrayList<ConferenceListView> getFutureConferences() {
        return null;
    }

    public void addConference(Conference conference) {

        //TODO: Create and populate ConferenceListingView
        //and add to appropriate LinearLayout
    }

    public ViewGroup getUpcomingConferences() {
        if (layout != null)
            return (ViewGroup) layout.findViewById(R.id.upcomingConferences);
        return null;
    }

    public ViewGroup getHappeningNowConferences() {
        if (layout != null)
            return (ViewGroup) layout.findViewById(R.id.happeningNowConferences);
        return null;
    }
}
