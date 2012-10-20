package edu.cornell.opencomm.view;

import java.util.Calendar;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConferenceCardView extends Activity{
	Conference conference;
	Calendar currentTime;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.conference_card_layout);
	    currentTime = Calendar.getInstance();
	    Bundle b = getIntent().getExtras();
	    conference = b.getParcelable("com.cornell.opencomm.model.Conference");
	    retrieveAndDisplayConferenceInformation();
	    adjustLayoutLookAndFunctionality();
	}
	
	
	public void retrieveAndDisplayConferenceInformation(){
		// Conference Title
		TextView conference_title = (TextView) findViewById(R.id.conference_title);
		conference_title.setText(conference.getConferenceTitle());
		// Description
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(conference.getDescription());
		// Start Time
		TextView start_time = (TextView) findViewById(R.id.start_time);
		start_time.setText(conference.getStartDateAndTimeDescription());
		// End Date
		TextView end_time = (TextView) findViewById(R.id.end_time);
		end_time.setText(conference.getEndDateAndTimeDescription());
		// Reoccurrence
		TextView recurring_time = (TextView) findViewById(R.id.recurring);
		recurring_time.setText(conference.getReoccurence());
		// Inviter
		//TextView inviter_name = (TextView) findViewById(R.id.invited_by_contact);
		//inviter_name.setText(conference.getInviter().getNickname());
		// Attendees
		// TODO
		// Type of conference (Invited/Happening Now/Already Accepted)
	}
	
	public void adjustLayoutLookAndFunctionality(){
		setTypeOfConference();
	}
	
	public void setTypeOfConference(){
		switch(conference.getConferenceType(currentTime)){
		case(Conference.UPCOMING):
			
			break;
		case(Conference.INVITED):
			RelativeLayout invitation_bar = (RelativeLayout) findViewById(R.id.invitation_bar);
			invitation_bar.setVisibility(View.VISIBLE);
			break;
		case(Conference.HAPPENING_NOW):
			RelativeLayout enter_conference_bar = (RelativeLayout) findViewById(R.id.enter_conference_bar);
			enter_conference_bar.setVisibility(View.VISIBLE);
			break;
		}
	}
}

