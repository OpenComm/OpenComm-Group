package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.ContextTracker;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.view.ConferenceListExpandableListAdapter;
import edu.cornell.opencomm.view.ConferenceListing;

public class ConferenceListActivity extends Activity {

    private View layout;
    private ConferenceListExpandableListAdapter upcomingAdapter;
    private ConferenceListExpandableListAdapter happeningNowAdapter;
    private int monthDisplay,dayDisplay, yearDisplay;
    private Calendar stock = Calendar.getInstance(); //stock calendar
    private TextView textView;
    private Button theButton;
    private ArrayList<Conference> conferences;
    ExpandableListView upcomingConferences;
    ExpandableListView happeningNowConferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextTracker.setContext(this);

        layout = getLayoutInflater().inflate(R.layout.conference_list_layout, null);
        setContentView(layout);

        ArrayList<String> openComm = new ArrayList<String>();

        openComm.add("Ashley");
        openComm.add("Risa");
        openComm.add("Joey");
        openComm.add("Rahul");
        openComm.add("Brandon");
        openComm.add("Natalie");
        openComm.add("Chris");
        openComm.add("Vinay");
        openComm.add("Crystal");
        openComm.add("Kris");
        openComm.add("Jonathan");
        openComm.add("Flavian");
        openComm.add("Najla");
        openComm.add("Justin");

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

        ArrayList<String> nora = new ArrayList<String>();
        nora.add("Nora Ng-Quinn");
       //Crystal: giving the conferences the invitee list for testing
        String[] s6 = new String[]{"Jonathan Pullano"};
        String[] s1= new String[]{"Vinay Maloo"};
        String[] s2=new String[]{"Flavian"};

        String[] s3=new String[]{"Najla Elmachtoub"};
        String[] s4=new String[]{"Graeme Bailey"};
        String[] s5=new String[]{"Ashley","Risa","Joey","Rahul",
        "Brandon",
        "Natalie",
        "Chris",
        "Vinay",
        "Crystal",
        "Kris",
        "Jonathan",
        "Flavian",
        "Najla",
       "Justin"};
        String[] s7=new String[]{"Nora Ng-Quinn"};
        String[] s8=new String[]{"Hurp"};
        String[] s9=new String[]{"Derp"};

        ArrayList<String> hurp = new ArrayList<String>();
        hurp.add("Hurpy McHurp");

        ArrayList<String> derp = new ArrayList<String>();
        derp.add("Derpman");

         conferences = new ArrayList<Conference>();
      /*  Conference conference1 = new Conference(2012, 4, 27, 10, 00, 12, 00, s1, "Vinay Maloo", "Graduation", mom);

        Conference conference2 = new Conference(2012, 10, 9, 12, 30, 13, 30, s2, "Flavian", "Wine+Cheese", flavian);
        Conference conference3 = new Conference(2012, 5, 25, 8, 30, 9, 15, s3, "Najla Elmachtoub", "Canada Trip", najla);
        Conference conference4 = new Conference(2015, 2, 14, 9, 26, 5, 35, s4, "Graeme Bailey", "Pi Day", graeme);
        Conference conference5 = new Conference(2012, 3, 12, 17, 45, 19, 45, s5, "Risa Naka", "OpenComm", openComm);
        Conference conference6 = new Conference(2012, 3, 12, 0, 0, 12, 0, s6, "Jonathan Pullano", "Debugging Late", jonathan);
        Conference conference7 = new Conference(2012, 5, 16, 18, 24, 19, 51, s7, "Nora Ng-Quinn", "lolcat party", nora);
        Conference conference8 = new Conference(2013, 0, 1, 18, 24, 19, 51, s8, "Hurp", "ABC", hurp);
        Conference conference9 = new Conference(2013, 0, 2, 18, 24, 19, 51, s9, "Derp", "123", derp);
        Conference conference10 = new Conference(2013, 0, 3, 18, 24, 19, 51, s9, "Derp", "YOU", derp);
        Conference conference11 = new Conference(2013, 0, 4, 18, 24, 19, 51, s9, "Derp", "AND", derp);
        Conference conference12 = new Conference(2013, 0, 5, 18, 24, 19, 51, s9, "Derp", "ME!", derp);
        Conference conference13 = new Conference(2013, 0, 6, 18, 24, 19, 51, s9, "Derp", "SecondToLast", derp);
        Conference conference14 = new Conference(2013, 0, 7, 18, 24, 19, 51, s9, "Derp", "Last", derp);
        
        conferences.add(conference1);
        conferences.add(conference2);
        conferences.add(conference3);
        conferences.add(conference4);
        conferences.add(conference5);
        conferences.add(conference6);
        conferences.add(conference7);

        conferences.add(conference8);
        conferences.add(conference9);
        conferences.add(conference10);
        conferences.add(conference11);
        conferences.add(conference12);
        conferences.add(conference13);
        conferences.add(conference14);
        initialize(conferences);
             */
        monthDisplay = stock.get(Calendar.MONTH);
        dayDisplay = stock.get(Calendar.DAY_OF_MONTH);
        yearDisplay=stock.get(Calendar.YEAR);

