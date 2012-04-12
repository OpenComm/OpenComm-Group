package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.view.ConferenceListExpandableListAdapter;
import edu.cornell.opencomm.view.ConferenceListing;

public class ConferenceListActivity extends Activity {

    private View layout;
    private ConferenceListExpandableListAdapter upcomingAdapter;
    private ConferenceListExpandableListAdapter happeningNowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = getLayoutInflater().inflate(R.layout.conference_list_layout, null);
        setContentView(layout);

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
        jonathan.add("Jonathan Pullano");

        ArrayList<String> mom = new ArrayList<String>();
        mom.add("Yo' mama");

        ArrayList<String> graeme = new ArrayList<String>();
        graeme.add("Graeme Bailey");

        ArrayList<String> flavian = new ArrayList<String>();
        flavian.add("Flavian Hautbois");

        ArrayList<Conference> conferences = new ArrayList<Conference>();
        Conference conference1 = new Conference(2012, 4, 27, 10, 00, 12, 00, null, "Vinay Maloo", "Graduation", mom);
        Conference conference2 = new Conference(2012, 10, 9, 12, 30, 13, 30, null, "Flavian", "Wine+Cheese", flavian);
        Conference conference3 = new Conference(2012, 5, 25, 8, 30, 9, 15, null, "Najla Elmachtoub", "Canada Trip", najla);
        Conference conference4 = new Conference(2015, 2, 14, 9, 26, 6, 53, null, "Graeme Bailey", "Pi Day", graeme);
        Conference conference5 = new Conference(2012, 3, 12, 17, 45, 19, 45, null, "Risa Naka", "OpenComm", openComm);
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
        ExpandableListView upcomingConferences = getUpcomingConferences();
        ExpandableListView happeningNowConferences = getHappeningNowConferences();

        upcomingConferences.setAdapter(upcomingAdapter);
        happeningNowConferences.setAdapter(happeningNowAdapter);
        upcomingConferences.setGroupIndicator(null);
        happeningNowConferences.setGroupIndicator(null);

        upcomingConferences.setOnGroupExpandListener(new ExpandListener(upcomingConferences, happeningNowConferences));
        upcomingConferences.setOnGroupCollapseListener(new CollapseListener(upcomingConferences));
        happeningNowConferences.setOnGroupExpandListener(new ExpandListener(happeningNowConferences, upcomingConferences));
        happeningNowConferences.setOnGroupCollapseListener(new CollapseListener(happeningNowConferences));

    }

    public class ExpandListener implements ExpandableListView.OnGroupExpandListener {

        private ExpandableListView me;
        private ExpandableListView other;

        public ExpandListener(ExpandableListView me, ExpandableListView other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public void onGroupExpand(int groupPosition) {
            ConferenceListExpandableListAdapter myAdapter = (ConferenceListExpandableListAdapter)me.getExpandableListAdapter();
            ConferenceListExpandableListAdapter otherAdapter = (ConferenceListExpandableListAdapter)other.getExpandableListAdapter();
            ((ConferenceListing)myAdapter.getGroup(groupPosition)).setPointer(true);

            for(int i = 0; i < myAdapter.getGroupCount(); i++) {
                if(i != groupPosition) {
                    me.collapseGroup(i);
                }
            }

            for(int i = 0; i < otherAdapter.getGroupCount(); i++) {
                other.collapseGroup(i);
            }
        }
    }

    public class CollapseListener implements ExpandableListView.OnGroupCollapseListener {
        private ExpandableListView me;

        public CollapseListener(ExpandableListView me) {
            this.me = me;
        }

        @Override
        public void onGroupCollapse(int groupPosition) {
            ConferenceListExpandableListAdapter myAdapter = (ConferenceListExpandableListAdapter)me.getExpandableListAdapter();
            ((ConferenceListing)myAdapter.getGroup(groupPosition)).setPointer(false);
        }

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
