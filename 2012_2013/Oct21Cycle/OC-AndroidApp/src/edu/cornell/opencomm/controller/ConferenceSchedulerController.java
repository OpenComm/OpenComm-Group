package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceSchedulerController {

	private ConferenceSchedulerView conferenceSchedulerView;
	private Context context; 
	
	public ConferenceSchedulerController(Context context, ConferenceSchedulerView view){
		this.conferenceSchedulerView = view;
		this.context = context; 
	}
	
	public void addConferencePressed(){
		//		Intent intent = new Intent(this.conferenceView, CreateConferenceView.class);
	}
	
	public void notificationsPressed(){
		//Intent intent = new Intent(this.conferenceView, NotficiationsView.class);
	}
	
	public void blockButtonPressed(){
		//Intent intent = new Intent(this.conferenceView, DashboardView.class);
	}
	
	public void backButtonPressed(){
		Intent intent = new Intent(this.conferenceSchedulerView, DashboardView.class); 
		this.conferenceSchedulerView.startActivity(intent);
	}
	
}
