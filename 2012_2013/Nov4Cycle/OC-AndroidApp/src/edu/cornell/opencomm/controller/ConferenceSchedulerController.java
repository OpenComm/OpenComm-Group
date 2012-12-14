package edu.cornell.opencomm.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.cornell.opencomm.interfaces.OCResponseListner;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.packet.ConferenceCommunicator;
import edu.cornell.opencomm.util.TestDataGen;
import edu.cornell.opencomm.view.ConferenceSchedulerView;
import edu.cornell.opencomm.view.DashboardView;

/**
 * @author 
 *
 */
public class ConferenceSchedulerController implements OCResponseListner{

	private ConferenceSchedulerView conferenceSchedulerView;
	private Context context; 
	private ConferenceCommunicator communicator;
	public ConferenceSchedulerController(Context context, ConferenceSchedulerView view){
		this.conferenceSchedulerView = view;
		this.context = context; 
		this.communicator = new ConferenceCommunicator();
	}

	public void addConferencePressed(){
		//TODO [frontend] launch add conference page
		Toast.makeText(this.context, "Action Bar: Add Conference clicked", Toast.LENGTH_SHORT).show();
		//Using TestData Gen with actuall user data
		//Conference confToAdd = TestDataGen.getCurrentConference();
		//communicator.pushConference(confToAdd,this);
		Intent i = new Intent(this.conferenceSchedulerView, ConferenceCreationController.class);
		this.conferenceSchedulerView.startActivity(i);
		
	}

	public void notificationsPressed(){
		//TODO [frontend] launch notifications overlay
		Toast.makeText(this.context, "Action Bar: Notifications clicked", Toast.LENGTH_SHORT).show();
	}

	public void overflowPressed(){
		//TODO [frontend] launch overflow overlay
		Toast.makeText(this.context, "Action Bar: Overflow clicked", Toast.LENGTH_SHORT).show();
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
	public void onResponse(int eventid, Object event) {
		if(eventid == 911){
			String response = (String) event;
			conferenceSchedulerView.dataReceived(response);
		}

	}

	public void onError(String message) {
		// TODO Auto-generated method stub

	}

}
