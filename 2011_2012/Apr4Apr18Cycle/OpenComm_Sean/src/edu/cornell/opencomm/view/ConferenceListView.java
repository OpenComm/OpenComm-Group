package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Collection;

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
        ArrayList<String> openComm = new ArrayList<String>();
        openComm.add("Jonathan");
        openComm.add("Flavian");
        openComm.add("Najla");
        openComm.add("Ashley");
        openComm.add("Brandon");
        openComm.add("Joey");
        openComm.add("Rahul");
        openComm.add("Risa");
        openComm.add("Chris");
        openComm.add("Vinay");
        openComm.add("Crystal");
        openComm.add("Kris");

        ArrayList<String> najla = new ArrayList<String>();
        najla.add("Najla Elmachtoub");

        ArrayList<String> jonathan = new ArrayList<String>();
        najla.add("Jonathan Pullano");

        ArrayList<String> vinay = new ArrayList<String>();
        najla.add("Vinay Maloo");

        ArrayList<String> graeme = new ArrayList<String>();
        najla.add("Graeme Bailey");

        ArrayList<String> nora = new ArrayList<String>();
        najla.add("Nora Ng-Quinn");

        ArrayList<Conference> conferences = new ArrayList<Conference>();
        Conference conference1 = new Conference(2012, 4, 27, 10, 00, 12, 00, null, "Vinay Maloo", "Graduation", vinay);
        Conference conference2 = new Conference(2012, 10, 9, 12, 30, 13, 30, null, "Nora Ng-Quinn", "OMG", nora);
        Conference conference3 = new Conference(2013, 5, 25, 8, 30, 9, 15, null, "Najla Elmachtoub", "Canada Trip", najla);
        Conference conference4 = new Conference(2015, 2, 14, 9, 26, 6, 53, null, "Graeme Bailey", "Pi Day", graeme);
        Conference conference5 = new Conference(2011, 3, 12, 17, 45, 19, 45, null, "Risa Naka", "OpenComm Meeting", openComm);
        Conference conference6 = new Conference(2012, 3, 12, 0, 0, 10, 0, null, "Jonathan Pullano", "Debugging Late", jonathan);
        conferences.add(conference1);
        conferences.add(conference2);
        conferences.add(conference3);
        conferences.add(conference4);
        conferences.add(conference5);
        conferences.add(conference6);
        initialize(conferences);

    } // end onCreate method

    private void initialize(Collection<Conference> conferences) {
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
