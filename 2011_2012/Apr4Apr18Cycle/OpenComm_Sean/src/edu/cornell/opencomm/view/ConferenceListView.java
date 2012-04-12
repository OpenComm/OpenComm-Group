package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;

public class ConferenceListView extends Activity {

    private View layout;
    private ConferenceListExpandableListAdapter upcomingAdapter;
    private ConferenceListExpandableListAdapter happeningNowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = getLayoutInflater().inflate(R.layout.conference_list_layout, null);
        setContentView(layout);
        /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getUpcomingConferences().addView(inflater.inflate(R.layout.conference_listing_layout, null));
        getHappeningNowConferences().invalidate();
        getUpcomingConferences().invalidate();
        layout.invalidate();*/
        ArrayList<Conference> conferences = new ArrayList<Conference>();
        Conference conference1 = new Conference(2012, 5, 27, 10, 00, 12, 00, null, "Jonathan Pullano");
        Conference conference2 = new Conference(2012, 11, 9, 12, 30, 13, 30, null, "Nora Ng-Quinn");
        Conference conference3 = new Conference(2013, 6, 25, 8, 30, 9, 15, null, "Vinay Maloo");
        Conference conference4 = new Conference(2014, 3, 2, 9, 45, 10, 30, null, "Risa Naka");
        conferences.add(conference1);
        conferences.add(conference2);
        conferences.add(conference3);
        conferences.add(conference4);
        initialize(conferences);

    } // end onCreate method

    private void initialize(List<Conference> conferences) {
        ArrayList<Conference> currentConferences = new ArrayList<Conference>();
        ArrayList<Conference> futureConferences = new ArrayList<Conference>();
        //TODO: Should we consider past conferences?
        for(Conference conference : conferences) {
            if(conference.isNow())
                currentConferences.add(conference);
            else
                futureConferences.add(conference);
        }
        upcomingAdapter = new ConferenceListExpandableListAdapter(this, futureConferences);
        happeningNowAdapter = new ConferenceListExpandableListAdapter(this, currentConferences);
        getUpcomingConferences().setAdapter(upcomingAdapter);
        getHappeningNowConferences().setAdapter(happeningNowAdapter);
    }

    public ExpandableListView getUpcomingConferences() {
        if (layout != null)
            return (ExpandableListView) layout.findViewById(R.id.upcomingConferences);
        return null;
    }

    public ExpandableListView getHappeningNowConferences() {
        if (layout != null)
            return (ExpandableListView) layout.findViewById(R.id.happeningNowConferences);
        return null;
    }
}
