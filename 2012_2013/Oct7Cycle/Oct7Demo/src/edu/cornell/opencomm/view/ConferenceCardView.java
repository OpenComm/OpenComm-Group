package edu.cornell.opencomm.view;

import java.util.Calendar;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		TextView conference_title = (TextView) findViewById(R.id.conference_title);
		conference_title.setText(conference.getConferenceTitle());
		TextView description = (TextView) findViewById(R.id.description);
		description.setText(conference.getDescription());
		TextView start_time = (TextView) findViewById(R.id.start_time);
		start_time.setText(conference.getStartDateAndTimeDescription());
		TextView end_time = (TextView) findViewById(R.id.end_time);
		end_time.setText(conference.getEndDateAndTimeDescription());
		TextView recurring_time = (TextView) findViewById(R.id.recurring);
		recurring_time.setText(conference.getReoccurence());
		TextView inviter_name = (TextView) findViewById(R.id.invited_by_contact);
		inviter_name.setText(conference.getInviterName());
		retrieveAndDisplayAttendeeInformation();
		
	}
	
	
	public void retrieveAndDisplayAttendeeInformation(){
		String[] attendee_names = conference.getAttendeeNames();
		int[] attendee_pictures = conference.getAttendeePictures();
		LinearLayout conference_information_layout = (LinearLayout) findViewById(R.id.conference_information_linear_layout);
		LayoutInflater layoutInflater = (LayoutInflater) 
		        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	/*	for (int i=0 ; i < attendee_names.length ; i++){
	//		createUserEntry(conference_information_layout, layoutInflater, attendee_names[i]/*, attendee_pictures[i]*///);
	//	}
		
		for (String attendee_name : attendee_names){
			createUserEntry(conference_information_layout, layoutInflater, attendee_name/*, attendee_picture*/);
		} 
	}
	
	public void createUserEntry(LinearLayout conference_information_layout, LayoutInflater layoutInflater, String name/*, int picture*/){
		View attendee_entry = layoutInflater.inflate(R.layout.conference_card_attendee_entry, null);
		RelativeLayout entry_container= (RelativeLayout) attendee_entry.findViewById(R.id.attendee_information);
		TextView attendee_name = (TextView) entry_container.findViewById(R.id.name);
		attendee_name.setText(name);
	/*	ImageView attendee_picture = (ImageView) entry_container.findViewById(R.id.picture);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(picture);
		attendee_picture.setImageDrawable(drawable); */
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

