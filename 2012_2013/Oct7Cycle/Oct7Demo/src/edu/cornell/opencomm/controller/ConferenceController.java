
package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

public class ConferenceController {

	private ConferenceSchedulerView conferenceSchedulerView;
	private Context context; 

	//Constructor - initialize required fields
	public ConferenceController(Context context, ConferenceSchedulerView view){
		this.conferenceSchedulerView = view;
		this.context = context; 
	}
	//TODO
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



