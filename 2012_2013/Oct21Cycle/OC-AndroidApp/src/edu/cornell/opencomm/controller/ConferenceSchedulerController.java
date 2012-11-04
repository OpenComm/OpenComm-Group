package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import edu.cornell.opencomm.interfaces.SimpleObserver;
import edu.cornell.opencomm.packet.ConferenceCommunicator;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

/**
 * @author 
 *
 */
@SuppressWarnings("unused")
public class ConferenceSchedulerController implements SimpleObserver{

	private ConferenceSchedulerView conferenceSchedulerView;
	private Context context; 
	private ConferenceCommunicator communicator;
	public ConferenceSchedulerController(Context context, ConferenceSchedulerView view){
		this.conferenceSchedulerView = view;
		this.context = context; 
		this.communicator = new ConferenceCommunicator();
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

	/**
	 * Send Pull Request for the list of conferences
	 */
	public void sendPullrequest(){

		communicator.pullConferences(this);
	}
	
	public void backButtonPressed(){
		Intent intent = new Intent(this.conferenceSchedulerView, DashboardView.class); 
		this.conferenceSchedulerView.startActivity(intent);
	}

	//TODO: Everything in this method is an example of bad coding practice in java.
	//Ankit->Ankit: fix it after the demo
	/* (non-Javadoc)
	 * @see edu.cornell.opencomm.interfaces.SimpleObserver#onUpdate(int, java.lang.Object)
	 */
	public void onUpdate(int eventid, Object event) {
		if(eventid == 911){
			String response = (String) event;
			conferenceSchedulerView.dataReceived(response);
		}

	}

	public void onError(String message) {
		// TODO Auto-generated method stub

	}

}
