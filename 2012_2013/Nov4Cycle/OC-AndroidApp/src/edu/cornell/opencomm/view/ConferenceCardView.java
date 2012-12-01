package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Calendar;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConferenceCardView extends Activity{
	Conference conference;
	Calendar currentTime;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.conference_card_layout);
	    currentTime = Calendar.getInstance();
	    conference = (Conference)getIntent().getSerializableExtra("conference");
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
		inviter_name.setText(conference.getInviter().getUsername());
		retrieveAndDisplayAttendeeInformation();
		
	}
	
	
	public void retrieveAndDisplayAttendeeInformation(){
		Log.v("UserCheck", ""+conference.getAttendees().size());
		ArrayList<User> users = conference.getAttendees();
		LinearLayout conference_information_layout = (LinearLayout) findViewById(R.id.conference_information_linear_layout);
		LayoutInflater layoutInflater = (LayoutInflater) 
		        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		for (int i=0 ; i < users.size() ; i++){
			createUserEntry(conference_information_layout, layoutInflater, users.get(i).getUsername(), users.get(i).getImage());
		}
	}
	
	public void createUserEntry(LinearLayout conference_information_layout, LayoutInflater layoutInflater, String name, int picture){
		View attendee_entry = layoutInflater.inflate(R.layout.conference_card_attendee_entry, null);
		RelativeLayout entry_container= (RelativeLayout) attendee_entry.findViewById(R.id.attendee_information);
		TextView attendee_name = (TextView) entry_container.findViewById(R.id.name);
		attendee_name.setText(name);
		ImageView attendee_picture = (ImageView) entry_container.findViewById(R.id.picture);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(picture);
		attendee_picture.setImageDrawable(drawable); 
		conference_information_layout.addView(attendee_entry);
	}
	
	public void adjustLayoutLookAndFunctionality(){
		setTypeOfConference();
		initializeBackButton();
		initializeActionOverflowButton();
	}
	
	public void setTypeOfConference(){
		switch(conference.getConferenceType(currentTime)){
		case(Conference.UPCOMING):
			
			break;
		case(Conference.INVITED):
			RelativeLayout invitation_bar = (RelativeLayout) findViewById(R.id.invitation_bar);
			invitation_bar.setVisibility(View.VISIBLE);
			initializeAcceptButton();
			initializeDeclineButton();
			break;
		case(Conference.HAPPENING_NOW):
			RelativeLayout enter_conference_bar = (RelativeLayout) findViewById(R.id.enter_conference_bar);
			enter_conference_bar.setVisibility(View.VISIBLE);
			initializeEnterConferenceBar();
			break;
		}
	}
	
	public void initializeBackButton(){
		final ImageView backButton = (ImageView) findViewById(R.id.back_button);
		backButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	Intent intent = new Intent(ConferenceCardView.this, ConferenceSchedulerView.class);
	        		startActivity(intent);
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}
	
	public void initializeActionOverflowButton(){
		final ImageView actionOverflowButton = (ImageView) findViewById(R.id.action_overflow_button);
		actionOverflowButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	showToast("Show Action Overflow Options");
	            	// TODO display dropdown of action overflow options
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}
	
	public void initializeAcceptButton(){
		final RelativeLayout acceptButton = (RelativeLayout) findViewById(R.id.accept_button);
		acceptButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	showToast("Accept Invitation");
	            	// TODO accept the invite and return to Conference Scheduling Page
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}
	
	public void initializeDeclineButton(){
		final RelativeLayout declineButton = (RelativeLayout) findViewById(R.id.decline_button);
		declineButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	showToast("Decline Invitation");
	            	// TODO Decline the invite and return to Conference Scheduling Page
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}
	
	public void initializeEnterConferenceBar(){
		final Activity activity = this;
		final RelativeLayout enterConferenceBar = (RelativeLayout) findViewById(R.id.enter_conference_bar);
		enterConferenceBar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            switch (arg1.getAction()) {
	            case MotionEvent.ACTION_DOWN: {
	            	// TODO Change color of button? Ask Design team
	                break;
	            }
	            case MotionEvent.ACTION_UP:{
	            	Intent i = new Intent(activity, ConferenceView.class);
	                Bundle bundle = new Bundle();
	                bundle.putSerializable("conference", conference);
	                 i.putExtras(bundle);
	        		startActivity(i);
	                break;
	            }
	            }
	            return true;
	        }
		}); 
	}
	
	public void showToast(String message){
		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}