        textView= getDateText();
        textView.setFocusable(false);

        setDateText();

        getLeftArrow().setOnClickListener(onLeftArrowListener);
        getRightArrow().setOnClickListener(onRightArrowListener);

        theButton = getTheButton();
        theButton.setVisibility(View.VISIBLE);
        theButton.setBackgroundColor(Color.TRANSPARENT);
        theButton.setOnClickListener(onDatePickerListener);

    } // end onCreate method

    private void initialize(Collection<Conference> conferences) {
        ArrayList<Conference> currentConferences = new ArrayList<Conference>();
        ArrayList<Conference> futureConferences = new ArrayList<Conference>();
        for(Conference conference : conferences) {
            if(conference.isNow())
                currentConferences.add(conference);
            else if(conference.isFuture())
                futureConferences.add(conference);
        }
        upcomingAdapter = new ConferenceListExpandableListAdapter(this, futureConferences);
        happeningNowAdapter = new ConferenceListExpandableListAdapter(this, currentConferences);
        upcomingConferences = getUpcomingConferences();
        happeningNowConferences = getHappeningNowConferences();

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


	View.OnClickListener onDatePickerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.v("CLICK", "Should be popping up datePicker!");
			handleDatePickerButtonClicked();
		}};

		//This is the controller for now
		private void handleDatePickerButtonClicked() {
			new DatePickerDialog(this, dateSetListener,stock.get(Calendar.YEAR),stock.get(Calendar.MONTH),stock.get(Calendar.DAY_OF_MONTH)).show();
		};


		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				dayDisplay = dayOfMonth;
				monthDisplay = monthOfYear;
				yearDisplay = year;
				stock.set(yearDisplay, monthDisplay, dayDisplay);
				setDateText();
			}

		};

		private void setDateText(){

			int tempMonthDisplay=stock.get(Calendar.MONTH)+1;
			int tempDayDisplay=stock.get(Calendar.DAY_OF_MONTH);
			boolean confFound=false;

			//Scroll upcoming
			for(int i=0;i<upcomingAdapter.getGroupCount();i++){
//				int startMonth = ((Conference)upcomingAdapter.getGroup(i)).getStartMonth();
//				int startDay = ((Conference)upcomingAdapter.getGroup(i)).getStartDay();

//				int startMonth = conferences.get(i).getStartMonth();
//				int startDay= conferences.get(i).getStartDay();

				int startMonth = upcomingAdapter.conferenceListings.get(i).getConference().getStartMonth();
				int startDay= upcomingAdapter.conferenceListings.get(i).getConference().getStartDay();

				if(startMonth == tempMonthDisplay-1 && startDay ==tempDayDisplay){
					Log.v("HOLY CRAP", "YOU HAVE A CONFERENCE ON THAT DAY");

					upcomingConferences.smoothScrollToPosition(i);
					confFound=true;
				}
				if(confFound){break;}
			}

			//Scroll happening now?? (I think its un-needed)
//			for(int i=0;i<happeningNowAdapter.getGroupCount();i++){
////				int startMonth = ((Conference)upcomingAdapter.getGroup(i)).getStartMonth();
////				int startDay = ((Conference)upcomingAdapter.getGroup(i)).getStartDay();
//
////				int startMonth = conferences.get(i).getStartMonth();
////				int startDay= conferences.get(i).getStartDay();
//
//				int startMonth = happeningNowAdapter.conferenceListings.get(i).getConference().getStartMonth();
//				int startDay= happeningNowAdapter.conferenceListings.get(i).getConference().getStartDay();
//
//				if(startMonth == tempMonthDisplay-1 && startDay ==tempDayDisplay){
//					Log.v("HOLY CRAP", "YOU HAVE A CONFERENCE ON THAT DAY");
//
//					upcomingConferences.smoothScrollToPosition(i);
//					confFound=true;
//				}
//				if(confFound){break;}
//			}
//
			if(confFound==false){
			Log.v("DANG","NO CONFERENCES ON " + tempMonthDisplay+"/" + tempDayDisplay);}
			else{

				Log.v("OMG","FOUND A CONFERENCE");
			}
		textView.setText(" " +tempMonthDisplay + "/" + tempDayDisplay);
		}

		public TextView getDateText(){
			return (TextView)layout.findViewById(R.id.date);
		}

		public ImageView getLeftArrow(){
			return (ImageView)layout.findViewById(R.id.leftArrow);
		}

		public ImageView getRightArrow(){
			return (ImageView)layout.findViewById(R.id.rightArrow);
		}

		public Button getTheButton(){
			return (Button)layout.findViewById(R.id.dateButton);
		}

		View.OnClickListener onLeftArrowListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stock.add(Calendar.DAY_OF_MONTH, -1);
				setDateText();
			}
		};

		View.OnClickListener onRightArrowListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				stock.add(Calendar.DAY_OF_MONTH, 1);
				setDateText();
			}
		};
}

